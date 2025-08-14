package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.response.MovieDTO;
import com.naitei.group3.movie_ticket_booking_system.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MovieDetailController extends BaseAdminController{

    private final MovieService movieService;

    @GetMapping("/admin/movies/{id}")
    public String getMovieDetail(@PathVariable Long id, Model model) {
        MovieDTO movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);

        return getAdminView("movies/show");
    }

}
