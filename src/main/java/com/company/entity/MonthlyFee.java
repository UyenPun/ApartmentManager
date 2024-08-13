package com.company.entity;

import java.io.Serializable;
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

	@Column(name = "fee_month", nullable = false)
	@NonNull
	private LocalDate feeMonth;

	@Column(name = "email_sent", nullable = false)
	private Boolean emailSent = false;
}
