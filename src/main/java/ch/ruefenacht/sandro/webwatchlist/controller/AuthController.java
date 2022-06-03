package ch.ruefenacht.sandro.webwatchlist.controller;

import ch.ruefenacht.sandro.webwatchlist.dto.AuthRequestDto;
import ch.ruefenacht.sandro.webwatchlist.dto.AuthShowDto;
import ch.ruefenacht.sandro.webwatchlist.service.UserService;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<AuthShowDto> authenticate(@RequestBody AuthRequestDto authRequestDto) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            Optional<AuthShowDto> authShowDtoOptional = userService.authenticate(authRequestDto.getEmail(), authRequestDto.getPassword(), httpHeaders);

            if(authShowDtoOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok().headers(httpHeaders).body(authShowDtoOptional.get());
        }catch (JWTCreationException | IllegalArgumentException exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        HttpHeaders httpHeaders = new HttpHeaders();
        userService.logout(httpHeaders);
        return ResponseEntity.ok().headers(httpHeaders).body("");
    }
}
