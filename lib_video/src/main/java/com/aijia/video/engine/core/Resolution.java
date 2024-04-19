package com.aijia.video.engine.core;

import android.text.TextUtils;

public enum Resolution {
    Undefine("", ""),
    Standard("360p", "medium"),
    High("480p", "higher"),
    SuperHigh("720p", "highest"),
    ExtremelyHigh("1080p", "original"),
    FourK("4k", "lossless"),
    HDR("hdr", ""),
    Auto("auto", ""),
    L_Standard("240p", ""),
    H_High("540p", ""),
    TwoK("2k", ""),
    ExtremelyHigh_50F("1080p 50fps", ""),
    TwoK_50F("2k 50fps", ""),
    FourK_50F("4k 50fps", ""),
    ExtremelyHigh_60F("1080p 60fps", ""),
    TwoK_60F("2k 60fps", ""),
    FourK_60F("4k 60fps", ""),
    ExtremelyHigh_120F("1080p 120fps", ""),
    TwoK_120F("2k 120fps", ""),
    FourK_120F("4k 120fps", ""),
    L_Standard_HDR("240p HDR", ""),
    Standard_HDR("360p HDR", ""),
    High_HDR("480p HDR", ""),
    H_High_HDR("540p HDR", ""),
    SuperHigh_HDR("720p HDR", ""),
    ExtremelyHigh_HDR("1080p HDR", ""),
    TwoK_HDR("2k HDR", ""),
    FourK_HDR("4k HDR", ""),
    EightK("8k", ""),
    ExtremelyHighPlus("1080p+", "");

    private final String resolution;
    private final String audioquality;
    public static final int RESOLUTION_VIDEO = 0;
    public static final int RESOLUTION_AUDIO = 1;

    private Resolution(String resolution, String audioquality) {
        this.resolution = resolution;
        this.audioquality = audioquality;
    }

    @Deprecated
    public String toString() {
        return "audio quality: "+this.audioquality+" ,video resolution: "+this.resolution;
    }

    public int getIndex() {
        return this.ordinal();
    }

    public static Resolution valueOf(int resolutionIndex) {
        return resolutionIndex >= Undefine.ordinal() && resolutionIndex <= FourK_HDR.ordinal() ? values()[resolutionIndex] : Undefine;
    }

    public static Resolution[] getAllResolutions() {
        Resolution[] tmp = null;

        try {
            tmp = new Resolution[]{Undefine, L_Standard, Standard, High, H_High, SuperHigh, ExtremelyHigh, ExtremelyHighPlus, ExtremelyHigh_50F, ExtremelyHigh_60F, ExtremelyHigh_120F, HDR, TwoK, TwoK_50F, TwoK_60F, TwoK_120F, FourK, FourK_50F, FourK_60F, FourK_120F, L_Standard_HDR, Standard_HDR, High_HDR, H_High_HDR, SuperHigh_HDR, ExtremelyHigh_HDR, TwoK_HDR, FourK_HDR, EightK};
        } catch (Exception var2) {
            tmp = new Resolution[0];
        }

        return tmp;
    }

