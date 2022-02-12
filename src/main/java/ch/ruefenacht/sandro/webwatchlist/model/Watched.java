package ch.ruefenacht.sandro.webwatchlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Watched {

    @GeneratedValue
    @Id
    protected UUID uuid;

    @OneToMany
    protected Set<Media> media;

    @GeneratedValue
    protected Date datetime;
}
