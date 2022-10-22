package com.example.demo.controller;

import com.example.demo.entity.Anime;
import com.example.demo.form.AnimeForm;
import com.example.demo.service.AnimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api/anime")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping
    public List<Anime> getAllAnime() {
        return animeService.getAllAnime();
    }

    @GetMapping(path = "{animeId}")
    public Anime getAnime(@PathVariable("animeId") Integer id) {
        return animeService.getAnime(id);
    }

    @PostMapping
    public ResponseEntity<String> registerAnime(@Valid @RequestBody AnimeForm animeForm) {
        animeService.registerAnime(animeForm.getName(), animeForm.getGenre());
        URI url = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("api/anime/")
                .build()
                .toUri();
        return ResponseEntity.created(url).body("anime successfully created");
    }

    @PatchMapping(path = "{animeId}")
    public ResponseEntity<String> updateAnime(@PathVariable("animeId") Integer id, @Valid @RequestBody AnimeForm animeForm) {
        animeService.updateAnime(id, animeForm.getName(), animeForm.getGenre());
        URI url = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("api/anime/" + id)
                .build()
                .toUri();
        return ResponseEntity.created(url).body("anime successfully updated");
    }

    @DeleteMapping(path = "{animeId}")
    public ResponseEntity<String> deleteAnime(@PathVariable("animeId") Integer id) {
        animeService.deleteAnime(id);
        URI url = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("api/anime/" + id)
                .build()
                .toUri();
        return ResponseEntity.created(url).body("anime successfully deleted");
    }
}