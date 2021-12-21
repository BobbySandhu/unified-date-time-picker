package com.bobgenix.datetimedialog;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import java.util.Calendar;
import java.util.Locale;

public class UnifiedDateTimePickerHelper {

    private Context context;
    private UnifiedDateTimePicker unifiedDateTimePicker;
    private long currentDate = -1;

    UnifiedDateTimePickerHelper(UnifiedDateTimePicker unifiedDateTimePicker) {
        this.context = unifiedDateTimePicker.getContext();
        this.unifiedDateTimePicker = unifiedDateTimePicker;
    }

    private Locale locale = new Locale("en");
    private FastDateFormat formatterScheduleDay = createFormatter(locale, "MMM d", "MMM d");
    private FastDateFormat formatterScheduleYear = createFormatter(locale, "MMM d yyyy", "MMM d yyyy");
    private FastDateFormat[] formatterScheduleSend = new FastDateFormat[3];

    public BottomSheet.Builder createDatePickerDialog() {
        if (context == null) {
            return null;
        }

        AndroidUtilities.checkDisplaySize(context, context.getResources().getConfiguration());

        formatterScheduleSend[0] = createFormatter(locale, "'Send today at' HH:mm", "'Send today at' HH:mm");
        formatterScheduleSend[1] = createFormatter(locale, "'Send on' MMM d 'at' HH:mm", "'Send on' MMM d 'at' HH:mm");
        formatterScheduleSend[2] = createFormatter(locale, "'Send on' MMM d yyyy 'at' HH:mm", "'Send on' MMM d yyyy 'at' HH:mm");

        BottomSheet.Builder builder = new BottomSheet.Builder(context, false);
        builder.setApplyBottomPadding(false);

        final NumberPicker dayPicker = new NumberPicker(context);
        dayPicker.setTextColor(context.getResources().getColor(unifiedDateTimePicker.getDateTimeTextColor()));
        dayPicker.setTextOffset(AndroidUtilities.dp(10));
        dayPicker.setItemCount(5);
        dayPicker.setSelectorColor(unifiedDateTimePicker.getButtonColor());

        final NumberPicker hourPicker = new NumberPicker(context) {
            @Override
            protected CharSequence getContentDescription(int value) {
                return "Hours";
            }
        };
        hourPicker.setItemCount(5);
        hourPicker.setTextColor(context.getResources().getColor(unifiedDateTimePicker.getDateTimeTextColor()));
        hourPicker.setTextOffset(-AndroidUtilities.dp(10));
        hourPicker.setSelectorColor(unifiedDateTimePicker.getButtonColor());

        final NumberPicker minutePicker = new NumberPicker(context) {
            @Override
            protected CharSequence getContentDescription(int value) {
                return "Minutes";
            }
        };
        minutePicker.setItemCount(5);
        minutePicker.setTextColor(context.getResources().getColor(unifiedDateTimePicker.getDateTimeTextColor()));
        minutePicker.setTextOffset(-AndroidUtilities.dp(34));
        minutePicker.setSelectorColor(unifiedDateTimePicker.getButtonColor());

        LinearLayout container = new LinearLayout(context) {

            boolean ignoreLayout = false;

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                ignoreLayout = true;
                int count;
                if (AndroidUtilities.displaySize.x > AndroidUtilities.displaySize.y) {
                    count = 3;
                } else {
                    count = 5;
                }
                dayPicker.setItemCount(count);
                hourPicker.setItemCount(count);
                minutePicker.setItemCount(count);
                dayPicker.getLayoutParams().height = AndroidUtilities.dp(54) * count;
                hourPicker.getLayoutParams().height = AndroidUtilities.dp(54) * count;
                minutePicker.getLayoutParams().height = AndroidUtilities.dp(54) * count;
                ignoreLayout = false;
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }

            @Override
            public void requestLayout() {
                if (ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        container.setOrientation(LinearLayout.VERTICAL);

        FrameLayout titleLayout = new FrameLayout(context);
        container.addView(titleLayout, createLinear(MATCH_PARENT, WRAP_CONTENT, Gravity.START | Gravity.TOP, 22, 0, 0, 4));

        TextView titleView = new TextView(context);
        titleView.setText(unifiedDateTimePicker.getTitle());
        titleView.setTextColor(context.getResources().getColor(unifiedDateTimePicker.getTitleTextColor()));
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, unifiedDateTimePicker.getTextSizeTitle());
        titleView.setTypeface(unifiedDateTimePicker.getTitleFont());
        titleLayout.addView(titleView, createFrame(WRAP_CONTENT, WRAP_CONTENT, Gravity.START | Gravity.TOP, 0, 12, 0, 0));
        titleView.setOnTouchListener((v, event) -> true);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(1.0f);
        container.addView(linearLayout, createLinear(MATCH_PARENT, WRAP_CONTENT));

        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        int currentYear = calendar.get(Calendar.YEAR);

        TextView buttonTextView = new AppCompatTextView(context) {
            @Override
            public CharSequence getAccessibilityClassName() {
                return Button.class.getName();
            }
        };

        linearLayout.addView(dayPicker, createLinear(0, 54 * 5, 0.5f));

        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(365);
        dayPicker.setWrapSelectorWheel(false);
        dayPicker.setFormatter(value -> {
            if (value == 0) {
                return "Today";
            } else {
                long date = currentTime + (long) value * 86400000L;
                calendar.setTimeInMillis(date);
                int year = calendar.get(Calendar.YEAR);
                if (year == currentYear) {
                    return formatterScheduleDay.format(date);
                } else {
                    return formatterScheduleYear.format(date);
                }
            }
        });

        final NumberPicker.OnValueChangeListener onValueChangeListener = (picker, oldVal, newVal) -> {
            try {
                if (unifiedDateTimePicker.getVibration()) {
                    container.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                }
            } catch (Exception ignore) {

            }
            checkScheduleDate(buttonTextView, null, 0, dayPicker, hourPicker, minutePicker);
        };

        dayPicker.setOnValueChangedListener(onValueChangeListener);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        linearLayout.addView(hourPicker, createLinear(0, 54 * 5, 0.2f));
        hourPicker.setFormatter(value -> String.format("%02d", value));
        hourPicker.setOnValueChangedListener(onValueChangeListener);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setValue(0);
        minutePicker.setFormatter(value -> String.format("%02d", value));
        linearLayout.addView(minutePicker, createLinear(0, 54 * 5, 0.3f));
        minutePicker.setOnValueChangedListener(onValueChangeListener);

        if (unifiedDateTimePicker.getDateTimeMillis() > 0L && unifiedDateTimePicker.getDateTimeMillis() > currentTime) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            int days = (int) ((unifiedDateTimePicker.getDateTimeMillis() - currentTime) / (24 * 60 * 60 * 1000));
            calendar.setTimeInMillis(unifiedDateTimePicker.getDateTimeMillis());
            if (days >= 0) {
                minutePicker.setValue(calendar.get(Calendar.MINUTE));
                hourPicker.setValue(calendar.get(Calendar.HOUR_OF_DAY));
                dayPicker.setValue(days);
            }
        }

        final boolean[] canceled = {true};

        checkScheduleDate(buttonTextView, null, 0, dayPicker, hourPicker, minutePicker);

        buttonTextView.setPadding(AndroidUtilities.dp(34), 0, AndroidUtilities.dp(34), 0);
        buttonTextView.setGravity(Gravity.CENTER);
        buttonTextView.setTextColor(context.getResources().getColor(unifiedDateTimePicker.getButtonTextColor()));
        buttonTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, unifiedDateTimePicker.getTextSizeButton());
        buttonTextView.setTypeface(unifiedDateTimePicker.getButtonFont());
        buttonTextView.setBackground(createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4), unifiedDateTimePicker.getButtonColor(), Color.GRAY));
        container.addView(buttonTextView, createLinear(MATCH_PARENT, 48, Gravity.START | Gravity.BOTTOM, 16, 15, 16, 16));

        buttonTextView.setOnClickListener(v -> {
            canceled[0] = false;
            boolean setSeconds = checkScheduleDate(null, null, 0, dayPicker, hourPicker, minutePicker);
            calendar.setTimeInMillis(System.currentTimeMillis() + (long) dayPicker.getValue() * 24 * 3600 * 1000);
            calendar.set(Calendar.HOUR_OF_DAY, hourPicker.getValue());
            calendar.set(Calendar.MINUTE, minutePicker.getValue());

            if (setSeconds) {
                calendar.set(Calendar.SECOND, 0);
            }

            unifiedDateTimePicker.getListener().onDateTimeSelected(calendar.getTimeInMillis());
            builder.getDismissRunnable().run();
        });

        builder.setCustomView(container);
        BottomSheet bottomSheet = builder.show();
        bottomSheet.setOnDismissListener(dialog -> {
            unifiedDateTimePicker.getListener().onPickerDismissed(calendar.getTimeInMillis());
        });

        bottomSheet.setBackgroundColor(unifiedDateTimePicker.getBackgroundColor());

        return builder;
    }

    private LinearLayout.LayoutParams createLinear(int width, int height, int gravity, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getSize(width), getSize(height));
        layoutParams.setMargins(AndroidUtilities.dp(leftMargin), AndroidUtilities.dp(topMargin), AndroidUtilities.dp(rightMargin), AndroidUtilities.dp(bottomMargin));
        layoutParams.gravity = gravity;
        return layoutParams;
    }

    private FrameLayout.LayoutParams createFrame(int width, float height, int gravity, float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getSize(width), getSize(height), gravity);
        layoutParams.setMargins(AndroidUtilities.dp(leftMargin), AndroidUtilities.dp(topMargin), AndroidUtilities.dp(rightMargin), AndroidUtilities.dp(bottomMargin));
        return layoutParams;
    }

    private int getSize(float size) {
        return (int) (size < 0 ? size : AndroidUtilities.dp(size));
    }

    private FastDateFormat createFormatter(Locale locale, String format, String defaultFormat) {
        if (format == null || format.length() == 0) {
            format = defaultFormat;
        }
        FastDateFormat formatter;
        try {
            formatter = FastDateFormat.getInstance(format, locale);
        } catch (Exception e) {
            format = defaultFormat;
            formatter = FastDateFormat.getInstance(format, locale);
        }
        return formatter;
    }

    public boolean checkScheduleDate(TextView button, TextView infoText, int type, NumberPicker dayPicker, NumberPicker hourPicker, NumberPicker minutePicker) {
        return checkScheduleDate(button, infoText, 0, type, dayPicker, hourPicker, minutePicker);
    }

    private boolean checkScheduleDate(TextView button, TextView infoText, long maxDate, int type, NumberPicker dayPicker, NumberPicker hourPicker, NumberPicker minutePicker) {
        int day = dayPicker.getValue();
        int hour = hourPicker.getValue();
        int minute = minutePicker.getValue();
        Calendar calendar = Calendar.getInstance();

        long systemTime = System.currentTimeMillis();
        calendar.setTimeInMillis(systemTime);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
        if (maxDate > 0) {
            maxDate *= 1000;
            calendar.setTimeInMillis(systemTime + maxDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            maxDate = calendar.getTimeInMillis();
        }

        calendar.setTimeInMillis(System.currentTimeMillis() + (long) day * 24 * 3600 * 1000);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        long currentTime = calendar.getTimeInMillis();

        if (currentTime <= systemTime + 60000L) {
            calendar.setTimeInMillis(systemTime + 60000L);

            if (currentDay != calendar.get(Calendar.DAY_OF_YEAR)) {
                dayPicker.setValue(day = 1);
            }
            hourPicker.setValue(hour = calendar.get(Calendar.HOUR_OF_DAY));
            minutePicker.setValue(minute = calendar.get(Calendar.MINUTE));
        } else if (maxDate > 0 && currentTime > maxDate) {
            calendar.setTimeInMillis(maxDate);

            dayPicker.setValue(day = 7);
            hourPicker.setValue(hour = calendar.get(Calendar.HOUR_OF_DAY));
            minutePicker.setValue(minute = calendar.get(Calendar.MINUTE));
        }
        int selectedYear = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(System.currentTimeMillis() + (long) day * 24 * 3600 * 1000);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        long time = calendar.getTimeInMillis();
        if (button != null) {
            int num;
            if (day == 0) {
                num = 0;
            } else if (currentYear == selectedYear) {
                num = 1;
            } else {
                num = 2;
            }
            if (type == 1) {
                num += 3;
            } else if (type == 2) {
                num += 6;
            } else if (type == 3) {
                num += 9;
            }
            button.setText(formatterScheduleSend[num].format(time));
        }
        if (infoText != null) {
            int diff = (int) ((time - systemTime) / 1000);
            String t;
            if (diff > 24 * 60 * 60) {
                t = "Days " + Math.round(diff / (24 * 60 * 60.0f));
            } else if (diff >= 60 * 60) {
                t = "Hours " + Math.round(diff / (60 * 60.0f));
            } else if (diff >= 60) {
                t = "Minutes " + Math.round(diff / 60.0f);
            } else {
                t = "Seconds" + diff;
            }
        }
        return currentTime - systemTime > 60000L;
    }

    private LinearLayout.LayoutParams createLinear(int width, int height, float weight) {
        return new LinearLayout.LayoutParams(getSize(width), getSize(height), weight);
    }

    private LinearLayout.LayoutParams createLinear(int width, int height) {
        return new LinearLayout.LayoutParams(getSize(width), getSize(height));
    }

    private Drawable createSimpleSelectorRoundRectDrawable(int rad, int defaultColor, int pressedColor) {
        return createSimpleSelectorRoundRectDrawable(rad, defaultColor, pressedColor, pressedColor);
    }

    private Drawable createSimpleSelectorRoundRectDrawable(int rad, int defaultColor, int pressedColor, int maskColor) {
        ShapeDrawable defaultDrawable = new ShapeDrawable(new RoundRectShape(new float[]{rad, rad, rad, rad, rad, rad, rad, rad}, null, null));
        defaultDrawable.getPaint().setColor(context.getResources().getColor(defaultColor));
        ShapeDrawable pressedDrawable = new ShapeDrawable(new RoundRectShape(new float[]{rad, rad, rad, rad, rad, rad, rad, rad}, null, null));
        pressedDrawable.getPaint().setColor(maskColor);

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{StateSet.WILD_CARD},
                new int[]{pressedColor}
        );
        return new RippleDrawable(colorStateList, defaultDrawable, pressedDrawable);
    }
}
