package com.naitei.group3.movie_ticket_booking_system.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieFilterReq {

    private String keyword;
    private Integer year;
    private String genreName;
    private Boolean isActive;

    @PositiveOrZero
    private int page = 0;

    @Min(1) @Max(100)
    private int size = 12;

    public void setKeyword(String keyword) {
        this.keyword = emptyToNull(keyword);
    }

    public void setGenreName(String genreName) {
        this.genreName = emptyToNull(genreName);
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public void setPage(int page) {
        this.page = Math.max(0, page);
    }

    public void setSize(int size) {
        if (size < 1) size = 10;
        if (size > 100) size = 100;
        this.size = size;
    }

    private String emptyToNull(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}
