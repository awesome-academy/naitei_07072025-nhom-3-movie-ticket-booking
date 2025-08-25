package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RevenueDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus;
import com.naitei.group3.movie_ticket_booking_system.repository.PaymentTransactionRepository;
import com.naitei.group3.movie_ticket_booking_system.service.RevenueService;
import com.naitei.group3.movie_ticket_booking_system.utils.RevenueLabelFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Override
    public BigDecimal getTotalRevenue(PaymentStatus status) {
        return paymentTransactionRepository.getTotalRevenue(status);
    }

    @Override
    public long getPaymentCountByStatus(PaymentStatus status) {
        return paymentTransactionRepository.countByStatus(status);
    }

    @Override
    public List<RevenueDTO> getRevenueByType(String type, PaymentStatus status) {

        return paymentTransactionRepository.getRevenue(type, status).stream()
                .map(projection -> {
                    String formattedLabel = RevenueLabelFormatter.format(type, projection.getLabel());
                    return new RevenueDTO(formattedLabel, projection.getRevenue());
                })
                .toList();
    }
}
