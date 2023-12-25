package io.github.smartsportapps.app1.TextView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import io.github.smartsportapps.app1.helper.CustomFontRobotoHelper;

public class RobotoTextView extends TextView {

    public RobotoTextView(Context context) {
        super(context);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontRobotoHelper.setCustomFont(this, context, attrs);
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontRobotoHelper.setCustomFont(this, context, attrs);
    }
}
