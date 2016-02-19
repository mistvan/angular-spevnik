package .web.rest;

import .Application;
import .domain.Song;
import .repository.SongRepository;

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
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SongResourceIntTest {


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
        song.setSongText(DEFAULT_SONG_TEXT);
    }

    @Test
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
        assertThat(testSong.getSongText()).isEqualTo(DEFAULT_SONG_TEXT);
    }

    @Test
    public void getAllSongs() throws Exception {
        // Initialize the database
        songRepository.save(song);

        // Get all the songs
        restSongMockMvc.perform(get("/api/songs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].songText").value(hasItem(DEFAULT_SONG_TEXT.toString())));
    }

    @Test
    public void getSong() throws Exception {
        // Initialize the database
        songRepository.save(song);

        // Get the song
        restSongMockMvc.perform(get("/api/songs/{id}", song.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.songText").value(DEFAULT_SONG_TEXT.toString()));
    }

    @Test
    public void getNonExistingSong() throws Exception {
        // Get the song
        restSongMockMvc.perform(get("/api/songs/{id}", ))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateSong() throws Exception {
        // Initialize the database
        songRepository.save(song);

		int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song
        song.setSongText(UPDATED_SONG_TEXT);

        restSongMockMvc.perform(put("/api/songs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(song)))
                .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songs = songRepository.findAll();
        assertThat(songs).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songs.get(songs.size() - 1);
        assertThat(testSong.getSongText()).isEqualTo(UPDATED_SONG_TEXT);
    }

    @Test
    public void deleteSong() throws Exception {
        // Initialize the database
        songRepository.save(song);

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
