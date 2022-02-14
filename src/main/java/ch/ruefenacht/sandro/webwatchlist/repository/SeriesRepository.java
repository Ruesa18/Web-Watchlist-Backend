package ch.ruefenacht.sandro.webwatchlist.repository;

import ch.ruefenacht.sandro.webwatchlist.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<Series, UUID> {}
