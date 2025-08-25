package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RevenueDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.PaymentTransaction;
import com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus;
import com.naitei.group3.movie_ticket_booking_system.repository.projection.RevenueProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    // total revenue
    @Query("SELECT SUM(p.amount) FROM PaymentTransaction p WHERE p.status = :status")
    BigDecimal getTotalRevenue(@Param("status") PaymentStatus status);

    //
    long countByStatus(PaymentStatus status);

    // filter by day or month or year
    @Query("""
    SELECT 
        CASE
            WHEN :type = 'DAY' THEN FUNCTION('DATE', pt.createdAt)
            WHEN :type = 'MONTH' THEN CONCAT(FUNCTION('YEAR', pt.createdAt), '-', 
                                             CASE WHEN FUNCTION('MONTH', pt.createdAt) < 10 THEN CONCAT('0', FUNCTION('MONTH', pt.createdAt))
                                                  ELSE FUNCTION('MONTH', pt.createdAt) END)
            WHEN :type = 'YEAR' THEN FUNCTION('YEAR', pt.createdAt)
        END AS label,
        SUM(pt.amount) AS revenue
    FROM PaymentTransaction pt
    WHERE pt.status = :status
    GROUP BY 
        CASE
            WHEN :type = 'DAY' THEN FUNCTION('DATE', pt.createdAt)
            WHEN :type = 'MONTH' THEN CONCAT(FUNCTION('YEAR', pt.createdAt), '-', 
                                             CASE WHEN FUNCTION('MONTH', pt.createdAt) < 10 THEN CONCAT('0', FUNCTION('MONTH', pt.createdAt))
                                                  ELSE FUNCTION('MONTH', pt.createdAt) END)
            WHEN :type = 'YEAR' THEN FUNCTION('YEAR', pt.createdAt)
        END
    ORDER BY label
    """)
    List<RevenueProjection> getRevenue(@Param("type") String type, @Param("status") PaymentStatus status);
}
