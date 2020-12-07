
package com.dialog.dialoggo.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaTypes {

    @SerializedName("Movie")
    @Expose
    private String movie;
    @SerializedName("Linear")
    @Expose
    private String linear;
    @SerializedName("ShortVideos")
    @Expose
    private String shortVideos;
    @SerializedName("Trailer")
    @Expose
    private String trailer;
    @SerializedName("Episode")
    @Expose
    private String episode;
    @SerializedName("Drama")
    @Expose
    private String drama;
    @SerializedName("Genre")
    @Expose
    private String genre;
    @SerializedName("Program")
    @Expose
    private String program;
    @SerializedName("SpotlightSeries")
    @Expose
    private String spotlightSeries;

    @SerializedName("Promo")
    @Expose
    private String promo;

    public String getPromo(){
        return promo;
    }

    public void setPromo(String promo){
        this.promo = promo;
    }


    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getLinear() {
        return linear;
    }

    public void setLinear(String linear) {
        this.linear = linear;
    }

    public String getShortVideos() {
        return shortVideos;
    }

    public void setShortVideos(String shortVideos) {
        this.shortVideos = shortVideos;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getDrama() {
        return drama;
    }

    public void setDrama(String drama) {
        this.drama = drama;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSpotlightSeries() {
        return spotlightSeries;
    }

    public void setSpotlightSeries(String spotlightSeries) {
        this.spotlightSeries = spotlightSeries;
    }
}
