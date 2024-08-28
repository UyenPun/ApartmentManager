package com.company.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "residents")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Resident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 255, nullable = false)
    @NonNull
    private String name;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    @NonNull
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "id_card", nullable = false, length = 20, unique = true)
    @NonNull
    private String idCard;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender", nullable = false)
    @NonNull
    private ResidentGender gender;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    @JsonBackReference
    private Apartment apartment;

    @Column(name = "moved_in_date")
    private LocalDate movedInDate;

    @Column(name = "moved_out_date")
    private LocalDate movedOutDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ResidentStatus status = ResidentStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private User deletedBy;

    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MonthlyFee> monthlyFees;

    public enum ResidentGender {
        MALE("Male"), FEMALE("Female"), OTHER("Other");

        private final String value;

        ResidentGender(String value) {
            this.value = value;
        }

        public static ResidentGender toEnum(String sqlStatus) {
            if (sqlStatus == null) {
                return null;
            }
            for (ResidentGender item : ResidentGender.values()) {
                if (item.getValue().equalsIgnoreCase(sqlStatus)) {
                    return item;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ResidentStatus {
        ACTIVE, INACTIVE
    }
}

@Converter(autoApply = true)
class GenderConverter implements AttributeConverter<Resident.ResidentGender, String> {

    @Override
    public String convertToDatabaseColumn(Resident.ResidentGender gender) {
        return (gender == null) ? null : gender.getValue();
    }

    @Override
    public Resident.ResidentGender convertToEntityAttribute(String value) {
        return (value == null) ? null : Resident.ResidentGender.toEnum(value);
    }
}
