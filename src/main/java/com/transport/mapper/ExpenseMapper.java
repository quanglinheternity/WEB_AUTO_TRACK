package com.transport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.transport.dto.expense.ExpenseRequest;
import com.transport.dto.expense.ExpenseResponse;
import com.transport.dto.expense.ExpenseUpdateRequest;
import com.transport.entity.domain.Expense;
import com.transport.enums.ExpenseStatus;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExpenseMapper {
    @Mapping(target = "nameCategory", source = "category.name")
    @Mapping(target = "codeTrip", source = "trip.tripCode")
    @Mapping(target = "driverByName", source = "driverBy.fullName")
    @Mapping(target = "accountantApprovedByName", source = "accountantApprovedBy.fullName")
    @Mapping(target = "managerApprovedByName", source = "managerApprovedBy.fullName")
    @Mapping(target = "textStatus", source = "status", qualifiedByName = "mapStatusToDescription")
    ExpenseResponse toExpenseResponse(Expense expense);

    @Named("mapStatusToDescription")
    default String mapStatusToDescription(ExpenseStatus status) {
        return status != null ? status.getDescription() : null;
    }

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "driverBy", ignore = true)
    @Mapping(target = "managerApprovedBy", ignore = true)
    @Mapping(target = "accountantApprovedBy", ignore = true)
    @Mapping(target = "accountantApprovedAt", ignore = true)
    @Mapping(target = "accountantNote", ignore = true)
    @Mapping(target = "attachmentUrl", ignore = true)
    @Mapping(target = "expenseCode", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "managerApprovedAt", ignore = true)
    @Mapping(target = "managerNote", ignore = true)
    @Mapping(target = "receiptNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    Expense toCreateExpense(ExpenseRequest expenseRequest);

    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "driverBy", ignore = true)
    @Mapping(target = "managerApprovedBy", ignore = true)
    @Mapping(target = "accountantApprovedBy", ignore = true)
    @Mapping(target = "accountantApprovedAt", ignore = true)
    @Mapping(target = "accountantNote", ignore = true)
    @Mapping(target = "attachmentUrl", ignore = true)
    @Mapping(target = "expenseCode", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "managerApprovedAt", ignore = true)
    @Mapping(target = "managerNote", ignore = true)
    @Mapping(target = "receiptNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateExpenseFromRequest(ExpenseUpdateRequest request, @MappingTarget Expense expense);
}
