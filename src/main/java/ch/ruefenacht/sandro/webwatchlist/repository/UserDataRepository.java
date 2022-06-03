package ch.ruefenacht.sandro.webwatchlist.repository;

import ch.ruefenacht.sandro.webwatchlist.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, UUID> {

    Optional<UserData> findByEmail(String email);
}
