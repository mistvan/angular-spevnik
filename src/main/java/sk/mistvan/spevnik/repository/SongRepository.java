package sk.mistvan.spevnik.repository;

import sk.mistvan.spevnik.domain.Song;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Song entity.
 */
public interface SongRepository extends JpaRepository<Song,Long> {

}
