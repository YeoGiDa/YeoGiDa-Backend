package com.Udemy.YeoGiDa.global.scheduler;

import com.Udemy.YeoGiDa.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final TripService tripService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void initTripChangeHeartCount() {
        tripService.initTripChangeHeartCount();
    }

//    @Scheduled(cron = "0 0 0 * * *")
//    public void resetRank(){
//        tripService.resetRank();
//    }

}
