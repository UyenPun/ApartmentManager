package com.company.presentation.rest.apartment.response;
//Cau 1

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ApartmentCountResponse {
    private long count;

    public static ApartmentCountResponse create(long count) {
        return ApartmentCountResponse.builder().count(count).build();
    }
}
