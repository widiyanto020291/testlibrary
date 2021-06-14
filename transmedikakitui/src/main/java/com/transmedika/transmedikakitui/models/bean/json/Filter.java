package com.transmedika.transmedikakitui.models.bean.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Filter {
    public static final String rates = "rates";
    public static final String experience = "experience";

    @SerializedName("symbol") private String symbol;
    @SerializedName("value") private String value;
    @SerializedName("description") private String description;

    //helper
    @Expose private Boolean checked;
    @Expose private String group;

    public Filter() {
    }

    public Filter(String value, String symbol) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return symbol.equals(filter.symbol) &&
                value.equals(filter.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, value);
    }
}
