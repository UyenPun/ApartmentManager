package com.company.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

// Cau 1, 2
@Entity
@Table(name = "`apartments`")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Apartment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "apartment_number", length = 10, nullable = false)
    @NonNull
    private String apartmentNumber;

    @Column(name = "area", nullable = false)
    @NonNull
    private Float area;

    @Column(name = "num_rooms", nullable = false)
    @NonNull
    private Integer numRooms;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Resident> residents;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Utility> utilities;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AdditionalCharge> additionalCharges;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MonthlyFee> monthlyFees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy; // Liên kết tới bảng User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonIgnore
    private User updatedBy; // Liên kết tới bảng User
}
