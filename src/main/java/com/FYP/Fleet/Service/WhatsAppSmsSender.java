package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniTripResponseDto;
import com.FYP.Fleet.Dto.Response.TransactionResponseDto;
import com.FYP.Fleet.Dto.Response.TripResponseDto;
import com.FYP.Fleet.Models.SecurityUser;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WhatsAppSmsSender {

    @Value("${twilio.ACCOUNT_SID}")
    public String ACCOUNT_SID;
    @Value("${twilio.AUTH_TOKEN}")
    public String AUTH_TOKEN ;
    @Value("${twilio.WHATSAPP_FROM}") // Your sandbox or live business number (e.g., "whatsapp:+14155238886")
    private String whatsappFrom;

    public void tripCreatedConfirmation(TripResponseDto createdTrip, String toPhoneNumber) {
        String variables = String.format(
                "{\"1\":\"%s\",\"2\":\"%s\",\"3\":\"%s\",\"4\":\"%s\",\"5\":\"%s\",\"6\":\"%s\"}",
                createdTrip.getSource(),
                createdTrip.getDestination(),
                createdTrip.getVehicleNumber(),
                createdTrip.getDriverName(),
                createdTrip.getFreightPrice(),
                createdTrip.getId()
        );

        sendMessage(toPhoneNumber, "HXe03463259b6926216df93d2675c8e443", variables);
    }

    public void closeTrip(MiniTripResponseDto tripResponseDto){
        String variables = String.format(
                "{\"1\":\"%s\",\"2\":\"%s\",\"3\":\"%s\",\"4\":\"%s\",\"5\":\"%s\",\"6\":\"%s\",\"7\":\"%s\"}",
                tripResponseDto.getSource(),
                tripResponseDto.getDestination(),
                tripResponseDto.getVehicleNumber(),
                tripResponseDto.getFreightPrice(),
                tripResponseDto.getOwnerRate(),
                tripResponseDto.getTotalExpense(),
                tripResponseDto.getProfit()
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        sendMessage(securityUser.phoneNumber(),"HXe56d7af1dc61471bfe4bb4c9c57830ec", variables);
    }

    public void addTransaction(TransactionResponseDto responseDto, Long ownerBalance, String toPhone){
        // 1. Map your DTO fields and variables to the numbered placeholders
        String variables = String.format(
                "{\"1\":\"%s\",\"2\":\"%s\",\"3\":\"%s\",\"4\":\"%s\",\"5\":\"%s\",\"6\":\"%s\"}",
                responseDto.getOwnerName(),
                responseDto.getAmount(),
                responseDto.getMethod(),
                responseDto.getNote(),
                responseDto.getOwnerName(), // Re-passing name for placeholder {{5}}
                ownerBalance
        );

        sendMessage(toPhone, "HXab010234c620051167503ba07c89642f", variables);
    }

    public void expenseLogRemind(String source, String destination, long id, String userPhoneNumber){
        String variables = String.format(
                "{\"1\":\"%s\",\"2\":\"%s\",\"3\":\"%s\"}",
                source,
                destination,
                id
        );
        sendMessage(userPhoneNumber, "HX07d9892323faa730276ecde18f4afbd6", variables);
    }

    private void sendMessage(String userPhoneNumber, String templateSid, String jsonVariables ){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try {
            Message message = Message.creator(
                            new PhoneNumber("whatsapp:+91" + userPhoneNumber),
                            new PhoneNumber("whatsapp:"+whatsappFrom),
                            "" // Body must be empty when passing contentSid
                    )
                    .setContentSid(templateSid)
                    .setContentVariables(jsonVariables) // Must be a JSON string format: {"1":"val1", "2":"val2"}
                    .create();

            log.info("Notification sent successfully! SID: {}", message.getSid());
        } catch (Exception e) {
            log.info("Failed to send WhatsApp alert: {}", e.getMessage());
        }

    }
}
