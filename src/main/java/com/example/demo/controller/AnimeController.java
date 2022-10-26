package com.example.demo.controller;

import com.example.demo.entity.Anime;
import com.example.demo.form.AnimeForm;
import com.example.demo.service.AnimeService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
