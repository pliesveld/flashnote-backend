package com.pliesveld.flashnote.web.validator;

public class ImageMetaData
{
    final private String formatName;
    final private int width;
    final private int height;
    final private float aspectRatio;

    ImageMetaData(String formatName, int width, int height, float aspectRatio) {
        this.formatName = formatName;
        this.width = width;
        this.height = height;
        this.aspectRatio = aspectRatio;
    }

    public String getFormatName() {
        return formatName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public String toString() {
        return "ImageMetaData{" +
                "formatName='" + formatName + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", aspectRatio=" + aspectRatio +
                '}';
    }
}
