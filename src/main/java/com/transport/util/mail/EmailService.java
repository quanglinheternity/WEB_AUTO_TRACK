package com.transport.util.mail;

import com.transport.dto.salary.SalaryReportEmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Async
    public void sendSalaryReport(SalaryReportEmailDTO emailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDTO.getDriverEmail());
            helper.setSubject("Báo cáo lương tháng " + formatMonth(emailDTO.getReportMonth()));
            helper.setFrom(fromEmail);

            String htmlContent = buildEmailContent(emailDTO);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Đã gửi email báo cáo lương cho: {}", emailDTO.getDriverEmail());

        } catch (MessagingException e) {
            log.error("Lỗi khi gửi email cho: {}", emailDTO.getDriverEmail(), e);
            throw new RuntimeException("Không thể gửi email báo cáo lương", e);
        }
    }

    private String buildEmailContent(SalaryReportEmailDTO dto) {
        Context context = new Context();
        context.setVariable("driverName", dto.getDriverName());
        context.setVariable("reportMonth", formatMonth(dto.getReportMonth()));
        context.setVariable("totalTrips", dto.getTotalTrips());
        context.setVariable("totalDistance", formatNumber(dto.getTotalDistance()));
        context.setVariable("baseSalary", formatCurrency(dto.getBaseSalary()));
        context.setVariable("tripBonus", formatCurrency(dto.getTripBonus()));
        context.setVariable("allowance", formatCurrency(dto.getAllowance()));
        context.setVariable("deduction", formatCurrency(dto.getDeduction()));
        context.setVariable("totalSalary", formatCurrency(dto.getTotalSalary()));
        context.setVariable("note", dto.getNote() != null ? dto.getNote() : "");

        return templateEngine.process("email/salary-report-email", context);
    }

    private String formatMonth(YearMonth yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        return yearMonth.format(formatter);
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0 ₫";
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    private String formatNumber(BigDecimal number) {
        if (number == null) return "0";
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return formatter.format(number);
    }
}
