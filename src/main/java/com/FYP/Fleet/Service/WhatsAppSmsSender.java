package com.FYP.Fleet.Service;

import com.FYP.Fleet.Dto.MiniResponseDto.MiniTripResponseDto;
import com.FYP.Fleet.Dto.Response.OwnerBalanceDto;
import com.FYP.Fleet.Dto.Response.TransactionResponseDto;
import com.FYP.Fleet.Dto.Response.TripResponseDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppSmsSender {

    @Value("${twilio.ACCOUNT_SID}")
    public String ACCOUNT_SID;
    @Value("${twilio.AUTH_TOKEN}")
    public String AUTH_TOKEN ;

//    @Autowired
//    private OwnerService ownerService;

    public void tripCreatedConfirmation(TripResponseDto createdTrip) {
        String mssg = "Trip Created:" +
                "\nRoute: " + createdTrip.getSource() +  "→" +  createdTrip.getDestination() +
                "\nTruck: " + createdTrip.getVehicleNumber() +
                "\nDriver: "  + createdTrip.getDriverName() +
                "\nFreight: " + "₹" + createdTrip.getFreightPrice() +
                "\nFleetOS #TRP: " + createdTrip.getId();
        sendMessage(mssg);
    }

    public void closeTrip(MiniTripResponseDto tripResponseDto){
        String mssg = "Trip Khatam:\n" +
        tripResponseDto.getSource() + "→" + tripResponseDto.getDestination() +
        "\nTruck: " + tripResponseDto.getVehicleNumber() +
        "\nMaal ka Kiraya: " +  "₹" + tripResponseDto.getFreightPrice() +
        "\nTruck Bhada: " + "-₹" + tripResponseDto.getOwnerRate() +
        "\nKul Kharcha: " + "-₹" + tripResponseDto.getTotalExpense() +
        "\nNet Faida: " + "₹" + tripResponseDto.getProfit() +
       " Owner settlement update\n" +
        "karna mat bhuliyo";

        sendMessage(mssg);
    }

    public void addTransaction(TransactionResponseDto responseDto, Long userId, Long ownerBalance){
        String mssg = "✅ Payment Record Hua" +
        "\nOwner: " + responseDto.getOwnerName() +
        "\nAmount: ₹" + responseDto.getAmount()+
        "\nMethod: " + responseDto.getMethod() +
        "\nNote: " + responseDto.getNote() +
        "\n " + responseDto.getOwnerName() +  " ka updated balance: " + "₹" + ownerBalance;
        sendMessage(mssg);
    }

    private void sendMessage(String mssg){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber("whatsapp:+917011562650"),                      //to
                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),       //from
                        mssg)
                .create();
        System.out.println(message.getSid());

    }
}
