package com.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "`monthly_fees`")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class MonthlyFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    @NonNull
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "resident_id", nullable = false)
    @NonNull
    private Resident resident;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @Column(name = "fee_month", nullable = false)
    @NonNull
    private LocalDate feeMonth;

    @Column(name = "email_sent", nullable = false)
    private Boolean emailSent = false;
}
