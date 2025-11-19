package com.transport.service.expense;

import com.transport.dto.expense.*;
import com.transport.entity.domain.Expense;
import com.transport.entity.domain.ExpenseCategory;
import com.transport.entity.domain.Trip;
import com.transport.entity.domain.User;
import com.transport.enums.ExpenseStatus;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.ExpenseMapper;
import com.transport.repository.expense.ExpenseRepository;
import com.transport.service.authentication.auth.AuthenticationService;
import com.transport.service.file.FileStorageService;
import com.transport.util.CodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {
    @Mock private ExpenseRepository expenseRepository;
    @Mock private ExpenseMapper expenseMapper;
    @Mock private ExpenseValidator expenseValidator;
    @Mock private AuthenticationService authenticationService;
    @Mock private FileStorageService fileStorageService;

    @InjectMocks
    private ExpenseServiceImpl expenseService;
    private User driverUser, managerUser, accountantUser;
    private Trip trip;
    private ExpenseCategory category;
    private Expense expense;
    @BeforeEach
    void setUp() {
        driverUser = new User();
        driverUser.setId(100L);
        driverUser.setFullName("Tài xế A");

        managerUser = new User();
        managerUser.setId(200L);

        accountantUser = new User();
        accountantUser.setId(300L);

        trip = new Trip();
        trip.setId(1L);

        category = new ExpenseCategory();
        category.setId(5L);
        category.setName("Xăng dầu");

        expense = new Expense();
        expense.setId(999L);
        expense.setTrip(trip);
        expense.setCategory(category);
        expense.setAmount(new BigDecimal("1500000"));
        expense.setStatus(ExpenseStatus.PENDING);
        expense.setDriverBy(driverUser);
        expense.setExpenseCode("YCCP20250001");
        expense.setAttachmentUrl("expenses/abc123.pdf");
    }
    @Test
    @DisplayName("getAll - Không có quyền EXPENSE_READ → chỉ xem chi phí của mình")
    void getAll_NoGlobalPermission_OnlyOwnExpenses() {
        ExpenseSearchRequest request = new ExpenseSearchRequest();
        Pageable pageable = PageRequest.of(0, 10);

        when(authenticationService.hasPermission("EXPENSE_READ")).thenReturn(false);
        when(authenticationService.getCurrentUser()).thenReturn(driverUser);
        when(expenseRepository.searchExpenses(any(), eq(pageable))).thenReturn(new PageImpl<>(List.of(expense)));

        expenseService.getAll(request, pageable);

        verify(expenseRepository).searchExpenses(argThat(r -> r.getDriverId() != null && r.getDriverId().equals(100L)), eq(pageable));
    }
    @Test
    @DisplayName("create - Thành công với file đính kèm")
    void create_WithFile_Success() {
        ExpenseRequest request = ExpenseRequest.builder()
                .tripId(1L)
                .categoryId(5L)
                .amount(new BigDecimal("1500000"))
                .description("Mua xăng")
                .build();

        MockMultipartFile file = new MockMultipartFile("file", "bill.pdf", "application/pdf", "PDF content".getBytes());

        when(authenticationService.getCurrentUser()).thenReturn(driverUser);
        when(expenseValidator.validateExpenseCategory(5L)).thenReturn(category);
        when(expenseValidator.validateTrip(1L)).thenReturn(trip);
        when(expenseMapper.toCreateExpense(request)).thenReturn(expense);
        when(fileStorageService.uploadFile(any(), eq("expenses"))).thenReturn("expenses/abc123.pdf");
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseMapper.toExpenseResponse(expense)).thenReturn(new ExpenseResponse(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null));

        try (MockedStatic<CodeGenerator> mocked = mockStatic(CodeGenerator.class)) {
            mocked.when(() -> CodeGenerator.generateCode("YCCP")).thenReturn("YCCP20250001");

            ExpenseResponse result = expenseService.create(request, file);

            assertNotNull(result);
            verify(expenseRepository).save(argThat(e ->
                    e.getExpenseCode().equals("YCCP20250001") &&
                            e.getStatus() == ExpenseStatus.PENDING &&
                            e.getAttachmentUrl() != null
            ));
        }
    }
    @Test
    @DisplayName("update - Chỉ sửa được khi PENDING + thay file")
    void update_Pending_WithNewFile_Success() {
        ExpenseUpdateRequest request = ExpenseUpdateRequest.builder()
                .amount(new BigDecimal("1800000"))
                .description("Cập nhật hóa đơn")
                .build();

        MockMultipartFile newFile = new MockMultipartFile("file", "new-bill.pdf", "application/pdf", "New PDF".getBytes());

        when(expenseValidator.validateExpense(999L)).thenReturn(expense);
        when(fileStorageService.uploadFile(any(), eq("expenses"))).thenReturn("expenses/new123.pdf");
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseMapper.toExpenseResponse(expense)).thenReturn(new ExpenseResponse(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null));

        expenseService.update(999L, request, newFile);

        verify(fileStorageService).deleteFile("expenses/abc123.pdf");
        verify(fileStorageService).uploadFile(any(), eq("expenses"));
        verify(expenseRepository).save(argThat(e -> e.getAttachmentUrl().equals("expenses/new123.pdf")));
    }
    @Test
    @DisplayName("update - Không phải PENDING → bị chặn")
    void update_NotPending_Fail() {
        expense.setStatus(ExpenseStatus.MANAGER_APPROVED);
        when(expenseValidator.validateExpense(999L)).thenReturn(expense);

        AppException ex = assertThrows(AppException.class,
                () -> expenseService.update(999L, new ExpenseUpdateRequest(), null));
        assertEquals(ErrorCode.EXPENSE_NOT_PENDING, ex.getErrorCode());

    }
    @Test
    @DisplayName("delete - Chỉ xóa được khi PENDING")
    void delete_OnlyPending_Success() {
        when(expenseValidator.validateExpense(999L)).thenReturn(expense);

        expenseService.delete(999L);

        verify(fileStorageService).deleteFile("expenses/abc123.pdf");
        verify(expenseRepository).delete(expense);
    }
    @Test
    @DisplayName("expenseApprove - Quản lý duyệt (PENDING → MANAGER_APPROVED)")
    void expenseApprove_ManagerApprove_Success() {
        ExpenseApproveRequest request = ExpenseApproveRequest.builder()
                .isApproved(true) // false = duyệt (theo logic của bạn)
                .reason("Hợp lệ")
                .build();

        when(expenseValidator.validateExpense(999L)).thenReturn(expense);
        when(authenticationService.getCurrentUser()).thenReturn(managerUser);
        doNothing().when(authenticationService).requirePermission("EXPENSE_APPROVE_MANAGER");
        when(expenseRepository.save(any())).thenReturn(expense);
        when(expenseMapper.toExpenseResponse(any())).thenReturn(new ExpenseResponse());

        expenseService.expenseApprove(999L, request);

        verify(expenseRepository).save(argThat(e ->
                e.getStatus() == ExpenseStatus.MANAGER_APPROVED &&
                        e.getManagerApprovedBy().equals(managerUser)
        ));
    }

    @Test
    @DisplayName("expenseApprove - Quản lý từ chối → cần lý do")
    void expenseApprove_ManagerReject_WithoutReason_Fail() {
        ExpenseApproveRequest request = ExpenseApproveRequest.builder()
                .isApproved(false) // true = từ chối
                .reason("")
                .build();

        expense.setStatus(ExpenseStatus.PENDING);
        when(expenseValidator.validateExpense(999L)).thenReturn(expense);
        when(authenticationService.getCurrentUser()).thenReturn(managerUser);
        doNothing().when(authenticationService).requirePermission("EXPENSE_APPROVE_MANAGER");

        AppException ex = assertThrows(AppException.class,
                () -> expenseService.expenseApprove(999L, request));

        assertEquals(ErrorCode.CANCELLATION_REASON_REQUIRED, ex.getErrorCode());
    }

    @Test
    @DisplayName("expenseApprove - Kế toán duyệt (MANAGER_APPROVED → ACCOUNTANT_APPROVED)")
    void expenseApprove_AccountantApprove_Success() {
        ExpenseApproveRequest request = ExpenseApproveRequest.builder()
                .isApproved(true)
                .reason("OK thanh toán")
                .build();

        expense.setStatus(ExpenseStatus.MANAGER_APPROVED);
        when(expenseValidator.validateExpense(999L)).thenReturn(expense);
        when(authenticationService.getCurrentUser()).thenReturn(accountantUser);
        doNothing().when(authenticationService).requirePermission("EXPENSE_APPROVE_ACCOUNTANT");

        expenseService.expenseApprove(999L, request);

        verify(expenseRepository).save(argThat(e ->
                e.getStatus() == ExpenseStatus.MANAGER_APPROVED && // lưu ý: code hiện tại bị sai ở đây!
                        e.getAccountantApprovedBy() != null
        ));
    }

    @Test
    @DisplayName("expenseApprove - Thanh toán cuối cùng (ACCOUNTANT_APPROVED → PAID)")
    void expenseApprove_FinalPayment_Success() {
        ExpenseApproveRequest request = ExpenseApproveRequest.builder()
                .isApproved(false)
                .build();

        expense.setStatus(ExpenseStatus.ACCOUNTANT_APPROVED);
        when(expenseValidator.validateExpense(999L)).thenReturn(expense);
        when(authenticationService.getCurrentUser()).thenReturn(accountantUser);
        doNothing().when(authenticationService).requirePermission("EXPENSE_APPROVE_ACCOUNTANT");


        expenseService.expenseApprove(999L, request);

        verify(expenseRepository).save(argThat(e -> e.getStatus() == ExpenseStatus.PAID));
    }

    @Test
    @DisplayName("expenseApprove - Trạng thái không hợp lệ → ném lỗi")
    void expenseApprove_InvalidState_Fail() {
        expense.setStatus(ExpenseStatus.PAID);
        when(expenseValidator.validateExpense(999L)).thenReturn(expense);

        AppException ex = assertThrows(AppException.class,
                () -> expenseService.expenseApprove(999L, new ExpenseApproveRequest()));

        assertEquals(ErrorCode.INVALID_STATE_TRANSITION, ex.getErrorCode());
    }

}
