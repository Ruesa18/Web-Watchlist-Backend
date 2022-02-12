package ch.ruefenacht.sandro.webwatchlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Episode {

    @GeneratedValue
    @Id
    protected UUID uuid;

    protected String name;

    protected int number;
}
