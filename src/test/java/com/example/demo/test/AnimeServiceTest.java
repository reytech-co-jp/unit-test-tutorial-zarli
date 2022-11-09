package com.example.demo.test;

import com.example.demo.entity.Anime;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AnimeMapper;
import com.example.demo.service.AnimeService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
