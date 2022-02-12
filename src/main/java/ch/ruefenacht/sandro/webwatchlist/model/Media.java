package ch.ruefenacht.sandro.webwatchlist.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter
public class Media {
    @Id
    @GeneratedValue
    protected UUID id;
}
