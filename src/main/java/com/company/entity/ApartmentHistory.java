// ApartmentHistory.java
package com.company.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "apartment_history")
@Getter
@Setter
@NoArgsConstructor
public class ApartmentHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "resident_id", nullable = false)
	private Resident resident;

	@ManyToOne
	@JoinColumn(name = "old_apartment_id")
	private Apartment oldApartment;

	@ManyToOne
	@JoinColumn(name = "new_apartment_id")
	private Apartment newApartment;

	@Column(name = "change_date", nullable = false)
	private LocalDate changeDate;

	// Constructor with parameters
	public ApartmentHistory(Resident resident, Apartment oldApartment, Apartment newApartment, LocalDate changeDate) {
		this.resident = resident;
		this.oldApartment = oldApartment;
		this.newApartment = newApartment;
		this.changeDate = changeDate;
	}
}
