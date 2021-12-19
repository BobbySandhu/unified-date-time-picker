package com.bobgenix.datetimedialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

public class UnifiedDateTimePicker {

    private final Context context;
    private final String title;
    private final int backgroundColor;
    private final int buttonColor;
    private final int dateTimeTextColor;
    private final int titleTextColor;
    private final int buttonTextColor;
    private final Typeface titleTypeface;
    private final Typeface buttonTypeface;
    private final Boolean enableVibration;
    private final int titleTextSize;
    private final int buttonTextSize;
    private final OnDateTimeSelectedListener onDateTimeSelected;
    private long millis = 0l;

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
        this.millis = builder.millis;
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

    public long getDateTimeMillis() {
        return this.millis;
    }

    public static class Builder {

        private String LOG_TAG = "UnifiedDateTimePicker";

        private final Context context;
        private String title = "Select Date and Time";
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
        private long millis = 0L;

        public Builder(Context context) {
            this.context = context;
            backgroundColor = Color.WHITE;
            buttonColor = Color.BLUE;
            dateTimeTextColor = Color.BLACK;

            titleTextColor = Color.BLACK;
            buttonTextColor = Color.WHITE;

            Typeface defaultTypeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
            titleTypeface = defaultTypeface;
            buttonTypeface = defaultTypeface;
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

        public Builder setDateTimeMillis(long milliSeconds) {
            millis = milliSeconds;
            return this;
        }

        private UnifiedDateTimePicker build() {
            return new UnifiedDateTimePicker(this);
        }

        public void show() {
            if (context != null) {
                UnifiedDateTimePicker unifiedDateTimePicker = build();
                new UnifiedDateTimePickerHelper(unifiedDateTimePicker).createDatePickerDialog();
            } else {
                Log.d(LOG_TAG, "context can not be null.");
            }
        }
    }
}
