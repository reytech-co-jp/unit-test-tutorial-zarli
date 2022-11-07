package com.example.demo.service;

import com.example.demo.entity.Anime;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AnimeMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AnimeService {

    private final AnimeMapper animeMapper;

    public AnimeService(AnimeMapper animeMapper) {
        this.animeMapper = animeMapper;
    }

    public List<Anime> getAllAnime() {
        List<Anime> animeList = animeMapper.findAll();
        return animeList;
    }

    public Anime getAnime(Integer id) {
        Optional<Anime> animeId = animeMapper.findById(id);
        if (animeId.isPresent()) {
            return animeId.get();
        } else {
            throw new ResourceNotFoundException("resource not found");
        }
    }

    public void registerAnime(String name, String genre) {
        Anime anime = new Anime(name, genre);
        animeMapper.createAnime(anime);
    }

    public void updateAnime(Integer id, String name, String genre) {
        Optional<Anime> animeId = animeMapper.findById(id);
        if (animeId.isPresent()) {
            Anime anime = new Anime(id, name, genre);
            animeMapper.updateAnime(anime);
        } else {
            throw new ResourceNotFoundException("resource not found");
        }
    }

    public void deleteAnime(Integer id) {
        Optional<Anime> animeId = animeMapper.findById(id);
        if (animeId.isPresent()) {
            animeMapper.deleteAnime(id);
        } else {
            throw new ResourceNotFoundException("resource not found");
        }
    }
}
