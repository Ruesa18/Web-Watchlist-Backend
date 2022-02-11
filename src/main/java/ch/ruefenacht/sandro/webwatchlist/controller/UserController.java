package ch.ruefenacht.sandro.webwatchlist.controller;

import ch.ruefenacht.sandro.webwatchlist.dto.UserCreateDto;
import ch.ruefenacht.sandro.webwatchlist.dto.UserShowDto;
import ch.ruefenacht.sandro.webwatchlist.model.UserData;
import ch.ruefenacht.sandro.webwatchlist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<UserData> user = this.userService.findById(id);

        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UserShowDto(user.get().getId(), user.get().getUsername(), user.get().getFirstname(), user.get().getLastname(), user.get().getEmail()));
    }

    @GetMapping
    public ResponseEntity<List<UserShowDto>> getAll() {
        List<UserShowDto> userDtos = this.userService.getAll().stream()
                .map(user -> new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail()))
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

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserShowDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail()));
    }
}
