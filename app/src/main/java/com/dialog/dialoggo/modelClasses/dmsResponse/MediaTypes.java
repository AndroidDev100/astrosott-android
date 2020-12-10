
package com.dialog.dialoggo.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaTypes {

    @SerializedName("SpotlightEpisode")
    @Expose
    private String spotlightEpisode;

    @SerializedName("UGCVideo")
    @Expose
    private String uGCVideo;

    @SerializedName("Clips")
    @Expose
    private String clips;


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

    @SerializedName("WebSeries")
    @Expose
    private String webSeries;

    @SerializedName("WebEpisode")
    @Expose
    private String webEpisode;

    @SerializedName("UGCCreator")
    @Expose
    private String uGCCreator;

    @SerializedName("ShortFilm")
    @Expose
    private String shortFilm;

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

    public String getWebSeries() {
        return webSeries;
    }

    public void setWebSeries(String webSeries) {
        this.webSeries = webSeries;
    }

    public String getWebEpisode() {
        return webEpisode;
    }

    public void setWebEpisode(String webEpisode) {
        this.webEpisode = webEpisode;
    }

    public String getUGCCreator() {
        return uGCCreator;
    }

    public void setUGCCreator(String uGCCreator) {
        this.uGCCreator = uGCCreator;
    }

    public String getShortFilm() {
        return shortFilm;
    }

    public void setShortFilm(String shortFilm) {
        this.shortFilm = shortFilm;
    }

    public String getSpotlightEpisode() {
        return spotlightEpisode;
    }

    public void setSpotlightEpisode(String spotlightEpisode) {
        this.spotlightEpisode = spotlightEpisode;
    }

    public String getClip() {
        return clips;
    }

    public void setClip(String clip) {
        this.clips = clip;
    }

    public String getUGCVideo() {
        return uGCVideo;
    }

    public void setUGCVideo(String uGCVideo) {
        this.uGCVideo = uGCVideo;
    }

}
