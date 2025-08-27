package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.request.SeatEditRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SeatDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Seat;
import com.naitei.group3.movie_ticket_booking_system.entity.SeatType;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.repository.SeatRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.SeatTypeRepository;
import com.naitei.group3.movie_ticket_booking_system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final MessageSource messageSource;

    @Override
    public Map<String, List<SeatDTO>> getSeatsByHallId(Long id) {
        List<Seat> seats = seatRepository.findByHallId(id);

        // Convert -> DTO
        List<SeatDTO> seatDTOs = seats.stream()
                .map(DtoConverter::convertSeatToDTO)
                .toList();

        // Group + sort
        return seatDTOs.stream()
                .collect(Collectors.groupingBy(         // group by row
                        SeatDTO::seatRow,
                        TreeMap::new,                   // sort row alphabet
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(
                                                Comparator.comparing(
                                                        SeatDTO::seatTypeName,              // sort by type
                                                        Comparator.comparingInt(type ->
                                                                List.of("Standard", "VIP", "Double").indexOf(type)
                                                        )
                                                ).thenComparingInt(s -> {           // sort by type
                                                    // Extract number from column string
                                                    String col = s.seatColumn(); // ví dụ: "A1"
                                                    return Integer.parseInt(col.replaceAll("\\D+", ""));        // remove non-digit
                                                })
                                        )
                                        .toList()
                        )
                ));
    }

    @Override
    public Seat getSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("seat.notfound", new Object[]{seatId}, LocaleContextHolder.getLocale())
                ));
    }

    @Override
    @Transactional
    public Seat editSeat(Long seatId, SeatEditRequest request) {

        // Seat hiện tại
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("seat.notfound", new Object[]{seatId}, LocaleContextHolder.getLocale())
                ));

        // Edit Seat Type
        if (request.getSeatTypeId() != null) {
            SeatType seatType = seatTypeRepository.findById(request.getSeatTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            messageSource.getMessage("seattype.notfound", new Object[]{request.getSeatTypeId()}, LocaleContextHolder.getLocale())
                    ));
            seat.setSeatType(seatType);
        }

        return seatRepository.save(seat);
    }

    @Override
    @Transactional
    public void deleteSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("seat.notfound", null, Locale.getDefault())
                ));

        // Nếu có booking vào seat thì soft delete
        if (seat.getBookingSeats() != null && !seat.getBookingSeats().isEmpty()) {
            // Đã có booking => xóa mềm
            seat.setDeletedAt(LocalDateTime.now());
            seatRepository.save(seat);
        } else {
            // Chưa từng được đặt => xóa cứng
            seatRepository.delete(seat);
        }
    }
}
