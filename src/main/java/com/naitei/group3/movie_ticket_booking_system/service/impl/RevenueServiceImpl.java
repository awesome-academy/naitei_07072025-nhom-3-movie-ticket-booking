package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RevenueDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus;
import com.naitei.group3.movie_ticket_booking_system.repository.PaymentTransactionRepository;
import com.naitei.group3.movie_ticket_booking_system.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    @Override
    public BigDecimal getTotalRevenue() {
        return paymentTransactionRepository.getTotalRevenue();
    }

    @Override
    public long getPaymentCountByStatus(PaymentStatus status) {
        return paymentTransactionRepository.countByStatus(status);
    }

    @Override
    public List<RevenueDTO>getRevenueByType(String type) {
        return paymentTransactionRepository.getRevenue(type).stream()
                .map(row -> {
                    String label = row[0].toString();
                    BigDecimal amount = (BigDecimal) row[1];

                    if ("MONTH".equalsIgnoreCase(type)) {

                        String[] parts = label.split("-");
                        String year = parts[0];
                        String month = parts[1];

                        label = "Tháng " + month + "/" + year;
                    } else if ("YEAR".equalsIgnoreCase(type)) {
                        label = "Năm " + label;
                    }

                    return new RevenueDTO(label, amount);
                })
                .toList();
    }
}
