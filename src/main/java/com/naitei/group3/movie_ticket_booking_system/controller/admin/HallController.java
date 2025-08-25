package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.request.HallRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SeatDTO;
import com.naitei.group3.movie_ticket_booking_system.service.CinemaService;
import com.naitei.group3.movie_ticket_booking_system.service.HallService;
import com.naitei.group3.movie_ticket_booking_system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/cinemas")
@RequiredArgsConstructor
public class HallController extends BaseAdminController {

    private final SeatService seatService;
    private final HallService hallService;
    private final CinemaService cinemaService;

    @GetMapping("/{cinemaId}/halls/{hallId}")
    public String getHallDetails(
            @PathVariable Long cinemaId,
            @PathVariable Long hallId,
            Model model
    ) {
        // seats (group + sort by column and row)
        Map<String, List<SeatDTO>> groupedSeats = seatService.getSeatsByHallId(hallId);

        // Count seats by type
        Map<String, Long> seatCount = groupedSeats.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(
                        SeatDTO::seatTypeName,
                        Collectors.counting()
                ));

        // Total seats
        long totalSeats = seatCount.values().stream().mapToLong(Long::longValue).sum();

        model.addAttribute("groupedSeats", groupedSeats);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("seatCount", seatCount);
        model.addAttribute("totalSeats", totalSeats);

        return getAdminView("cinemas/halls/index");
    }

    @GetMapping("/{cinemaId}/halls/add")
    public String showAddForm(@PathVariable Long cinemaId, Model model) {
        model.addAttribute("cinema", cinemaService.getCinemaById(cinemaId));
        model.addAttribute("hallRequest", new HallRequestDTO());
        return getAdminView("cinemas/halls/add");
    }

    @PostMapping("/{cinemaId}/halls/add")
    public String addHall(
        @PathVariable Long cinemaId,
        @ModelAttribute("hallRequest") HallRequestDTO hallRequest
    ) {
        hallRequest.setCinemaId(cinemaId);
        hallService.addHallWithSeats(hallRequest);

        return "redirect:/admin/cinemas/" + cinemaId;
    }
}
