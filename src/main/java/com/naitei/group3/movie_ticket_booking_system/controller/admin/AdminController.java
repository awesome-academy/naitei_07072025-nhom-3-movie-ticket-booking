package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("ticketsToday", 0);
        return "admin/dashboard";
    }
}
