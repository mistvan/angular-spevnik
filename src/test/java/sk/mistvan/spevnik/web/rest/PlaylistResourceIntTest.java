package sk.mistvan.spevnik.web.rest;

import sk.mistvan.spevnik.SpevnikApp;
import sk.mistvan.spevnik.domain.Playlist;
import sk.mistvan.spevnik.repository.PlaylistRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PlaylistResource REST controller.
 *
 * @see PlaylistResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpevnikApp.class)
@WebAppConfiguration
@IntegrationTest
public class PlaylistResourceIntTest {

    private static final String DEFAULT_SLUG = "AAAAA";
    private static final String UPDATED_SLUG = "BBBBB";
    private static final String DEFAULT_LINK = "AAAAA";
    private static final String UPDATED_LINK = "BBBBB";

    @Inject
    private PlaylistRepository playlistRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPlaylistMockMvc;

    private Playlist playlist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlaylistResource playlistResource = new PlaylistResource();
        ReflectionTestUtils.setField(playlistResource, "playlistRepository", playlistRepository);
        this.restPlaylistMockMvc = MockMvcBuilders.standaloneSetup(playlistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        playlist = new Playlist();
        playlist.setSlug(DEFAULT_SLUG);
        playlist.setLink(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void createPlaylist() throws Exception {
        int databaseSizeBeforeCreate = playlistRepository.findAll().size();

        // Create the Playlist

        restPlaylistMockMvc.perform(post("/api/playlists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(playlist)))
                .andExpect(status().isCreated());

        // Validate the Playlist in the database
        List<Playlist> playlists = playlistRepository.findAll();
        assertThat(playlists).hasSize(databaseSizeBeforeCreate + 1);
        Playlist testPlaylist = playlists.get(playlists.size() - 1);
        assertThat(testPlaylist.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testPlaylist.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void getAllPlaylists() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get all the playlists
        restPlaylistMockMvc.perform(get("/api/playlists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(playlist.getId().intValue())))
                .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getPlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);

        // Get the playlist
        restPlaylistMockMvc.perform(get("/api/playlists/{id}", playlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(playlist.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlaylist() throws Exception {
        // Get the playlist
        restPlaylistMockMvc.perform(get("/api/playlists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);
        int databaseSizeBeforeUpdate = playlistRepository.findAll().size();

        // Update the playlist
        Playlist updatedPlaylist = new Playlist();
        updatedPlaylist.setId(playlist.getId());
        updatedPlaylist.setSlug(UPDATED_SLUG);
        updatedPlaylist.setLink(UPDATED_LINK);

        restPlaylistMockMvc.perform(put("/api/playlists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlaylist)))
                .andExpect(status().isOk());

        // Validate the Playlist in the database
        List<Playlist> playlists = playlistRepository.findAll();
        assertThat(playlists).hasSize(databaseSizeBeforeUpdate);
        Playlist testPlaylist = playlists.get(playlists.size() - 1);
        assertThat(testPlaylist.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testPlaylist.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void deletePlaylist() throws Exception {
        // Initialize the database
        playlistRepository.saveAndFlush(playlist);
        int databaseSizeBeforeDelete = playlistRepository.findAll().size();

        // Get the playlist
        restPlaylistMockMvc.perform(delete("/api/playlists/{id}", playlist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Playlist> playlists = playlistRepository.findAll();
        assertThat(playlists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
