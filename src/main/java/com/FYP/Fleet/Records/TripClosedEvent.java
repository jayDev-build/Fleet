package com.FYP.Fleet.Records;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniTripResponseDto;

public record TripClosedEvent(
        MiniTripResponseDto tripResponseDto
) {
}
