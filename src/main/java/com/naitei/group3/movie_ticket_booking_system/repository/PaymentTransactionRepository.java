package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.entity.PaymentTransaction;
import com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    // total revenue
    @Query("SELECT SUM(p.amount) FROM PaymentTransaction p WHERE p.status = com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus.SUCCESS")
    BigDecimal getTotalRevenue();

    //
    long countByStatus(PaymentStatus status);

    // filter by day or month or year
    @Query("""
    SELECT 
        CASE 
            WHEN :type = 'DAY' THEN FUNCTION('DATE', pt.createdAt)
            WHEN :type = 'MONTH' THEN CONCAT(                                      
                FUNCTION('YEAR', pt.createdAt), '-', 
                FUNCTION('LPAD', CONCAT('', FUNCTION('MONTH', pt.createdAt)), 2, '0')
            )
            WHEN :type = 'YEAR' THEN FUNCTION('YEAR', pt.createdAt)
        END,
        SUM(pt.amount)                                      
    FROM PaymentTransaction pt
    WHERE pt.status = com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus.SUCCESS        
    GROUP BY 
        CASE 
            WHEN :type = 'DAY' THEN FUNCTION('DATE', pt.createdAt)
            WHEN :type = 'MONTH' THEN CONCAT(
                FUNCTION('YEAR', pt.createdAt), '-', 
                FUNCTION('LPAD', CONCAT('', FUNCTION('MONTH', pt.createdAt)), 2, '0')
            )
            WHEN :type = 'YEAR' THEN FUNCTION('YEAR', pt.createdAt)
        END
    ORDER BY 1
    """)
    List<Object[]> getRevenue(@Param("type") String type);
}

