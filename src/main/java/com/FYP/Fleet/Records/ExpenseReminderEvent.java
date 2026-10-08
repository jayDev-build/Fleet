package com.FYP.Fleet.Records;

public record ExpenseReminderEvent(
        String source,
        String destination,
        long tripId,
        String userPhoneNumber
) {
}
