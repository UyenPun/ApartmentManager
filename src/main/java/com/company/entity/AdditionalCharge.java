package com.company.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
