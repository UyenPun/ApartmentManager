package com.company.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`users`")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Role {
		ADMIN, MANAGER, RESIDENT
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "username", length = 255, nullable = false, unique = true)
	@NonNull
	private String username;

	@Column(name = "email", length = 255, nullable = false, unique = true)
	@NonNull
	private String email;

	@Column(name = "password", length = 255, nullable = false)
	@NonNull
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	@NonNull
	private Role role;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
	private List<Apartment> createdApartments;

	@OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL)
	private List<Apartment> updatedApartments;

	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
	private List<Utility> createdUtilities;

	@OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL)
	private List<Utility> updatedUtilities;

	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
	private List<AdditionalCharge> createdAdditionalCharges;

	@OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL)
	private List<AdditionalCharge> updatedAdditionalCharges;

	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
	private List<ApartmentHistory> createdApartmentHistories;

	@OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL)
	private List<ApartmentHistory> updatedApartmentHistories;

	@OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
	private List<MonthlyFee> createdMonthlyFees;

	@OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL)
	private List<MonthlyFee> updatedMonthlyFees;
}
