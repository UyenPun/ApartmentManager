package com.company.presentation.rest.apartment.response;
//Cau 2

import com.company.domain.entity.Apartment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ApartmentResponse {
    private Integer id;
    private String apartmentNumber;
    private Float area;
    private Integer numRooms;

    public static Page<ApartmentResponse> create(Page<Apartment> apartmentPage) {
        return apartmentPage.map(ApartmentResponse::create);
    }

    private static ApartmentResponse create(Apartment apartment) {
        return ApartmentResponse.builder()
                .id(apartment.getId())
                .apartmentNumber(apartment.getApartmentNumber())
                .area(apartment.getArea())
                .numRooms(apartment.getNumRooms())
                .build();
    }
}