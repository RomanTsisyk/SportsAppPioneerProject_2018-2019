package io.github.smartsportapps.app1.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import io.github.smartsportapps.app1.R;
import io.github.smartsportapps.app1.TextView.RobotoFont;

public class CustomFontRobotoHelper {

    public static void setCustomFont(TextView textView, Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RobotoFont);
        String fontName = typedArray.getString(R.styleable.RobotoFont_fontRoboto);
        setCustomFont(textView, fontName, context);
        typedArray.recycle();
    }

    public static void setCustomFont(TextView textView, String fontName, Context context) {
        if (fontName == null) {
            return;
        }
        Typeface typeface = RobotoFont.get(fontName, context);
        if (typeface != null) {
            textView.setTypeface(typeface);
        }
    }
}
