package com.company.presentation.rest.apartment.response;
//Cau 2

import com.company.domain.entity.Apartment;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ApartmentResponse {
    private Integer id;
    private String apartmentNumber;
    private Float area;
    private Integer numRooms;
    private String search;

    public ApartmentResponse(Integer id, String apartmentNumber, Float area, Integer numRooms) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.area = area;
        this.numRooms = numRooms;
    }

    public static Page<ApartmentResponse> create(Page<Apartment> apartmentPage) {
        return apartmentPage.map(ApartmentResponse::convertToDTO);
    }


    private static ApartmentResponse convertToDTO(Apartment apartment) {
        return new ApartmentResponse(apartment.getId(), apartment.getApartmentNumber(), apartment.getArea(),
                apartment.getNumRooms());
    }
}