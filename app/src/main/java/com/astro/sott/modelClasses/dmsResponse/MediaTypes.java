
package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaTypes {

    @SerializedName("Linear")
    @Expose
    private String linear;
    @SerializedName("Movie")
    @Expose
    private String movie;
    @SerializedName("Episode")
    @Expose
    private String episode;
    @SerializedName("Series")
    @Expose
    private String series;
    @SerializedName("Season")
    @Expose
    private String season;
    @SerializedName("Trailer")
    @Expose
    private String trailer;
    @SerializedName("Highlight")
    @Expose
    private String highlight;
    @SerializedName("Collection")
    @Expose
    private String collection;
    @SerializedName("Program")
    @Expose
    private String program;

    public String getLinear() {
        return linear;
    }

    public void setLinear(String linear) {
        this.linear = linear;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

}
