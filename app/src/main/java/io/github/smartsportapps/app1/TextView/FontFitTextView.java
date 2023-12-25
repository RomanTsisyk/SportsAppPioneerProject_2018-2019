package io.github.smartsportapps.app1.TextView;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class FontFitTextView extends TextView {

    private Paint mTestPaint;
    private float mDefaultTextSize;

    public FontFitTextView(final Context context) {
        super(context);
        initialize();
    }

    public FontFitTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        mDefaultTextSize = getTextSize();
    }

    private void refitText(final String text, final int textWidth) {
        if (textWidth <= 0 || text.isEmpty()) {
            return;
        }

        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();

        if (targetWidth <= 2) {
            return;
        }

        mTestPaint.set(this.getPaint());
        mTestPaint.setTextSize(mDefaultTextSize);

        float hi = Math.max(mDefaultTextSize, targetWidth);
        float lo = 2;
        final float threshold = 0.5f;

        while (hi - lo > threshold) {
            float size = (hi + lo) / 2;
            mTestPaint.setTextSize(size);
            if (mTestPaint.measureText(text) >= targetWidth) {
                hi = size;
            } else {
                lo = size;
            }
        }

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();
        refitText(this.getText().toString(), parentWidth);
        this.setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
                                 final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        if (w != oldw || h != oldh) {
            refitText(this.getText().toString(), w);
        }
    }
}
