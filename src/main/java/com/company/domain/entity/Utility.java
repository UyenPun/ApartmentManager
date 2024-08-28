package com.company.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "utilities")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Utility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    @NonNull
    @JsonBackReference
    private Apartment apartment;

    @Column(name = "electricity_usage", nullable = false)
    @NonNull
    private Integer electricityUsage;

    @Column(name = "water_usage", nullable = false)
    @NonNull
    private Integer waterUsage;

    @Column(name = "total_amount", nullable = false)
    @NonNull
    private BigDecimal totalAmount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_status", nullable = false)
    @Convert(converter = PaymentStatusConverter.class)
    @NonNull
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy; // Liên kết tới bảng User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy; // Liên kết tới bảng User

    public enum PaymentStatus {
        PAID("Paid"), UNPAID("Unpaid");

        private final String value;

        PaymentStatus(String value) {
            this.value = value;
        }

        public static PaymentStatus toEnum(String sqlStatus) {
            for (PaymentStatus item : PaymentStatus.values()) {
                if (item.getValue().equals(sqlStatus)) {
                    return item;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }
    }
}

@Converter(autoApply = true)
class PaymentStatusConverter implements AttributeConverter<Utility.PaymentStatus, String> {

    @Override
    public String convertToDatabaseColumn(Utility.PaymentStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public Utility.PaymentStatus convertToEntityAttribute(String sqlStatus) {
        if (sqlStatus == null) {
            return null;
        }
        return Utility.PaymentStatus.toEnum(sqlStatus);
    }
}