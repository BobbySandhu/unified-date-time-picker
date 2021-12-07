package com.bobgenix.datetimedialog;

public interface OnDateTimeSelectedListener {
    void onDateTimeSelected(long millis);
    void onPickerDismissed(long millis);
}
