package com.example.demo.integration;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnimeIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    ZonedDateTime zonedDateTime = ZonedDateTime.of(2022, 12, 21, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

    @Test
    @DataSet(value = "datasets/anime.yml")
    void アニメを全件取得できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/anime"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = """
                [{
                    "id": 1,
                    "name": "Anime1",
                    "genre": "Action"
                },
                {
                    "id": 2,
                    "name": "Anime2",
                    "genre": "Adventure"
                }]
                """;

        JSONAssert.assertEquals(expectedResult, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/anime.yml")
    void 特定のアニメを１件取得できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/anime/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = """
                {
                    "id": 1,
                    "name": "Anime1",
                    "genre": "Action"
                }
                """;

        JSONAssert.assertEquals(expectedResult, response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/anime.yml")
    void 存在しないアニメIDを指定した時にNotFoundが返ってくること() throws Exception{
        try(MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = Mockito.mockStatic(ZonedDateTime.class)) {
            zonedDateTimeMockedStatic.when(ZonedDateTime::now).thenReturn(zonedDateTime);

            String response = mockMvc.perform(MockMvcRequestBuilders.get("/api/anime/3"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

            String expected = """
                    {
                        "error": "Not Found",
                        "path": "/api/anime/3",
                        "status": "404",
                        "message": "resource not found",
                        "timestamp": "2022-12-21T00:00+09:00[Asia/Tokyo]"

                    }
                    """;

            JSONAssert.assertEquals(expected, response, JSONCompareMode.STRICT);
        }
    }

    @Test
    @DataSet(value = "datasets/anime.yml")
    @ExpectedDataSet(value = "datasets/expectedAfterInsertAnime.yml", ignoreCols = "id")
    void アニメが登録できること() throws Exception{
        String response = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/anime")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name" : "Anime3",
                                    "genre" : "Power" 
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo("anime successfully created");
    }

    @Test
    @DataSet(value = "datasets/anime.yml")
    @ExpectedDataSet(value = "datasets/expectedAfterUpdateAnime.yml", ignoreCols = "id")
    void アニメが更新できること() throws Exception{
        String response = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/api/anime/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name" : "Anime4",
                                    "genre" : "Psychological" 
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo("anime successfully updated");
    }

    @Test
    @DataSet(value = "datasets/anime.yml")
    void 更新時に指定したIDのアニメが存在しない場合404エラーとなること() throws Exception {
        try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = Mockito.mockStatic(ZonedDateTime.class)) {
            zonedDateTimeMockedStatic.when(ZonedDateTime::now).thenReturn(zonedDateTime);
            String response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/anime/3")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content("""
                                {
                                    "name" : "Anime4",
                                    "genre" : "Psychological" 
                                }
                                """))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = """
                    {
                        "error": "Not Found",
                        "path": "/api/anime/3",
                        "status": "404",
                        "message": "resource not found",
                        "timestamp": "2022-12-21T00:00+09:00[Asia/Tokyo]"

                    }
                    """;
            JSONAssert.assertEquals(expectedResult, response, JSONCompareMode.STRICT);
        }
    }

    @Test
    @DataSet(value = "datasets/anime.yml")
    @ExpectedDataSet(value = "datasets/expectedAfterDeleteAnime.yml")
    void アニメが削除できること() throws Exception {
        String url = "/api/anime/1";
        String response = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo("anime successfully deleted");
    }

    @Test
    @DataSet(value = "datasets/anime.yml")
    void 削除時に指定したIDのアニメが存在しない場合404エラーとなりエラーのレスポンスを返すこと() throws Exception {
        try (MockedStatic<ZonedDateTime> zonedDateTimeMockedStatic = Mockito.mockStatic(ZonedDateTime.class)) {
            zonedDateTimeMockedStatic.when(ZonedDateTime::now).thenReturn(zonedDateTime);
            String url = "/api/anime/3";
            String response = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
            String expectedResult = """
                    {
                        "error": "Not Found",
                        "path": "/api/anime/3",
                        "status": "404",
                        "message": "resource not found",
                        "timestamp": "2022-12-21T00:00+09:00[Asia/Tokyo]"

                    }
                    """;

            JSONAssert.assertEquals(expectedResult, response, JSONCompareMode.STRICT);
        }
    }
}
