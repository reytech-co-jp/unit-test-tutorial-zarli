package com.example.demo.controller;

import com.example.demo.entity.Anime;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.AnimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AnimeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    private final String BASE_PATH = "classpath:test-data/";

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AnimeService animeService;

    @Test
    void アニメが全件取得できること() throws Exception {
        var response = List.of(new Anime(1, "鬼滅の刃", "ダークファンタジー"), new Anime(2, "SPY×FAMILY", "ホームコメディ"), new Anime(3, "Dr.STONE", "survival"));

        String url = "/api/anime";
        Mockito.when(animeService.getAllAnime()).thenReturn(response);
        var result = mockMvc.perform(get(String.format("/api/anime"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals(objectMapper.readTree(getJsonFileData("200AllAnime.json")), objectMapper.readTree(result));
    }

    @Test
    void アニメが取得できるときに1件アニメを返すこと() throws Exception {
        var id = 1;
        var response = new Anime(1, "鬼滅の刃", "ダークファンタジー");

        Mockito.when(animeService.getAnime(id)).thenReturn(response);
        var result = mockMvc.perform(get(String.format("/api/anime/%d", id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals(objectMapper.readTree(getJsonFileData("200OneAnime.json")), objectMapper.readTree(result));
    }

    @Test
    void アニメが取得できないときに例外をthrowすること() throws Exception {
        var id = 4;

        Mockito.when(animeService.getAnime(id)).thenThrow(
                new ResourceNotFoundException("resource not found"));
        var result = mockMvc.perform(get(String.format("/api/anime/%d", id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException().getMessage();

        Assertions.assertEquals(result, "resource not found");
    }

    @Test
    void アニメの登録ができること() throws Exception {
        Anime anime = new Anime("Your Name", "Romantic Fantasy");

        var result = mockMvc.perform(post(String.format("/api/anime/"))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(anime)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals("anime successfully created", result);
    }

    @Test
    void アニメの更新ができること() throws Exception {
        var id = 1;
        Anime anime = new Anime(id, "Your Name", "Romantic Fantasy");

        var result = mockMvc.perform(patch(String.format("/api/anime/%d", id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(anime)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals("anime successfully updated", result);
    }

    @Test
    void アニメの削除ができること() throws Exception {
        var id = 1;
        Anime anime = new Anime(id, "Your Name", "Romantic Fantasy");

        var result = mockMvc.perform(delete(String.format("/api/anime/%d", id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(anime)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assertions.assertEquals("anime successfully deleted", result);
    }

    @Test
    void 更新対象のアニメが存在しないときにレスポンスボディにエラーメッセージが返されること() throws Exception {
        var id = 4;
        var name = "Your Name";
        var genre = "Romantic Fantasy";

        Anime anime = new Anime(id, name, genre);

        doThrow(new ResourceNotFoundException("resource not found")).when(animeService).updateAnime(id, name, genre);
        var result = mockMvc.perform(patch(String.format("/api/anime/%d", id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(anime)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException().getMessage();

        Assertions.assertEquals(result, "resource not found");
    }

    @Test
    void 削除対象のアニメが存在しないときにレスポンスボディにエラーメッセージが返されること() throws Exception {
        var id = 4;

        doThrow(new ResourceNotFoundException("resource not found")).when(animeService).deleteAnime(id);
        var result = mockMvc.perform(delete(String.format("/api/anime/%d", id))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResolvedException().getMessage();

        Assertions.assertEquals(result, "resource not found");
    }

    private String getJsonFileData(String fileName) throws IOException {
        var jsonResult = resourceLoader.getResource(BASE_PATH + fileName);
        return StreamUtils.copyToString(jsonResult.getInputStream(), StandardCharsets.UTF_8);
    }
}
