package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.request.SeatEditRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SeatDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Hall;
import com.naitei.group3.movie_ticket_booking_system.entity.Seat;
import com.naitei.group3.movie_ticket_booking_system.entity.SeatType;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.repository.HallRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.SeatRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.SeatTypeRepository;
import com.naitei.group3.movie_ticket_booking_system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final HallRepository hallRepository;
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
    public String addSeatsToHall(Long hallId, String row, int quantity, Long seatTypeId, Locale locale) {

        // Hàng nhập vào phải in hoa
        row = row.toUpperCase();

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("hall.notfound", null, locale)));

        SeatType seatType = seatTypeRepository.findById(seatTypeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("seattype.notfound", null, locale)));

        // Lấy ra danh sách cột hiện có trong row
        List<String> existingColumns = seatRepository.findColumnsByHallIdAndRow(hallId, row);
        int maxColumn = 0;
        for (String col : existingColumns) {
            try {
                int num = Integer.parseInt(col.substring(1));   // Tìm số ghế lớn nhất trong hàng (ví dụ A5 -> 5)
                if (num > maxColumn) maxColumn = num;
            } catch (NumberFormatException ignored) {}
        }

        // số ghế add thêm ko thể vượt quá max seat của mỗi hàng
        int maxSeatsPerRow = hall.getMaxSeatsPerRow();
        if (maxColumn + quantity > maxSeatsPerRow) {
            quantity = maxSeatsPerRow - maxColumn;
        }

        List<Seat> newSeats = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            int colNumber = maxColumn + i;
            Seat seat = new Seat();
            seat.setHall(hall);
            seat.setSeatRow(row);
            seat.setSeatColumn(row + colNumber);
            seat.setSeatType(seatType);
            newSeats.add(seat);
        }
        seatRepository.saveAll(newSeats);

        return messageSource.getMessage("seat.add.success", new Object[]{newSeats.size()}, locale);
    }
}
