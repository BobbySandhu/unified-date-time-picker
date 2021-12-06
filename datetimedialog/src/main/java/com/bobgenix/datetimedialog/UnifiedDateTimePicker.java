package com.bobgenix.datetimedialog;

import android.content.Context;
import android.graphics.Typeface;

public class UnifiedDateTimePicker {

    private Context context;
    private String title;
    private int backgroundColor;
    private int buttonColor;
    private int dateTimeTextColor;
    private int titleTextColor;
    private int buttonTextColor;
    private Typeface titleTypeface;
    private Typeface buttonTypeface;
    private Boolean enableVibration;
    private int titleTextSize = 20;
    private int buttonTextSize = 14;
    private OnDateTimeSelectedListener onDateTimeSelected;

    private UnifiedDateTimePicker(Builder builder) {
        this.context = builder.context;
        this.title = builder.title;
        this.backgroundColor = builder.backgroundColor;
        this.buttonColor = builder.buttonColor;
        this.dateTimeTextColor = builder.dateTimeTextColor;
        this.titleTextColor = builder.titleTextColor;
        this.buttonTextColor = builder.buttonTextColor;
        this.titleTypeface = builder.titleTypeface;
        this.buttonTypeface = builder.buttonTypeface;
        this.enableVibration = builder.enableVibration;
        this.titleTextSize = builder.titleTextSize;
        this.buttonTextSize = builder.buttonTextSize;
        this.onDateTimeSelected = builder.onDateTimeSelected;
    }

    public Context getContext() {
        return this.context;
    }

    public String getTitle() {
        return this.title;
    }

    public Typeface getTitleFont() {
        return this.titleTypeface;
    }

    public Typeface getButtonFont() {
        return this.buttonTypeface;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public int getButtonColor() {
        return this.buttonColor;
    }

    public int getDateTimeTextColor() {
        return this.dateTimeTextColor;
    }

    public int getTitleTextColor() {
        return this.titleTextColor;
    }

    public int getButtonTextColor() {
        return this.buttonTextColor;
    }

    public Boolean getVibration() {
        return this.enableVibration;
    }

    public int getTextSizeTitle() {
        return this.titleTextSize;
    }

    public int getTextSizeButton() {
        return this.buttonTextSize;
    }

    public OnDateTimeSelectedListener getListener() {
        return this.onDateTimeSelected;
    }

    public static class Builder {

        private Context context;
        private String title;
        private int backgroundColor;
        private int buttonColor;
        private int dateTimeTextColor;
        private int titleTextColor;
        private int buttonTextColor;
        private Typeface titleTypeface;
        private Typeface buttonTypeface;
        private Boolean enableVibration;
        private int titleTextSize = 20;
        private int buttonTextSize = 14;
        private OnDateTimeSelectedListener onDateTimeSelected;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitleFont(Typeface typeface) {
            titleTypeface = typeface;
            return this;
        }

        public Builder setButtonFont(Typeface typeface) {
            buttonTypeface = typeface;
            return this;
        }

        public Builder backgroundColor(int color) {
            backgroundColor = color;
            return this;
        }

        public Builder buttonColor(int color) {
            buttonColor = color;
            return this;
        }

        public Builder dateTimeTextColor(int color) {
            dateTimeTextColor = color;
            return this;
        }

        public Builder titleTextColor(int color) {
            titleTextColor = color;
            return this;
        }

        public Builder buttonTextColor(int color) {
            buttonTextColor = color;
            return this;
        }

        public Builder vibration(Boolean enable) {
            enableVibration = enable;
            return this;
        }

        public Builder textSizeTitle(int size) {
            titleTextSize = AndroidUtilities.dp(size);
            return this;
        }

        public Builder textSizeButton(int size) {
            buttonTextSize = AndroidUtilities.dp(size);
            return this;
        }


        public Builder addListener(OnDateTimeSelectedListener listener) {
            onDateTimeSelected = listener;
            return this;
        }

        private UnifiedDateTimePicker build() {
            return new UnifiedDateTimePicker(this);
        }

        public void show() {
            UnifiedDateTimePicker unifiedDateTimePicker = build();
            new UnifiedDateTimePickerHelper(unifiedDateTimePicker).createDatePickerDialog();
        }
    }
}
