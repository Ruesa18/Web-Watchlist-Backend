package ch.ruefenacht.sandro.webwatchlist.repository;

import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    List<Movie> findByNameContaining(String name);

    List<Movie> findByOrderByNameAsc();
}
