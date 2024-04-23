package com.quocanle.pianoapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quocanle.pianoapp.model.Key;

import java.util.ArrayList;

public class PianoView extends View {
    public static final int NUMBER_OF_KEYS = 14;
    private ArrayList<Key> whites;
    private ArrayList<Key> blacks;
    private int keyWidth, keyHeight;
    Paint blackPen, whitePen;

    int blackCount = 15;
    public PianoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        whites = new ArrayList<>();
        blacks = new ArrayList<>();

        whitePen = new Paint();
        whitePen.setColor(Color.WHITE);
        whitePen.setStyle(Paint.Style.FILL);

        blackPen = new Paint();
        blackPen.setColor(Color.BLACK);
        blackPen.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        keyWidth = w / NUMBER_OF_KEYS;
        keyHeight = h;

        // tạo mảng phím trắng
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            int left = i * keyWidth;
            int right = left + keyWidth;

            // tạo mảng phím trắng
            RectF rect = new RectF(left, 0, right, h);
            whites.add(new Key(1+1, rect));

            // tạo mảng phím đen
            if (i != 0 && i != 3 && i != 7 && i != 10) {
                rect = new RectF((float)(i - 0.5) * keyWidth, 0,
                        (float)(i + 0.5) * keyWidth, 0.6f * keyHeight);
                blacks.add(new Key(blackCount, rect));
                blackCount++;
            }
        }

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (Key k : whites) {
            canvas.drawRect(k.rect, whitePen);
        }
        // vẽ đường thẳng để phân biệt các phím trắng
        for (int i = 1; i < NUMBER_OF_KEYS; i++) {
            canvas.drawLine(i * keyWidth, 0, i * keyWidth, keyHeight, blackPen);
        }

        for (Key k : blacks) {
            canvas.drawRect(k.rect, blackPen);
        }

        // vẽ đường thẳng phân biệt các phím đen
        for (Key k : blacks) {
            canvas.drawLine(k.rect.left, 0, k.rect.left, k.rect.bottom, whitePen);
            canvas.drawLine(k.rect.right, 0, k.rect.right, k.rect.bottom, whitePen);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        boolean isDownAction = action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_MOVE;

        for (int touchIndex = 0; touchIndex < event.getPointerCount(); touchIndex++) {
            float x = event.getX(touchIndex);
            float y = event.getY(touchIndex);

            for (Key k : whites) {
                if (k.rect.contains(x, y)) {
                    k.down = isDownAction;
                }
            }

            for (Key k : blacks) {
                if (k.rect.contains(x, y)) {
                    k.down = isDownAction;
                }
            }
        }

        invalidate();

        return super.onTouchEvent(event);
    }
}
