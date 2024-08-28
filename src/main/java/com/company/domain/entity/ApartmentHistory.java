package com.company.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    // Constructor with parameters
    public ApartmentHistory(Resident resident, Apartment oldApartment, Apartment newApartment, LocalDate changeDate,
                            User createdBy, User updatedBy) {
        this.resident = resident;
        this.oldApartment = oldApartment;
        this.newApartment = newApartment;
        this.changeDate = changeDate;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }
}
