package com.naitei.group3.movie_ticket_booking_system.repository.projection;

import java.math.BigDecimal;

public interface RevenueProjection {

    String getLabel();
    BigDecimal getRevenue();
}
