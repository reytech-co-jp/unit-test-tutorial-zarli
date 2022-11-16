package com.example.demo.mapper;

import com.example.demo.entity.Anime;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnimeMapperTests {
    @Autowired
    AnimeMapper animeMapper;

    @Test
    @DataSet(value = "anime.yml")
    void アニメが全件取得できること() {
        List<Anime> animeList = Arrays.asList(new Anime(1, "Anime1", "Action"), new Anime(2, "Anime2", "Adventure"));
        List<Anime> expectedAnimeList = animeMapper.findAll();
        assertThat(expectedAnimeList).hasSize(2).containsAll(animeList);

    }

    @Test
    @DataSet(value = "empty.yml")
    void アニメが空になること() {
        List<Anime> animeList = animeMapper.findAll();
        assertThat(animeList).isEmpty();
    }

    @Test
    @DataSet(value = "anime.yml")
    void 引数のidでアニメを取得できること() {
        Optional<Anime> anime = animeMapper.findById(1);
        assertThat(anime).contains(new Anime(1, "Anime1", "Action"));
    }

    @Test
    @DataSet(value = "empty.yml")
    void 引数のidに対したアニメが存在しない時_空のOptionalを取得すること() {
        Optional<Anime> anime = animeMapper.findById(3);
        assertThat(anime).isEmpty();
    }
}
