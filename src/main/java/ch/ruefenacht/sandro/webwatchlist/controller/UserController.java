package ch.ruefenacht.sandro.webwatchlist.controller;

import ch.ruefenacht.sandro.webwatchlist.dto.*;
import ch.ruefenacht.sandro.webwatchlist.model.Media;
import ch.ruefenacht.sandro.webwatchlist.model.UserData;
import ch.ruefenacht.sandro.webwatchlist.service.MediaService;
import ch.ruefenacht.sandro.webwatchlist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<UserData> user = this.userService.findById(id);

        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UserShowDto(user.get().getId(), user.get().getUsername(), user.get().getFirstname(), user.get().getLastname(), user.get().getEmail(), user.get().getFavoritesAsDto(), user.get().getWatchlistAsDto()));
    }

    @GetMapping
    public ResponseEntity<List<UserShowDto>> getAll() {
        List<UserShowDto> userDtos = this.userService.getAll().stream()
                .map(user -> new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    @PostMapping
    public ResponseEntity<UserShowDto> createUser(@RequestBody UserCreateDto createDto) {
        UserData user = new UserData();

        user.setUsername(createDto.getUsername());
        user.setFirstname(createDto.getFirstname());
        user.setLastname(createDto.getLastname());
        user.setEmail(createDto.getEmail());

        this.userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getFavoritesAsDto(), user.getWatchlistAsDto()));
    }

    @PostMapping("/{userId}/watchlist")
    public ResponseEntity<UserShowDto> addEntryToWatchlist(@PathVariable UUID userId, @RequestBody WatchlistEntryCreateDto watchlistEntryCreateDto) {
        UUID id = watchlistEntryCreateDto.getId();

        Optional<? extends Media> media = this.mediaService.findMediaById(id);

        if(media.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<UserData> user = this.userService.findById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        this.userService.addToWatchlist(user.get(), media.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserShowDto(user.get().getId(), user.get().getUsername(), user.get().getFirstname(), user.get().getLastname(), user.get().getEmail(), user.get().getFavoritesAsDto(), user.get().getWatchlistAsDto()));
    }

    @PostMapping("/{userId}/favorites")
    public ResponseEntity<UserShowDto> addEntryToFavorites(@PathVariable UUID userId, @RequestBody FavoritesEntryCreateDto favoritesEntryCreateDto) {
        UUID id = favoritesEntryCreateDto.getId();

        Optional<? extends Media> media = this.mediaService.findMediaById(id);

        if(media.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<UserData> user = this.userService.findById(userId);

        if(user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        this.userService.addToFavorites(user.get(), media.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserShowDto(user.get().getId(), user.get().getUsername(), user.get().getFirstname(), user.get().getLastname(), user.get().getEmail(), user.get().getFavoritesAsDto(), user.get().getWatchlistAsDto()));
    }
}
