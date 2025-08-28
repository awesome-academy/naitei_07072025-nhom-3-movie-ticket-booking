package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.request.HallRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SeatDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Hall;
import com.naitei.group3.movie_ticket_booking_system.exception.BadRequestException;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.service.CinemaService;
import com.naitei.group3.movie_ticket_booking_system.service.HallService;
import com.naitei.group3.movie_ticket_booking_system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final MessageSource messageSource;

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

        Hall hall = hallService.getHallById(hallId);

        model.addAttribute("groupedSeats", groupedSeats);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("seatCount", seatCount);
        model.addAttribute("totalSeats", totalSeats);
        model.addAttribute("hall", hall);

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

    @GetMapping("/{cinemaId}/halls/{hallId}/edit")
    public String showEditHallForm(@PathVariable Long cinemaId,
                                   @PathVariable Long hallId,
                                   Model model) {
        Hall hall = hallService.getHallById(hallId);

        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("hall", hall);
        return getAdminView("cinemas/halls/edit");
    }

    @PutMapping("/{cinemaId}/halls/{hallId}")
    public String updateHallName(@PathVariable Long cinemaId,
                                 @PathVariable Long hallId,
                                 @RequestParam("newName") String newName,
                                 RedirectAttributes redirectAttributes) {
        try {
            hallService.updateHallName(hallId, newName);
            redirectAttributes.addFlashAttribute("successMessage", "hall.edit.success");
        } catch (BadRequestException | ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/cinemas/" + cinemaId + "/halls/" + hallId;
    }
}
