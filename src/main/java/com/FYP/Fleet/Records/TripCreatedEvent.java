package com.FYP.Fleet.Records;

import com.FYP.Fleet.Dto.Response.TripResponseDto;

public record TripCreatedEvent(
        TripResponseDto createdTrip,
        String toPhoneNumber
) {
}
