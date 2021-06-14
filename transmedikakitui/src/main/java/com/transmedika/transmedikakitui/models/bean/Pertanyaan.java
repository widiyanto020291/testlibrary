package com.transmedika.transmedikakitui.models.bean;

import androidx.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;

public class Pertanyaan {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_TEXTVIEW, TYPE_EDITTEXT, TYPE_RADIO_GROUP, TYPE_DROPDOWNLIST, TYPE_DROPDOWNLISTPAGE,
            TYPE_CHECK_GROUP, TYPE_DATE, TYPE_DATE, TYPE_TIME, TYPE_DATE_TIME})
    public @interface TYPE_VIEW { }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_ANSWER_SINGLE, TYPE_ANSWER_MULTIPLE})
    public @interface TYPE_ANSWER { }

    public static final String TYPE_TEXTVIEW = "TextView";
    public static final String TYPE_EDITTEXT = "Edittext";
    public static final String TYPE_RADIO_GROUP = "RadioGroup";
    public static final String TYPE_DROPDOWNLIST = "Spinner";
    public static final String TYPE_DROPDOWNLISTPAGE = "SpinnerPage";
    public static final String TYPE_CHECK_GROUP = "CheckGroup";
    public static final String TYPE_DATE = "DateView";
    public static final String TYPE_TIME = "TimeView";
    public static final String TYPE_DATE_TIME = "DateTimeView";

    public static final String TYPE_ANSWER_SINGLE = "Single";
    public static final String TYPE_ANSWER_MULTIPLE = "Multiple";

    @SerializedName("id_pertanyaan") private Long idPertanyaan;
    @SerializedName("pertanyaan") private String pertanyaan;
    @SerializedName("type_view") private @TYPE_VIEW String tipeView;
    @SerializedName("jawabans") private List<Jawaban> jawabans;
    @SerializedName("mandatori") private Boolean mandatori;
    @SerializedName("type_answer") private @TYPE_ANSWER String typeAnswer;

    public Long getIdPertanyaan() {
        return idPertanyaan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public @TYPE_VIEW String getTipeView() {
        return tipeView;
    }

    public List<Jawaban> getJawabans() {
        return jawabans;
    }

    public Boolean getMandatori() {
        return mandatori;
    }

    public void setMandatori(Boolean mandatori) {
        this.mandatori = mandatori;
    }

    public @TYPE_ANSWER String getTypeAnswer() {
        return typeAnswer;
    }

    public void setTypeAnswer(String typeAnswer) {
        this.typeAnswer = typeAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pertanyaan)) return false;
        Pertanyaan that = (Pertanyaan) o;
        return getIdPertanyaan().equals(that.getIdPertanyaan());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdPertanyaan());
    }

    public static class Jawaban{
        @SerializedName("id_jawaban") private Long idJawaban;
        @SerializedName("jawaban") private String jawaban;

        public Long getIdJawaban() {
            return idJawaban;
        }

        public void setIdJawaban(Long idJawaban) {
            this.idJawaban = idJawaban;
        }

        public String getJawaban() {
            return jawaban;
        }

        public void setJawaban(String jawaban) {
            this.jawaban = jawaban;
        }

        @Override
        public String toString() {
            return  jawaban;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Jawaban)) return false;
            Jawaban jawaban = (Jawaban) o;
            return getIdJawaban().equals(jawaban.getIdJawaban());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getIdJawaban());
        }
    }


}
