package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.request.MovieSearchApiRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.MovieDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Genre;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.enums.ShowtimeStatus;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import com.naitei.group3.movie_ticket_booking_system.repository.GenreRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.naitei.group3.movie_ticket_booking_system.repository.MovieRepository;
import com.naitei.group3.movie_ticket_booking_system.service.MovieService;
import com.naitei.group3.movie_ticket_booking_system.entity.Movie;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
//import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MessageSource messageSource;

    @Override
    public Page<MovieDTO> filterMovies(String keyword, Integer year, String genreName, Boolean isActive,
            Pageable pageable) {
        return movieRepository.filterMovies(keyword, year, genreName, isActive, pageable)
                .map(DtoConverter::convertMovieToDTO);
    }

    @Override
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
        return DtoConverter.convertMovieToDTO(movie);
    }

    @Override
    public Page<Movie> getNowShowingMovies(Pageable pageable) {
        return movieRepository.findMoviesByShowtimeStatus(ShowtimeStatus.AVAILABLE, pageable);
    }

    @Override
    @Transactional
    public MovieDTO updateMovie(Long id, MovieDTO dto) {
        Movie oldMovie = movieRepository.findById(id)
            .orElseThrow(() -> {
                String msg = messageSource.getMessage(
                    "movie.notfound.id",
                    new Object[]{id},
                    LocaleContextHolder.getLocale()
                );
                return new ResourceNotFoundException(msg);
            });

        // cập nhật genres
        Set<Genre> genres = dto.genres().stream()
            .map(name -> genreRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage(
                        "genre.notfound",
                        new Object[]{name},
                        LocaleContextHolder.getLocale()
                    );
                    return new ResourceNotFoundException(msg);
                })
            )
            .collect(Collectors.toSet());

        // dùng builder để tạo movie mới từ dto (giữ nguyên id cũ)
        Movie movie = Movie.builder()
            .id(oldMovie.getId()) // giữ nguyên id
            .name(dto.name())
            .description(dto.description())
            .duration(dto.duration())
            .poster(dto.poster())
            .releaseDate(dto.releaseDate())
            .isActive(dto.isActive())
            .genres(genres)
            .build();

        movieRepository.save(movie);

        return DtoConverter.convertMovieToDTO(movie);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage(
                    "movie.notfound",
                    new Object[]{movieId},
                    LocaleContextHolder.getLocale()
                )));

        // Nếu không có ràng buộc thì hard delete
        if (movie.getShowtimes().isEmpty() && movie.getRatings().isEmpty()) {
            movie.getGenres().clear(); // tránh lỗi ràng buộc ở bảng movie_genre
            movieRepository.delete(movie);
        } else {
            // Nếu có ràng buộc thì soft delete
            movie.setIsActive(false);
            movie.setDeletedAt(LocalDateTime.now());
            movieRepository.save(movie);
        }
    }

    public Page<MovieDTO> searchMovies(MovieSearchApiRequest req, Pageable pageable) {
        Page<Movie> movies = movieRepository.searchMovies(
                req.keyword(),
                req.genreName(),
                req.showDate(),
                pageable
        );
        return movies.map(DtoConverter::convertMovieToDTO);
    }
}
