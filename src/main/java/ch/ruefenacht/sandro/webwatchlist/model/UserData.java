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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class UserData {
    @Id
    @GeneratedValue
    protected UUID id;

    @Column(length = 100, unique = true, nullable = false)
    protected String username;

    @Column(length = 50, nullable = false)
    protected String firstname;

    @Column(length = 50, nullable = false)
    protected String lastname;

    @Column(length = 320)
    protected String email;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    protected byte[] profileImage;
}
