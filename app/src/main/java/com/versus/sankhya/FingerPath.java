package com.versus.sankhya;

import android.graphics.Path;

class FingerPath {
    int color;
    boolean emboss;
    boolean blur;
    int strokeWidth;
    Path path;

    FingerPath(int color, boolean emboss, boolean blur, int strokeWidth, Path path) {
        this.color = color;
        this.emboss = emboss;
        this.blur = blur;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}