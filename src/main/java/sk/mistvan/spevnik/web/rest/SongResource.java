package sk.mistvan.spevnik.web.rest;

import com.codahale.metrics.annotation.Timed;
import sk.mistvan.spevnik.domain.Song;
import sk.mistvan.spevnik.repository.SongRepository;
import sk.mistvan.spevnik.web.rest.util.HeaderUtil;
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
     * POST  /songs : Create a new song.
     *
     * @param song the song to create
     * @return the ResponseEntity with status 201 (Created) and with body the new song, or with status 400 (Bad Request) if the song has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
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
     * PUT  /songs : Updates an existing song.
     *
     * @param song the song to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated song,
     * or with status 400 (Bad Request) if the song is not valid,
     * or with status 500 (Internal Server Error) if the song couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
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
     * GET  /songs : get all the songs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of songs in body
     */
    @RequestMapping(value = "/songs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Song> getAllSongs() {
        log.debug("REST request to get all Songs");
        List<Song> songs = songRepository.findAll();
        return songs;
    }

    /**
     * GET  /songs/:id : get the "id" song.
     *
     * @param id the id of the song to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the song, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/songs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Song> getSong(@PathVariable Long id) {
        log.debug("REST request to get Song : {}", id);
        Song song = songRepository.findOne(id);
        return Optional.ofNullable(song)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /songs/:id : delete the "id" song.
     *
     * @param id the id of the song to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/songs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        log.debug("REST request to delete Song : {}", id);
        songRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("song", id.toString())).build();
    }

}
