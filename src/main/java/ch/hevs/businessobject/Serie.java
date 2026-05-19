package ch.hevs.businessobject;

import jakarta.persistence.Entity;

@Entity
public class Serie extends Media {
    private int seasons;
    private int episodes;


    public Serie() {
        super();
    }

    public Serie(String title, String director, double price, Category category, int seasons, int episodes) {
        super(title, director, price, category);
        this.seasons = seasons;
        this.episodes = episodes;
    }

    @Override
    public int getRentalDurationInDays() {
        return 30;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }
}
