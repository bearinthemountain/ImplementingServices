package ch.hevs.businessobject;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private LocalDateTime rentalStart;

    @Column(nullable=false)
    private LocalDateTime rentalEnd;

    @Column(nullable = false)
    private double appliedPrice;

    public Rental() {
    }

    public Rental(LocalDateTime rentalStart, LocalDateTime rentalEnd, double appliedPrice, User user, Media media) {
        this.rentalStart = rentalStart;
        this.rentalEnd = rentalEnd;
        this.appliedPrice = appliedPrice;
        this.user = user;
        this.media = media;
    }



    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    private Media media;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(rentalEnd);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(LocalDateTime rentalStart) {
        this.rentalStart = rentalStart;
    }

    public LocalDateTime getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(LocalDateTime rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public double getAppliedPrice() {
        return appliedPrice;
    }

    public void setAppliedPrice(double appliedPrice) {
        this.appliedPrice = appliedPrice;
    }

    public User getViewer() {
        return user;
    }

    public void setViewer(User user) {
        this.user = user;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
