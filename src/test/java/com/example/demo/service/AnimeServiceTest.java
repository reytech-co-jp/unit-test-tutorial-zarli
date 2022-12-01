package com.example.demo.service;

import com.example.demo.entity.Anime;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AnimeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @InjectMocks
    AnimeService animeService;

    @Mock
    AnimeMapper animeMapper;

    @Test
    public void アニメが取得できないときに例外をthrowすること() {
        doReturn(Optional.empty()).when(animeMapper).findById(1);
        assertThatThrownBy(() -> animeService.getAnime(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("resource not found");
    }

    @Test
    public void アニメの登録ができること() {
        doNothing().when(animeMapper).createAnime(any(Anime.class));
        animeService.registerAnime("name", "genre");
        verify(animeMapper).createAnime(any(Anime.class));
    }

    @Test
    public void アニメが全件取得できること() {
        doReturn(List.of(new Anime(1, "Anime1", "Action"), new Anime(2, "Anime2", "Psychological"))).when(animeMapper).findAll();
        List<Anime> actual = animeService.getAllAnime();
        assertThat(actual).isEqualTo(List.of(new Anime(1, "Anime1", "Action"), new Anime(2, "Anime2", "Psychological")));
    }

    @Test
    public void アニメが取得できるときに1件アニメを返すこと() {
        doReturn(Optional.of(new Anime(1, "Anime1", "Action"))).when(animeMapper).findById(1);
        Anime actual = animeService.getAnime(1);
        assertThat(actual).isEqualTo(new Anime(1, "Anime1", "Action"));
    }

    @Test
    public void アニメが更新できること() {
        doReturn(Optional.of(new Anime(1, "Anime1", "Action"))).when(animeMapper).findById(1);
        animeService.updateAnime(1, "Anime3", "History");
        verify(animeMapper).updateAnime(any(Anime.class));
    }

    @Test
    public void アニメが更新できないときに例外をthrowすること() {
        doReturn(Optional.empty()).when(animeMapper).findById(1);
        assertThatThrownBy(() -> animeService.updateAnime(1, "Anime4", "Romance"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("resource not found");
    }

    @Test
    public void アニメが削除できること() {
        doReturn(Optional.of(new Anime(1, "Anime1", "Action"))).when(animeMapper).findById(1);
        animeService.deleteAnime(1);
        verify(animeMapper).deleteAnime(1);
    }

    @Test
    public void アニメが削除できないときに例外をthrowすること() {
        doReturn(Optional.empty()).when(animeMapper).findById(1);
        assertThatThrownBy(() -> animeService.deleteAnime(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("resource not found");
    }

}
