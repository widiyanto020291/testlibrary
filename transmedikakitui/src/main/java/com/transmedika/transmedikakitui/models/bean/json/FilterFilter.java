package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterFilter {
    @SerializedName("rates")private List<Filter> rates;
    @SerializedName("experience")private List<Filter> experiences;
    @SerializedName("search")private String search;

    public FilterFilter() {
    }

    public List<Filter> getRates() {
        return rates;
    }

    public void setRates(List<Filter> rates) {
        this.rates = rates;
    }

    public List<Filter> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Filter> experiences) {
        this.experiences = experiences;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
