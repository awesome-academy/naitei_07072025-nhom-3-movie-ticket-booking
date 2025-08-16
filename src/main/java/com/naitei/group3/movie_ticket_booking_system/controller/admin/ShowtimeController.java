package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.request.ShowtimeFilterReq;
import com.naitei.group3.movie_ticket_booking_system.dto.response.ShowtimeDTO;
import com.naitei.group3.movie_ticket_booking_system.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class ShowtimeController extends BaseAdminController {

    private final ShowtimeService service;

    @GetMapping("/admin/showtimes")
    public String showTimes(
            @ModelAttribute ShowtimeFilterReq filter,
            Model model) {

        PageRequest pageable = PageRequest.of(filter.getPage(), filter.getSize());

        Page<ShowtimeDTO> showtimes = service.filterShowtime(
                filter.getKeyword(),
                filter.getCinemaName(),
                filter.getStatus(),
                filter.getShowDate(),
                pageable);

        model.addAttribute("showtimes", showtimes);
        model.addAttribute("currentPage", filter.getPage());
        model.addAttribute("totalPages", showtimes.getTotalPages());
        model.addAttribute("keyword", filter.getKeyword());
        model.addAttribute("cinemaName", filter.getCinemaName());
        model.addAttribute("status", filter.getStatus());
        model.addAttribute("showDate", filter.getShowDate());

        return getAdminView("showtimes/index");
    }
}
