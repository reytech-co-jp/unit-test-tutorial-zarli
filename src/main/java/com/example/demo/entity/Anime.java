package com.example.demo.entity;

import java.util.Objects;

public class Anime {
    private Integer id;
    private String name;
    private String genre;

    public Anime(Integer id, String name, String genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }

    public Anime(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    public Anime() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }
        Anime anime = (Anime) o;
        return Objects.equals(id, anime.id) 
        		&& Objects.equals(name, anime.name) 
        		&& Objects.equals(genre, anime.genre);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, genre);
    }
}
