package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;
import com.naitei.group3.movie_ticket_booking_system.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/ratings")
@RequiredArgsConstructor
public class RatingController extends BaseAdminController{

    private final RatingService ratingService;

    @GetMapping
    public String listRatings(
            @RequestParam(value = "status", defaultValue = "PENDING") String status,     // pending default
            Model model) {

        // list ratings by status
        RatingStatus ratingStatus = RatingStatus.valueOf(status.toUpperCase());
        List<RatingDTO> ratings = ratingService.getRatingsByStatus(ratingStatus);

        model.addAttribute("ratings", ratings);
        model.addAttribute("status", status);

        return getAdminView("ratings/index");
    }
}
