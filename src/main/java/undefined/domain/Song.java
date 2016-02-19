package .domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Song.
 */

public class Song implements Serializable {


    private  songText;

    public  getId() {
        return id;
    }

    public void setId( id) {
        this.id = id;
    }

    public  getSongText() {
        return songText;
    }

    public void setSongText( songText) {
        this.songText = songText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Song song = (Song) o;
        return Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Song{" +
            "id=" + id +
            ", songText='" + songText + "'" +
            '}';
    }
}
