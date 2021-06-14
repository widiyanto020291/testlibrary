package com.transmedika.transmedikakitui.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransmedikaSettings {
    @SerializedName("applicationId")private String applicationId;
    @SerializedName("baseUrl")private String baseUrl;
    @SerializedName("parseHost")private String parseHost;
    @SerializedName("parseAppId")private String parseAppId;
    @SerializedName("parseClientId")private String parseClientId;
    @SerializedName("videoCallHost")private String videoCallHost;

    @SerializedName("backIcon")private String backIcon;
    @SerializedName("titleToolbarColor") private String titleToolbarColor;
    @SerializedName("toolbarCenterTitle") private Boolean toolbarCenterTitle;
    @SerializedName("toolbarColor") private String toolbarColor;

    @SerializedName("fontBlack")private String fontBlack;
    @SerializedName("fontBlackItalic")private String fontBlackItalic;
    @SerializedName("fontBold")private String fontBold;
    @SerializedName("fontBoldItalic")private String fontBoldItalic;
    @SerializedName("fontItalic")private String fontItalic;
    @SerializedName("fontLight")private String fontLight;
    @SerializedName("fontLightItalic")private String fontLightItalic;
    @SerializedName("fontMedium")private String fontMedium;
    @SerializedName("fontMediumItalic")private String fontMediumItalic;
    @SerializedName("fontRegular")private String fontRegular;
    @SerializedName("fontThin")private String fontThin;
    @SerializedName("fontThinItalic")private String fontThinItalic;
    @SerializedName("dialogResource")private List<String> dialogResource;

    @SerializedName("errorResource")private String errorResource;
    @SerializedName("emptyDataResource")private String emptyDataResource;

    @SerializedName("buttonPrimaryBackground")private String buttonPrimaryBackground;
    @SerializedName("buttonPrimaryRoundBackground")private String buttonPrimaryRoundBackground;
    @SerializedName("buttonPrimaryLightRoundBackground")private String buttonPrimaryLightRoundBackground;

    @SerializedName("toolbarDoctorMenuIcon")private String toolbarDoctorMenuIcon;
    @SerializedName("doctorItemButtonChatBackground")private String doctorItemButtonChatBackground;
    @SerializedName("doctorCenterSearchHint")private Boolean doctorCenterSearchHint;
    @SerializedName("doctorOnlineBackground")private String doctorOnlineBackground;
    @SerializedName("doctorBusyBackground")private String doctorBusyBackground;
    @SerializedName("doctorOfflineBackground")private String doctorOfflineBackground;
    @SerializedName("doctorSearchBackground")private String doctorSearchBackground;
    @SerializedName("doctorPriceIcon")private String doctorPriceIcon;
    @SerializedName("doctorStatusIcon")private String doctorStatusIcon;
    @SerializedName("doctorRateIcon")private String doctorRateIcon;
    @SerializedName("doctorExperienceIcon")private String doctorExperienceIcon;
    @SerializedName("doctorStrNumberIcon")private String doctorStrNumberIcon;
    @SerializedName("doctorAlumniIcon")private String doctorAlumniIcon;
    @SerializedName("doctorFacilityIcon")private String doctorFacilityIcon;
    @SerializedName("doctorScheduleIcon")private String doctorScheduleIcon;
    @SerializedName("doctorEditButtonBackground")private String doctorEditButtonBackground;
    @SerializedName("doctorEditButtonIcon")private String doctorEditButtonIcon;
    @SerializedName("pocketIcon")private String pocketIcon;
    @SerializedName("otherPaymentArrowIcon")private String otherPaymentArrowIcon;

    @SerializedName("waitingDoctorResource") private String waitingDoctorResource;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getParseHost() {
        return parseHost;
    }

    public String getParseAppId() {
        return parseAppId;
    }

    public String getParseClientId() {
        return parseClientId;
    }

    public String getBackIcon() {
        return backIcon;
    }

    public String getTitleToolbarColor() {
        return titleToolbarColor;
    }

    public Boolean getToolbarCenterTitle() {
        return toolbarCenterTitle;
    }

    public String getToolbarColor() {
        return toolbarColor;
    }

    public String getFontBlack() {
        return fontBlack;
    }

    public String getFontBlackItalic() {
        return fontBlackItalic;
    }

    public String getFontBold() {
        return fontBold;
    }

    public String getFontBoldItalic() {
        return fontBoldItalic;
    }

    public String getFontItalic() {
        return fontItalic;
    }

    public String getFontLight() {
        return fontLight;
    }

    public String getFontLightItalic() {
        return fontLightItalic;
    }

    public String getFontMedium() {
        return fontMedium;
    }

    public String getFontMediumItalic() {
        return fontMediumItalic;
    }

    public String getFontRegular() {
        return fontRegular;
    }

    public String getFontThin() {
        return fontThin;
    }

    public String getFontThinItalic() {
        return fontThinItalic;
    }

    public List<String> getDialogResource() {
        return dialogResource;
    }

    public String getErrorResource() {
        return errorResource;
    }

    public String getEmptyDataResource() {
        return emptyDataResource;
    }

    public String getToolbarDoctorMenuIcon() {
        return toolbarDoctorMenuIcon;
    }

    public Boolean getDoctorCenterSearchHint() {
        return doctorCenterSearchHint;
    }

    public String getDoctorBusyBackground() {
        return doctorBusyBackground;
    }

    public String getDoctorOnlineBackground() {
        return doctorOnlineBackground;
    }

    public String getDoctorOfflineBackground() {
        return doctorOfflineBackground;
    }

    public String getDoctorSearchBackground() {
        return doctorSearchBackground;
    }

    public String getDoctorPriceIcon() {
        return doctorPriceIcon;
    }

    public String getDoctorStatusIcon() {
        return doctorStatusIcon;
    }

    public String getDoctorRateIcon() {
        return doctorRateIcon;
    }

    public String getDoctorExperienceIcon() {
        return doctorExperienceIcon;
    }

    public String getDoctorStrNumberIcon() {
        return doctorStrNumberIcon;
    }

    public String getDoctorAlumniIcon() {
        return doctorAlumniIcon;
    }

    public String getDoctorFacilityIcon() {
        return doctorFacilityIcon;
    }

    public String getDoctorScheduleIcon() {
        return doctorScheduleIcon;
    }

    public String getDoctorItemButtonChatBackground() {
        return doctorItemButtonChatBackground;
    }

    public String getButtonPrimaryBackground() {
        return buttonPrimaryBackground;
    }

    public String getDoctorEditButtonBackground() {
        return doctorEditButtonBackground;
    }

    public String getDoctorEditButtonIcon() {
        return doctorEditButtonIcon;
    }

    public String getPocketIcon() {
        return pocketIcon;
    }

    public String getOtherPaymentArrowIcon() {
        return otherPaymentArrowIcon;
    }

    public String getWaitingDoctorResource() {
        return waitingDoctorResource;
    }

    public String getButtonPrimaryLightRoundBackground() {
        return buttonPrimaryLightRoundBackground;
    }

    public String getButtonPrimaryRoundBackground() {
        return buttonPrimaryRoundBackground;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getVideoCallHost() {
        return videoCallHost;
    }
}
