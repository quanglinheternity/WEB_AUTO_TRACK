package com.transport.dto.salary;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaySalaryRequest {
    private List<Long> salaryReportIds;
    private String note;
}