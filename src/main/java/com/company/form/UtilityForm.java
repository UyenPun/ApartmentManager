package com.company.form;

import com.company.entity.Utility.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UtilityForm {
    private Integer apartmentId; 
    private Integer electricityUsage;
    private Integer waterUsage;
    private BigDecimal totalAmount;
    private LocalDate paymentDate;
    private PaymentStatus paymentStatus;
}
