package com.company.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "`additional_charges`")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class AdditionalCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    @NonNull
    private Apartment apartment;

    @Column(name = "charge_description", length = 255, nullable = false)
    @NonNull
    private String chargeDescription;

    @Column(name = "amount", nullable = false)
    @NonNull
    private BigDecimal amount;

    @Column(name = "charge_date", nullable = false)
    @NonNull
    private LocalDate chargeDate;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by", nullable = false)
    private User updatedBy;
}
