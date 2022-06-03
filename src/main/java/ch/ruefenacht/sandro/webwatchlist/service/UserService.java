package ch.ruefenacht.sandro.webwatchlist.service;

import ch.ruefenacht.sandro.webwatchlist.dto.*;
import ch.ruefenacht.sandro.webwatchlist.model.Media;
import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.model.Series;
import ch.ruefenacht.sandro.webwatchlist.model.UserData;
import ch.ruefenacht.sandro.webwatchlist.repository.MovieRepository;
import ch.ruefenacht.sandro.webwatchlist.repository.SeriesRepository;
import ch.ruefenacht.sandro.webwatchlist.repository.UserDataRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final UserDataRepository userDataRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDataRepository userDataRepository, MovieRepository movieRepository, SeriesRepository seriesRepository) {
        this.userDataRepository = userDataRepository;
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
    }

    public List<UserShowDto> getAll() {
        List<UserData> users = userDataRepository.findAll();
        List<UserShowDto> userShowDtos = new ArrayList<>(List.of());

        for(UserData user : users) {
            userShowDtos.add(new UserShowDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
        }

        return userShowDtos;
    }

    public UserShowDto save(UserData user) {
        String hash = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        this.userDataRepository.save(user);
        return new UserShowDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto());
    }

    public Optional<UserShowDto> findById(UUID uuid) throws NoSuchElementException {
        Optional<UserData> userDataOptional = userDataRepository.findById(uuid);

        if(userDataOptional.isEmpty()) {
            return Optional.empty();
        }
        UserData user = userDataOptional.get();

        return Optional.of(new UserShowDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
    }

    public Optional<UserData> findByEmail(String email) {
        return userDataRepository.findByEmail(email);
    }

    public Optional<AuthShowDto> authenticate(String email, String password, HttpHeaders httpHeaders) throws JWTCreationException, IllegalArgumentException {
        Optional<UserData> userDataOptional = this.findByEmail(email);

        if(userDataOptional.isEmpty()) {
            return Optional.empty();
        }

        if(!this.passwordEncoder.matches(password, userDataOptional.get().getPassword())) {
            return Optional.empty();
        }

        long issuedAt = Instant.now().getEpochSecond();
        long expiration = issuedAt + 60 * 60 * 2;

        Algorithm algorithm = Algorithm.HMAC256("SECRET");
        String accessToken = JWT.create().withIssuer("web-watchlist").withPayload(Map.of("iat", issuedAt, "exp", expiration, "id", userDataOptional.get().getId().toString(), "email", userDataOptional.get().getEmail())).sign(algorithm);

        long expirationRefresh = issuedAt + 60 * 60 * 4;
        String refreshToken = JWT.create().withIssuer("web-watchlist").withPayload(Map.of("iat", issuedAt, "exp", expirationRefresh, "id", userDataOptional.get().getId().toString())).sign(algorithm);

        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", refreshToken)
                .maxAge(expirationRefresh)
                .httpOnly(true)
                .path("/")
                .build();
        httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return Optional.of(new AuthShowDto(accessToken, refreshToken));
    }

    public void logout(HttpHeaders httpHeaders) {
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "").maxAge(0).httpOnly(true).path("/").build();
        httpHeaders.add(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public Optional<UserShowDto> addToWatchlist(UUID uuid, UUID mediaId) {
        Optional<UserData> userDataOptional = this.userDataRepository.findById(uuid);

        Media media;
        Optional<Movie> movieOptional = this.movieRepository.findById(mediaId);
        if(movieOptional.isEmpty()) {
            Optional<Series> seriesOptional = this.seriesRepository.findById(mediaId);
            if(seriesOptional.isPresent()) {
                media = seriesOptional.get();
            }else{
                return Optional.empty();
            }
        }else{
            media = movieOptional.get();
        }

        if(userDataOptional.isPresent()) {
            UserData user = userDataOptional.get();
            user.getWatchlist().add(media);
            userDataRepository.save(user);

            return Optional.of(new UserShowDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
        }
        return Optional.empty();
    }

    public Optional<UserShowDto> addToFavorites(UUID uuid, UUID mediaId) {
        Optional<UserData> userDataOptional = this.userDataRepository.findById(uuid);

        Media media;
        Optional<Movie> movieOptional = this.movieRepository.findById(mediaId);
        if(movieOptional.isEmpty()) {
            Optional<Series> seriesOptional = this.seriesRepository.findById(mediaId);

            if(seriesOptional.isPresent()) {
                media = seriesOptional.get();
            }else{
                return Optional.empty();
            }
        }else{
            media = movieOptional.get();
        }

        if(userDataOptional.isPresent()) {
            UserData user = userDataOptional.get();
            user.getFavorites().add(media);
            userDataRepository.save(user);

            return Optional.of(new UserShowDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
        }
        return Optional.empty();
    }

    public List<MovieShowDto> getAllFavorites(UserShowDto user) {
        List<MovieShowDto> movieShowDtos = new ArrayList<>();
        for(MediaShowDto movieShowDto : user.getFavorites()) {
            Optional<Movie> movieOptional = this.movieRepository.findById(movieShowDto.getUuid());
            if(movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                movieShowDtos.add(new MovieShowDto(movie.getId(), movie.getName(), movie.getImageUrl()));
            }
        }
        return movieShowDtos;
    }
}
