package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;
import com.naitei.group3.movie_ticket_booking_system.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("adminRatingController")
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

        // ds status tránh harecode
        RatingStatus[] allStatuses = RatingStatus.values();

        model.addAttribute("ratings", ratings);
        model.addAttribute("status", status);
        model.addAttribute("allStatuses", allStatuses);

        return getAdminView("ratings/index");
    }

    @PostMapping("/approve/{userId}/{movieId}")
    public String approve(@PathVariable Long userId, @PathVariable Long movieId) {
        ratingService.updateStatus(userId, movieId, RatingStatus.APPROVED);
        return "redirect:/admin/ratings?status=PENDING";
    }

    @PostMapping("/reject/{userId}/{movieId}")
    public String reject(@PathVariable Long userId, @PathVariable Long movieId) {
        ratingService.updateStatus(userId, movieId, RatingStatus.REJECTED);
        return "redirect:/admin/ratings?status=PENDING";
    }
}
