package com.FYP.Fleet.Records;

import com.FYP.Fleet.Dto.Response.TransactionResponseDto;

public record TransactionAddedEvent(
        TransactionResponseDto responseDto,
        Long ownerBalance,
        String toPhone
) {
}
