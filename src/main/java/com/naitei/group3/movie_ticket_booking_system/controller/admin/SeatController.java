package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.request.AddSeatRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.request.SeatEditRequest;
import com.naitei.group3.movie_ticket_booking_system.entity.Seat;
import com.naitei.group3.movie_ticket_booking_system.entity.SeatType;
import com.naitei.group3.movie_ticket_booking_system.service.SeatService;
import com.naitei.group3.movie_ticket_booking_system.service.SeatTypeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin/cinemas/{cinemaId}/halls/{hallId}/seats")
@RequiredArgsConstructor
public class SeatController extends BaseAdminController{

    private final SeatService seatService;
    private final SeatTypeService seatTypeService;
    private final MessageSource messageSource;

    // Form edit
    @GetMapping("/{seatId}/edit")
    public String showEditForm(@PathVariable Long cinemaId,
                               @PathVariable Long hallId,
                               @PathVariable Long seatId,
                               Model model) {

        Seat seat = seatService.getSeatById(seatId);
        List<SeatType> seatTypes = seatTypeService.getAllSeatTypes();

        model.addAttribute("seat", seat);
        model.addAttribute("seatTypes", seatTypes);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("hallId", hallId);

        return getAdminView("cinemas/seats/edit");
    }

    @PutMapping("/{seatId}")
    public String editSeat(@PathVariable Long cinemaId,
                           @PathVariable Long hallId,
                           @PathVariable Long seatId,
                           @ModelAttribute SeatEditRequest request,
                           Locale locale,
                           RedirectAttributes redirectAttributes) {

        seatService.editSeat(seatId, request);

        String successMessage = messageSource.getMessage("seat.update.success", null, locale);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/admin/cinemas/" + cinemaId + "/halls/" + hallId;
    }

    @GetMapping("/add")
    public String showAddForm(@PathVariable Long cinemaId,
                           @PathVariable Long hallId,
                           Model model) {
        List<SeatType> seatTypes = seatTypeService.getAllSeatTypes();

        model.addAttribute("seatTypes", seatTypes);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("hallId", hallId);
        model.addAttribute("addSeatRequest", new AddSeatRequest());
        return getAdminView("cinemas/seats/add");
    }

    @PostMapping("/add")
    public String addSeat(@PathVariable Long cinemaId,
                          @PathVariable Long hallId,
                          @ModelAttribute AddSeatRequest request,
                          RedirectAttributes redirectAttributes,
                          Locale locale) {
        String message = seatService.addSeatsToHall(hallId, request.getRow(), request.getQuantity(), request.getSeatTypeId(), locale);

        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/admin/cinemas/" + cinemaId + "/halls/" + hallId;
    }
}
