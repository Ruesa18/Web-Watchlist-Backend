package ch.ruefenacht.sandro.webwatchlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    @GeneratedValue
    @Id
    protected UUID uuid;

    protected String name;

    protected int number;

    @OneToMany
    protected Set<Episode> episodes;
}