    public static Resolution forString(String str) {
        if (TextUtils.isEmpty(str)) {
            return Undefine;
        } else if (str.equals("Undefine")) {
            return Undefine;
        } else if (str.equals("Standard")) {
            return Standard;
        } else if (str.equals("High")) {
            return High;
        } else if (str.equals("SuperHigh")) {
            return SuperHigh;
        } else if (str.equals("FourK")) {
            return FourK;
        } else if (str.equals("HDR")) {
            return HDR;
        } else if (str.equals("Auto")) {
            return Auto;
        } else if (str.equals("L_Standard")) {
            return L_Standard;
        } else if (str.equals("H_High")) {
            return H_High;
        } else if (str.equals("TwoK")) {
            return TwoK;
        } else if (str.equals("ExtremelyHigh_50F")) {
            return ExtremelyHigh_50F;
        } else if (str.equals("TwoK_50F")) {
            return TwoK_50F;
        } else if (str.equals("FourK_50F")) {
            return FourK_50F;
        } else if (str.equals("ExtremelyHigh_60F")) {
            return ExtremelyHigh_60F;
        } else if (str.equals("TwoK_60F")) {
            return TwoK_60F;
        } else if (str.equals("FourK_60F")) {
            return FourK_60F;
        } else if (str.equals("ExtremelyHigh_120F")) {
            return ExtremelyHigh_120F;
        } else if (str.equals("TwoK_120F")) {
            return TwoK_120F;
        } else if (str.equals("FourK_120F")) {
            return FourK_120F;
        } else if (str.equals("L_Standard_HDR")) {
            return L_Standard_HDR;
        } else if (str.equals("Standard_HDR")) {
            return Standard_HDR;
        } else if (str.equals("High_HDR")) {
            return High_HDR;
        } else if (str.equals("H_High_HDR")) {
            return H_High_HDR;
        } else if (str.equals("SuperHigh_HDR")) {
            return SuperHigh_HDR;
        } else if (str.equals("ExtremelyHigh_HDR")) {
            return ExtremelyHigh_HDR;
        } else if (str.equals("TwoK_HDR")) {
            return TwoK_HDR;
        } else if (str.equals("FourK_HDR")) {
            return FourK_HDR;
        } else if (str.equals("EightK")) {
            return EightK;
        } else {
            return str.equals("1080p+") ? ExtremelyHighPlus : Undefine;
        }
    }

    public static String toString(Resolution resolution) {
        if (resolution == null) {
            return "Undefine";
        } else if (resolution == Undefine) {
            return "Undefine";
        } else if (resolution == Standard) {
            return "Standard";
        } else if (resolution == High) {
            return "High";
        } else if (resolution == SuperHigh) {
            return "SuperHigh";
        } else if (resolution == FourK) {
            return "FourK";
        } else if (resolution == HDR) {
            return "HDR";
        } else if (resolution == Auto) {
            return "Auto";
        } else if (resolution == L_Standard) {
            return "L_Standard";
        } else if (resolution == H_High) {
            return "H_High";
        } else if (resolution == TwoK) {
            return "TwoK";
        } else if (resolution == ExtremelyHigh_50F) {
            return "ExtremelyHigh_50F";
        } else if (resolution == TwoK_50F) {
            return "TwoK_50F";
        } else if (resolution == FourK_50F) {
            return "FourK_50F";
        } else if (resolution == ExtremelyHigh_60F) {
            return "ExtremelyHigh_60F";
        } else if (resolution == TwoK_60F) {
            return "TwoK_60F";
        } else if (resolution == FourK_60F) {
            return "FourK_60F";
        } else if (resolution == ExtremelyHigh_120F) {
            return "ExtremelyHigh_120F";
        } else if (resolution == TwoK_120F) {
            return "TwoK_120F";
        } else if (resolution == FourK_120F) {
            return "FourK_120F";
        } else if (resolution == L_Standard_HDR) {
            return "L_Standard_HDR";
        } else if (resolution == Standard_HDR) {
            return "Standard_HDR";
        } else if (resolution == High_HDR) {
            return "High_HDR";
        } else if (resolution == H_High_HDR) {
            return "H_High_HDR";
        } else if (resolution == SuperHigh_HDR) {
            return "SuperHigh_HDR";
        } else if (resolution == ExtremelyHigh_HDR) {
            return "ExtremelyHigh_HDR";
        } else if (resolution == TwoK_HDR) {
            return "TwoK_HDR";
        } else if (resolution == FourK_HDR) {
            return "FourK_HDR";
        } else if (resolution == EightK) {
            return "EightK";
        } else {
            return resolution == ExtremelyHighPlus ? "1080p+" : "Undefine";
        }
    }
}
