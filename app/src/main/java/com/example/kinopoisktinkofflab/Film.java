package com.example.kinopoisktinkofflab;

import java.util.List;

public class Film {
    private Integer kinopoiskId;
    private String nameRu;
    private String nameEn;
    private Integer year;
    private String posterUrl;
    private String description;
    private List<String> countries;
    private List<String> genres;

    public Integer getId() {
        return this.kinopoiskId;
    }

    public void setId(Integer id) {
        this.kinopoiskId = id;
    }

    public String getNameRu() {
        return this.nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPosterUrl() {
        return this.posterUrl;
    }

    public void setPosterUrl(String url) {
        this.posterUrl = url;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCountries() {
        return this.countries;
    }

    public void setCountries(List<String> ncountries) {
        this.countries = ncountries;
    }

    public List<String> getGenres() {
        return this.genres;
    }

    public void setGenres(List<String> ngenres) {
        this.genres = ngenres;
    }

    @Override
    public String toString() {
        return
            String.format(
                "Film{id = %d, nameEn = %s, nameRu = %s, year = %d, posterUrl = %s,  description = %s, countries = %s, genres = %s}",
            getId(), getNameEn(), getNameRu(), getYear(), getPosterUrl(), getDescription(), String.join(",", getCountries()), String.join(",", getGenres())
            );
    }
}

