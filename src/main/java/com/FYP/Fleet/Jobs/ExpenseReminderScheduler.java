package com.FYP.Fleet.Jobs;

import com.FYP.Fleet.Models.Trip;
import com.FYP.Fleet.Service.TripService;
import com.FYP.Fleet.Service.WhatsAppSmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ExpenseReminderScheduler {

    private final TripService tripService;
    private final WhatsAppSmsSender whatsAppSmsSenderService;

    @Autowired
    public ExpenseReminderScheduler(TripService tripService, WhatsAppSmsSender whatsAppSmsSenderService){
        this.tripService = tripService;
        this.whatsAppSmsSenderService = whatsAppSmsSenderService;
    }

    @Scheduled(cron = "0 0 9-21 * * ?")
    public void tripsWhereLastExpenseIsOlderThan24Hours(){
        List<Trip> trips = tripService.findTripsWhereLastExpenseIsOlderThan24Hours();
        for(Trip t : trips){
            whatsAppSmsSenderService.expenseLogRemind(
                    t.getSource(),
                    t.getDestination(),
                    t.getId(),
                    t.getUser().getPhone()
                    );
        }
    }
}
