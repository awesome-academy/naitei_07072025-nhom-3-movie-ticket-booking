package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.dto.response.CinemaRecommendationDTO;

import java.util.List;

public interface CinemaRecommendationService {

    /**
     * Tìm danh sách rạp gần vị trí người dùng, kèm danh sách phim và đánh giá.
     *
     * @param userLat Vĩ độ người dùng
     * @param userLon Kinh độ người dùng
     * @param radiusKm Bán kính tìm kiếm (km)
     * @return Danh sách rạp với thông tin phim
     */
    List<CinemaRecommendationDTO> findNearbyCinemasWithMovies(double userLat, double userLon, double radiusKm);
}
