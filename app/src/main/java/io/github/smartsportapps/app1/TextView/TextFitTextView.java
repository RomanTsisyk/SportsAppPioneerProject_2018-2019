package io.github.smartsportapps.app1.TextView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextFitTextView extends TextView {

    boolean fit = false;

    public TextFitTextView(Context context) {
        super(context);
    }

    public TextFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextFitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFitTextToBox(Boolean fit) {
        this.fit = fit;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fit) {
            shrinkToFit();
        }
    }

    protected void shrinkToFit() {
        int height = this.getHeight();
        int lines = this.getLineCount();
        Rect r = new Rect();
        int y1 = this.getLineBounds(0, r);
        int y2 = this.getLineBounds(lines - 1, r);

        float size = this.getTextSize();
        if (y2 > height && size >= 8.0f) {
            this.setTextSize(size - 2.0f);
            shrinkToFit();
        }
    }
}
