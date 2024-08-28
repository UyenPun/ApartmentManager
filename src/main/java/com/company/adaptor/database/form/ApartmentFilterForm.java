package com.company.adaptor.database.form;

import lombok.Data;

import java.util.Date;

@Data
public class ApartmentFilterForm {
    private String apartmentNumber;
    private Float minArea;
    private Float maxArea;
    private Integer minRooms;
    private Integer maxRooms;
    private Date minCreatedDate;
    private Date maxCreatedDate;
    private Integer minYear;
    private String search; // Sẽ sử dụng cho cả tên cư dân hoặc số căn hộ.

}
