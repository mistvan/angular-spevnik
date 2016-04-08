package sk.mistvan.spevnik.web.rest;

import com.codahale.metrics.annotation.Timed;
import sk.mistvan.spevnik.domain.Playlist;
import sk.mistvan.spevnik.repository.PlaylistRepository;
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
 * REST controller for managing Playlist.
 */
@RestController
@RequestMapping("/api")
public class PlaylistResource {

    private final Logger log = LoggerFactory.getLogger(PlaylistResource.class);
        
    @Inject
    private PlaylistRepository playlistRepository;
    
    /**
     * POST  /playlists : Create a new playlist.
     *
     * @param playlist the playlist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new playlist, or with status 400 (Bad Request) if the playlist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/playlists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) throws URISyntaxException {
        log.debug("REST request to save Playlist : {}", playlist);
        if (playlist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("playlist", "idexists", "A new playlist cannot already have an ID")).body(null);
        }
        Playlist result = playlistRepository.save(playlist);
        return ResponseEntity.created(new URI("/api/playlists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("playlist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /playlists : Updates an existing playlist.
     *
     * @param playlist the playlist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated playlist,
     * or with status 400 (Bad Request) if the playlist is not valid,
     * or with status 500 (Internal Server Error) if the playlist couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/playlists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Playlist> updatePlaylist(@RequestBody Playlist playlist) throws URISyntaxException {
        log.debug("REST request to update Playlist : {}", playlist);
        if (playlist.getId() == null) {
            return createPlaylist(playlist);
        }
        Playlist result = playlistRepository.save(playlist);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("playlist", playlist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /playlists : get all the playlists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of playlists in body
     */
    @RequestMapping(value = "/playlists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Playlist> getAllPlaylists() {
        log.debug("REST request to get all Playlists");
        List<Playlist> playlists = playlistRepository.findAllWithEagerRelationships();
        return playlists;
    }

    /**
     * GET  /playlists/:id : get the "id" playlist.
     *
     * @param id the id of the playlist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the playlist, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/playlists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Long id) {
        log.debug("REST request to get Playlist : {}", id);
        Playlist playlist = playlistRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(playlist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /playlists/:id : delete the "id" playlist.
     *
     * @param id the id of the playlist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/playlists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        log.debug("REST request to delete Playlist : {}", id);
        playlistRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("playlist", id.toString())).build();
    }

}
