package com.rafal.pathrecall.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.rafal.pathrecall.R;


public class StatusInfoBackgroundView extends View {

    public StatusInfoBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StatusInfoBackgroundView(Context context) {
        super(context);
    }

    public StatusInfoBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.secondary_accent_color));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);

        Path path = new Path();
        path.moveTo(0, height/2);
        path.lineTo(height / 3, 0);
        path.lineTo(width - height / 3, 0);
        path.lineTo(width, height / 2);
        path.lineTo(width - height / 3, height);
        path.lineTo(height / 3, height);
        path.lineTo(0, height/2);
        path.close();

        canvas.drawPath(path, paint);

        paint.setColor(getResources().getColor(R.color.main_color_contrasted));
        paint.setStyle(Paint.Style.STROKE);
        int dpSize =  2;
        DisplayMetrics dm = getResources().getDisplayMetrics() ;
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
        paint.setStrokeWidth(strokeWidth);

        canvas.drawPath(path, paint);
    }
}
