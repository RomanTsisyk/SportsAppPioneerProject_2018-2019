package io.github.smartsportapps.app1.TextView;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class TopAlignedTextView extends TextView {

    public TopAlignedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopAlignedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        setIncludeFontPadding(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();
        canvas.save();

        int additionalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getContext().getResources().getDisplayMetrics());

        canvas.translate(0, -additionalPadding);
        if (getLayout() != null)
            getLayout().draw(canvas);
        canvas.restore();
    }
}
