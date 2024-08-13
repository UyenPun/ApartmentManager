package com.company.dto;
//Cau 2
import java.util.Date;

import lombok.Data;

@Data
public class ApartmentDTO {
	private Integer id;
	private String apartmentNumber;
	private Float area;
	private Integer numRooms;
	private String search;
	private Date minCreatedDate;
	private Date maxCreatedDate;
	private Integer minYear;

	public ApartmentDTO(Integer id, String apartmentNumber, Float area, Integer numRooms) {
		this.id = id;
		this.apartmentNumber = apartmentNumber;
		this.area = area;
		this.numRooms = numRooms;
	}
}
