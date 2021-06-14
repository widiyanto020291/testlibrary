package com.transmedika.transmedikakitui.models.bean.json.param;

import com.google.gson.annotations.SerializedName;

public class PinParam {

    @SerializedName("pin")
    private final String pin;

    @SerializedName("new_pin")
    private final String newPin;

    @SerializedName("old_pin")
    private final String oldPin;

    public PinParam(String pin) {
        this(pin, null, null);
    }

    public PinParam(String oldPin, String newPin) {
        this(null, oldPin, newPin);
    }

    private PinParam(String pin, String oldPin, String newPin) {
        this.pin = pin;
        this.oldPin = oldPin;
        this.newPin = newPin;
    }

    public String getPin() {
        return pin;
    }

    public String getOldPin() {
        return oldPin;
    }

    public String getNewPin() {
        return newPin;
    }
}
