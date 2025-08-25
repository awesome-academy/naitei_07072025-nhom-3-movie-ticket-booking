package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RevenueDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus;
import com.naitei.group3.movie_ticket_booking_system.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class RevenueController extends BaseAdminController{

    private final RevenueService revenueService;

    @GetMapping("/admin/revenues")
    public String viewRevenue(Model model) {

        String[] types = {"DAY", "MONTH", "YEAR"};
        String[] prefixes = {"daily", "monthly", "yearly"};

        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            String prefixe = prefixes[i];

            List<RevenueDTO> revenueList = revenueService.getRevenueByType(type, PaymentStatus.SUCCESS);

            List<String> labels = revenueList.stream()
                    .map(RevenueDTO::getLabel)
                    .toList();

            List<BigDecimal> data = revenueList.stream()
                    .map(RevenueDTO::getRevenue)
                    .toList();

            model.addAttribute(prefixe + "Labels", labels);
            model.addAttribute(prefixe + "Data", data);
        }

        BigDecimal totalRevenue = revenueService.getTotalRevenue(PaymentStatus.SUCCESS);
        long successTransactions = revenueService.getPaymentCountByStatus(PaymentStatus.SUCCESS);
        long pendingTransactions = revenueService.getPaymentCountByStatus(PaymentStatus.PENDING);
        long failedTransactions = revenueService.getPaymentCountByStatus(PaymentStatus.FAILED);

        model.addAttribute("successTransactions", successTransactions);
        model.addAttribute("pendingTransactions", pendingTransactions);
        model.addAttribute("failedTransactions", failedTransactions);
        model.addAttribute("totalRevenue", totalRevenue);

        return getAdminView("revenues/index");
    }
}
