package com.naitei.group3.movie_ticket_booking_system.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocationController {

    @GetMapping("/location")
    public String getLocationPage() {
        return "location/getLocation"; 
    }
}
