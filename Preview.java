package com.lj.shcode.snake.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/4/28.
 */
public class Preview extends View {
    public Preview(Context context) {
        super(context);
    }

    public Preview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Preview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth()*0.8f;
        float height = width*0.75f;
        int posx = (int) ((getWidth()-width)/2.0);
        int posy = (int)((getHeight() - height)/2.0);
        Rect r = new Rect(posx, posy, (int)(posx+width), (int)(posy+height));


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#00ff00"));
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(r,paint);
    }
}
