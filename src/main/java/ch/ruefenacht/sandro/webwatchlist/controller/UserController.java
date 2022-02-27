package ch.ruefenacht.sandro.webwatchlist.controller;

import ch.ruefenacht.sandro.webwatchlist.dto.*;
import ch.ruefenacht.sandro.webwatchlist.model.UserData;
import ch.ruefenacht.sandro.webwatchlist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<UserShowDto> user = this.userService.findById(id);

        if(user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<UserShowDto>> getAll() {
        return ResponseEntity.ok(this.userService.getAll());
    }

    @PostMapping
    public ResponseEntity<UserShowDto> createUser(@RequestBody UserCreateDto createDto) {
        UserData user = new UserData();

        user.setUsername(createDto.getUsername());
        user.setFirstname(createDto.getFirstname());
        user.setLastname(createDto.getLastname());
        user.setEmail(createDto.getEmail());

        UserShowDto userShowDto = this.userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userShowDto);
    }

    @PostMapping("/{userId}/watchlist")
    public ResponseEntity<UserShowDto> addEntryToWatchlist(@PathVariable UUID userId, @RequestBody WatchlistEntryCreateDto watchlistEntryCreateDto) {
        UUID mediaId = watchlistEntryCreateDto.getId();

        Optional<UserShowDto> userShowDto = this.userService.addToWatchlist(userId, mediaId);

        if(userShowDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userShowDto.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{userId}/favorites")
    public ResponseEntity<UserShowDto> addEntryToFavorites(@PathVariable UUID userId, @RequestBody FavoritesEntryCreateDto favoritesEntryCreateDto) {
        UUID mediaId = favoritesEntryCreateDto.getId();

        Optional<UserShowDto> userShowDto = this.userService.addToFavorites(userId, mediaId);

        if(userShowDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userShowDto.get());
        }
        return ResponseEntity.badRequest().build();
    }
}
