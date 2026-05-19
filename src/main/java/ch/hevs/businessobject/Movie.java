package ch.hevs.businessobject;
import jakarta.persistence.Entity;

@Entity
public class Movie extends Media {
    private int duration; //Minutes

    public Movie() {
        super();
    }

    public Movie(String title, String director, double price, Category category, int duration) {
        super(title, director, price, category);
        this.duration = duration;
    }

    @Override
    public int getRentalDurationInDays() {
        return 1; //24 Hours
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
