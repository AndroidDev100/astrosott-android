package com.astro.sott.usermanagment.modelClasses;

import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response;

public class EvergentCommonResponse {

    private String errorMessage;
    private boolean status;
    private SearchAccountv2Response searchAccountv2Response;

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setSearchAccountv2Response(SearchAccountv2Response searchAccountv2Response) {
        this.searchAccountv2Response = searchAccountv2Response;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public SearchAccountv2Response getSearchAccountv2Response() {
        return searchAccountv2Response;
    }
}
