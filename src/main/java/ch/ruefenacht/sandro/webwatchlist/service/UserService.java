package ch.ruefenacht.sandro.webwatchlist.service;

import ch.ruefenacht.sandro.webwatchlist.dto.UserShowDto;
import ch.ruefenacht.sandro.webwatchlist.model.Media;
import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.model.Series;
import ch.ruefenacht.sandro.webwatchlist.model.UserData;
import ch.ruefenacht.sandro.webwatchlist.repository.MovieRepository;
import ch.ruefenacht.sandro.webwatchlist.repository.SeriesRepository;
import ch.ruefenacht.sandro.webwatchlist.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserDataRepository userDataRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

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
            userShowDtos.add(new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
        }

        return userShowDtos;
    }

    public UserShowDto save(UserData user) {
        this.userDataRepository.save(user);
        return new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto());
    }

    public Optional<UserShowDto> findById(UUID uuid) throws NoSuchElementException {
        Optional<UserData> userDataOptional = userDataRepository.findById(uuid);

        if(userDataOptional.isEmpty()) {
            return Optional.empty();
        }
        UserData user = userDataOptional.get();

        return Optional.of(new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
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

            return Optional.of(new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
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

            return Optional.of(new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
        }
        return Optional.empty();
    }
}
