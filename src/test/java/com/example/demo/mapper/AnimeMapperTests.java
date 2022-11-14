package com.example.demo.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.example.demo.entity.Anime;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnimeMapperTests {
	@Autowired
	AnimeMapper animeMapper;
	
	@Test
    @DataSet(value = "datatests/anime.yml")
    void �A�j�����S���擾�ł��邱��() {
	   List<Anime> animeList = Arrays.asList(new Anime(1, "Anime1", "Action"), new Anime(2, "Anime2", "Adventure"));
       List<Anime> expectedAnimeList = animeMapper.findAll();
       assertThat(expectedAnimeList).hasSize(2).containsAll(animeList);
       
    }
	
	@Test
	@DataSet(value = "datatests/empty.yml")
    void �A�j������ɂȂ邱��() {
        List<Anime> animeList = animeMapper.findAll();
        assertThat(animeList).isEmpty();
    }
	
	@Test
	@DataSet(value = "datatests/anime.yml")
    void ������id�ŃA�j�����擾�ł��邱��() {
        Optional<Anime> anime = animeMapper.findById(1);
        assertThat(anime).contains(new Anime(1, "Anime1", "Action"));
    }

    @Test
    @DataSet(value = "datatests/empty.yml")
    void ������id�ɑΉ������A�j�������݂��Ȃ���_���Optional���擾���邱��() {
        Optional<Anime> anime = animeMapper.findById(3);
        assertThat(anime).isEmpty();
    }
}
