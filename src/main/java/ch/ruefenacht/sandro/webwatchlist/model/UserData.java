package ch.ruefenacht.sandro.webwatchlist.model;

import ch.ruefenacht.sandro.webwatchlist.dto.MediaShowDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
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

    @Column(length = 50, nullable = false)
    protected String firstname;

    @Column(length = 50, nullable = false)
    protected String lastname;

    @Column(length = 320, unique = true, nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String password;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    protected byte[] profileImage;

    @ManyToMany
    protected Set<Media> favorites = Set.of();

    @ManyToMany
    protected Set<Media> watchlist = Set.of();

    public Set<MediaShowDto> getFavoritesAsDto() {
        Set<MediaShowDto> favorites = new HashSet<>(Set.of());
        for(Media media : this.favorites) {
            favorites.add(new MediaShowDto(media.getId()));
        }
        return favorites;
    }

    public Set<MediaShowDto> getWatchlistAsDto() {
        Set<MediaShowDto> watchlist = new HashSet<>(Set.of());
        for(Media media : this.watchlist) {
            watchlist.add(new MediaShowDto(media.getId()));
        }
        return watchlist;
    }
}
