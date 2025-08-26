package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RevenueDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

public interface RevenueService {

    BigDecimal getTotalRevenue(PaymentStatus status);

    long getPaymentCountByStatus(PaymentStatus status);

    List<RevenueDTO> getRevenueByType(String type, PaymentStatus status);
}
