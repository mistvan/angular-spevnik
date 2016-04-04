package sk.mistvan.spevnik.repository;

import sk.mistvan.spevnik.domain.Playlist;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Playlist entity.
 */
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {

    @Query("select distinct playlist from Playlist playlist left join fetch playlist.songss")
    List<Playlist> findAllWithEagerRelationships();

    @Query("select playlist from Playlist playlist left join fetch playlist.songss where playlist.id =:id")
    Playlist findOneWithEagerRelationships(@Param("id") Long id);

}
