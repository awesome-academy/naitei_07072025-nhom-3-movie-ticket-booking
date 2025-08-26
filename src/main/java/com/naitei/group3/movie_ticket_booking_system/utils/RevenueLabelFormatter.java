package com.naitei.group3.movie_ticket_booking_system.utils;

public class RevenueLabelFormatter {

    public static String format(String type, String rawLabel) {
        if ("MONTH".equalsIgnoreCase(type)) {
            String[] parts = rawLabel.split("-");
            return "Tháng " + parts[1] + "/" + parts[0];
        } else if ("YEAR".equalsIgnoreCase(type)) {
            return "Năm " + rawLabel;
        }
        return rawLabel;
    }
}
