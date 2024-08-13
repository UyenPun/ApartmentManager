package com.company.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
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

	public enum PaymentStatus {
		PAID("Paid"), UNPAID("Unpaid");

		private String value;

		PaymentStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static PaymentStatus toEnum(String sqlStatus) {
			for (PaymentStatus item : PaymentStatus.values()) {
				if (item.getValue().equals(sqlStatus)) {
					return item;
				}
			}
			return null;
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