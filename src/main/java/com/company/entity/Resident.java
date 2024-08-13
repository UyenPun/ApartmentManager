// Resident.java
package com.company.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    @Column(name = "email", nullable = false, length = 255)
    @NonNull
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "id_card", nullable = false, length = 20)
    @NonNull
    private String idCard;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "gender", nullable = false)
    @Convert(converter = GenderConverter.class)
    @NonNull
    private ResidentGender gender;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Column(name = "moved_in_date")
    private LocalDate movedInDate;

    @Column(name = "moved_out_date")
    private LocalDate movedOutDate;

    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private List<MonthlyFee> monthlyFees;

    public enum ResidentGender {
        MALE("Male"), FEMALE("Female"), OTHER("Other");

        private String value;

        private ResidentGender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ResidentGender toEnum(String sqlStatus) {
            for (ResidentGender item : ResidentGender.values()) {
                if (item.getValue().equals(sqlStatus)) {
                    return item;
                }
            }
            return null;
        }
    }
}

@Converter(autoApply = true)
class GenderConverter implements AttributeConverter<Resident.ResidentGender, String> {

    @Override
    public String convertToDatabaseColumn(Resident.ResidentGender name) {
        if (name == null) {
            return null;
        }
        return name.getValue();
    }

    @Override
    public Resident.ResidentGender convertToEntityAttribute(String sqlName) {
        if (sqlName == null) {
            return null;
        }
        return Resident.ResidentGender.toEnum(sqlName);
    }
}
