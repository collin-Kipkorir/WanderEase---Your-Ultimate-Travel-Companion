package com.wanderease.travelcompanion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

public class StarRatingView extends View {
    private int numStars = 5;
    private float rating = 0;
    private int starSize = 30;
    private int starSpacing = 5;
    private Paint filledPaint;
    private Paint emptyPaint;

    public StarRatingView(Context context) {
        super(context);
        init();
    }

    public StarRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Modify the color of the filled stars here
        filledPaint = new Paint();
        filledPaint.setColor(ContextCompat.getColor(getContext(), R.color.yellow));
        // Alternatively, you can use direct color integer value
        // filledPaint.setColor(0xFFFFA500); // Orange color
        emptyPaint = new Paint();
        emptyPaint.setColor(getResources().getColor(android.R.color.darker_gray));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < numStars; i++) {
            float starLeft = i * (starSize + starSpacing);
            float starRight = starLeft + starSize;
            float starCenter = starLeft + starSize / 2;

            if (i < rating) {
                drawStar(canvas, starCenter, starSize / 2, starSize / 2, filledPaint);
            } else {
                drawStar(canvas, starCenter, starSize / 2, starSize / 2, emptyPaint);
            }
        }
    }

    // Custom method to draw a star
    private void drawStar(Canvas canvas, float cx, float cy, float radius, Paint paint) {
        float angle = (float) (Math.PI / 5);
        float innerRadius = (float) (radius * Math.sin(angle) / Math.cos(angle));
        float rotation = (float) (Math.PI / 2);

        for (int i = 0; i < 5; i++) {
            float startX = cx + (float) (radius * Math.cos(rotation));
            float startY = cy + (float) (radius * Math.sin(rotation));
            float stopX = cx + (float) (innerRadius * Math.cos(rotation + angle));
            float stopY = cy + (float) (innerRadius * Math.sin(rotation + angle));

            canvas.drawLine(startX, startY, stopX, stopY, paint);
            rotation += 2 * angle;
            startX = cx + (float) (radius * Math.cos(rotation));
            startY = cy + (float) (radius * Math.sin(rotation));
            canvas.drawLine(stopX, stopY, startX, startY, paint);
            rotation += 2 * angle;
        }
    }

    // Setter method for rating
    public void setRating(float rating) {
        this.rating = rating;
        invalidate();
    }

    // Setter method for number of stars
    public void setNumStars(int numStars) {
        this.numStars = numStars;
        invalidate();
    }

    // Setter method for star size
    public void setStarSize(int starSize) {
        this.starSize = starSize;
        invalidate();
    }

    // Setter method for star spacing
    public void setStarSpacing(int starSpacing) {
        this.starSpacing = starSpacing;
        invalidate();
    }
}
