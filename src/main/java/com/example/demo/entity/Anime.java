package com.example.demo.entity;

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
}