package .web.rest;

import com.codahale.metrics.annotation.Timed;
import .domain.Song;
import .repository.SongRepository;
import .web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Song.
 */
@RestController
@RequestMapping("/api")
public class SongResource {

    private final Logger log = LoggerFactory.getLogger(SongResource.class);
        
    @Inject
    private SongRepository songRepository;
    
    /**
     * POST  /songs -> Create a new song.
     */
    @RequestMapping(value = "/songs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Song> createSong(@RequestBody Song song) throws URISyntaxException {
        log.debug("REST request to save Song : {}", song);
        if (song.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("song", "idexists", "A new song cannot already have an ID")).body(null);
        }
        Song result = songRepository.save(song);
        return ResponseEntity.created(new URI("/api/songs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("song", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /songs -> Updates an existing song.
     */
    @RequestMapping(value = "/songs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Song> updateSong(@RequestBody Song song) throws URISyntaxException {
        log.debug("REST request to update Song : {}", song);
        if (song.getId() == null) {
            return createSong(song);
        }
        Song result = songRepository.save(song);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("song", song.getId().toString()))
            .body(result);
    }

    /**
     * GET  /songs -> get all the songs.
     */
    @RequestMapping(value = "/songs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Song> getAllSongs() {
        log.debug("REST request to get all Songs");
        return songRepository.findAll();
            }

    /**
     * GET  /songs/:id -> get the "id" song.
     */
    @RequestMapping(value = "/songs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Song> getSong(@PathVariable Long id) {
        log.debug("REST request to get Song : {}", id);
        Song song = ;
        return Optional.ofNullable(song)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /songs/:id -> delete the "id" song.
     */
    @RequestMapping(value = "/songs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        log.debug("REST request to delete Song : {}", id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("song", id.toString())).build();
    }
}
