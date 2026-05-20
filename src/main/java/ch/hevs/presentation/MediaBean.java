package ch.hevs.presentation;


import ch.hevs.businessobject.Media;
import ch.hevs.service.MediaService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named("mediaBean")
public class MediaBean implements Serializable {

    @Inject
    MediaService mediaService;

    private List<Media> medias;


    @PostConstruct
    public void init(){
        try{
        this.medias = mediaService.getAllMedia();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createMedia(){
        // movieService.createblabla();
    }
    public void refresh(){
        this.medias = mediaService.getAllMedia();
    }




}
