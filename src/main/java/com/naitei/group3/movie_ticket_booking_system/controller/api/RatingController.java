package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.request.RatingRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingResponse;
import com.naitei.group3.movie_ticket_booking_system.security.CustomUserPrincipal;
import com.naitei.group3.movie_ticket_booking_system.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // Tạo hoặc cập nhật rating
    @PostMapping
    public BaseApiResponse<RatingResponse> createOrUpdateRating(
            @RequestBody RatingRequest request,
            Authentication authentication) {

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        request.setUserId(userId); // Set userId từ SecurityContext

        RatingResponse response = ratingService.createOrUpdateRating(request);
        return new BaseApiResponse<>(
                HttpStatus.OK.value(),
                "Rating created/updated successfully",
                response
        );
    }

    // Lấy rating theo movie
    @GetMapping("/movie/{movieId}")
    public BaseApiResponse<List<RatingResponse>> getRatingsByMovie(@PathVariable Long movieId) {
        List<RatingResponse> responses = ratingService.getRatingsByMovie(movieId);
        return new BaseApiResponse<>(
                HttpStatus.OK.value(),
                "Get ratings by movie successfully",
                responses
        );
    }

    // Lấy rating của chính user (dựa trên SecurityContext)
    @GetMapping("/me")
    public BaseApiResponse<List<RatingResponse>> getMyRatings(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        List<RatingResponse> responses = ratingService.getRatingsByUser(userId);
        return new BaseApiResponse<>(
                HttpStatus.OK.value(),
                "Get my ratings successfully",
                responses
        );
    }
}
