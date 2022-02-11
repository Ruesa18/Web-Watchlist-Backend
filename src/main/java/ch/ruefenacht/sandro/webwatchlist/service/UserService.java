package ch.ruefenacht.sandro.webwatchlist.service;

import ch.ruefenacht.sandro.webwatchlist.model.UserData;
import ch.ruefenacht.sandro.webwatchlist.repository.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public List<UserData> getAll() {
        return userDataRepository.findAll();
    }

    public void save(UserData user) {
        this.userDataRepository.save(user);
    }

    public Optional<UserData> findById(UUID uuid) {
        return userDataRepository.findById(uuid);
    }
}
