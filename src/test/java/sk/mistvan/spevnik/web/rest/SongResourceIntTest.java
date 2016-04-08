package sk.mistvan.spevnik.web.rest;

import sk.mistvan.spevnik.SpevnikApp;
import sk.mistvan.spevnik.domain.Song;
import sk.mistvan.spevnik.repository.SongRepository;

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
 * Test class for the SongResource REST controller.
 *
 * @see SongResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpevnikApp.class)
@WebAppConfiguration
@IntegrationTest
public class SongResourceIntTest {

    private static final String DEFAULT_SLUG = "AAAAA";
    private static final String UPDATED_SLUG = "BBBBB";
    private static final String DEFAULT_LINK = "AAAAA";
    private static final String UPDATED_LINK = "BBBBB";

    @Inject
    private SongRepository songRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSongMockMvc;

    private Song song;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SongResource songResource = new SongResource();
        ReflectionTestUtils.setField(songResource, "songRepository", songRepository);
        this.restSongMockMvc = MockMvcBuilders.standaloneSetup(songResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        song = new Song();
        song.setSlug(DEFAULT_SLUG);
        song.setLink(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void createSong() throws Exception {
        int databaseSizeBeforeCreate = songRepository.findAll().size();

        // Create the Song

        restSongMockMvc.perform(post("/api/songs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(song)))
                .andExpect(status().isCreated());

        // Validate the Song in the database
        List<Song> songs = songRepository.findAll();
        assertThat(songs).hasSize(databaseSizeBeforeCreate + 1);
        Song testSong = songs.get(songs.size() - 1);
        assertThat(testSong.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testSong.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void getAllSongs() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get all the songs
        restSongMockMvc.perform(get("/api/songs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(song.getId().intValue())))
                .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get the song
        restSongMockMvc.perform(get("/api/songs/{id}", song.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(song.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSong() throws Exception {
        // Get the song
        restSongMockMvc.perform(get("/api/songs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);
        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song
        Song updatedSong = new Song();
        updatedSong.setId(song.getId());
        updatedSong.setSlug(UPDATED_SLUG);
        updatedSong.setLink(UPDATED_LINK);

        restSongMockMvc.perform(put("/api/songs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSong)))
                .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songs = songRepository.findAll();
        assertThat(songs).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songs.get(songs.size() - 1);
        assertThat(testSong.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testSong.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void deleteSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);
        int databaseSizeBeforeDelete = songRepository.findAll().size();

        // Get the song
        restSongMockMvc.perform(delete("/api/songs/{id}", song.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Song> songs = songRepository.findAll();
        assertThat(songs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
