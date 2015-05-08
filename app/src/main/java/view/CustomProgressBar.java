package view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by User1 on 20/3/2015.
 */
public class CustomProgressBar {
    private Paint barPaint;
    private float barStartX;
    private float barStartY;
    private float barEndX;
    private float barEndY;
    private float currentSeekBarLength = 0;


    CustomProgressBar(int sX,int sY,int eX,int eY) {
        barPaint = new Paint();
        barStartX = sX;
        barStartY = sY;
        barEndX = eX;
        barEndY = eY;
    }

    private void setCurrentSeekBarLength(float x) {

        this.currentSeekBarLength = x;
    }

    private float getCurrentSeekBarLength() {

        return this.currentSeekBarLength;
    }

    public void drawProgressBar(Canvas c,float seconds) {
        barPaint.setColor(Color.WHITE);
        setCurrentSeekBarLength((barEndX - barStartX) * (seconds / 15));
        barPaint.setColor(Color.BLUE);
        //barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeWidth(10);
        c.drawRect(barStartX, barStartY, getCurrentSeekBarLength(),
                barEndY, barPaint);

    }
}
