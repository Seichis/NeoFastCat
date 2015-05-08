package view;

/**
 * Created by User1 on 17/3/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.jupiter.on.tetsuo.neofastcat.ConnectDotsOneShotActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.ImageAdapter;


/**
 * Created by User1 on 14/3/2015.
 */
public class DrawingPanelOneShot extends ImageView implements View.OnTouchListener {
    int count;
    private List<ShapeDrawable> dotsToDraw;
    private Path mPath;
    private Paint mPaint;
    private List<Path> pathsToReset = new ArrayList<>();
    private List<Path> pathsToStay = new ArrayList<>();
    private Path mPathStay;
    private Paint textPaint;
    static int temp = 0;
    public static int score = 0;
    private Set<ShapeDrawable> changedDotSet;
    private static List<Integer> dotIdForSet;
    static Set<List<Integer>> pmSetList;
    private CustomProgressBar mCustomProgressBar;
    Context c;
    AttributeSet attr;
    Point mPoint = new Point();
    public WindowManager wm;
    Display display;
    ShapeDrawable[] mSpeedArray;

    public DrawingPanelOneShot(Context context, AttributeSet attrs) {
        super(context, attrs);
        wm = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        display.getSize(mPoint);
        changedDotSet = new HashSet<>();
        dotIdForSet = new ArrayList<>();
        pmSetList = new HashSet<>();
        mPathStay = new Path();
        count = 0;
        mCustomProgressBar = new CustomProgressBar(20, 20, 600, 40);
        this.c = context;
        this.attr = attrs;
        dotsToDraw = new ArrayList<>();
        textPaint = new Paint();
        mSpeedArray = new ShapeDrawable[5];
        mPaint = new Paint();
        mPath = new Path();
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        setTextPaint(textPaint);
        setDotPaint(mPaint);
        pathsToReset.add(mPath);
        pathsToStay.add(mPathStay);
        setDotsToDraw();
        dotsToDraw.toArray(mSpeedArray);
    }

    private void setDotPaint(Paint paint) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);
    }

    public void setTextPaint(Paint paint) {
        paint.setColor(Color.WHITE);
        paint.setTextSize(80f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(Integer.toString(score), mPoint.x / 2, mPoint.y / 8, textPaint);
        drawDots(canvas);
        mCustomProgressBar.drawProgressBar(canvas, ConnectDotsOneShotActivity.second);
        for (Path p : pathsToReset) {
            canvas.drawPath(p, mPaint);
        }
        for (Path p : pathsToStay) {
            canvas.drawPath(p, mPaint);
        }
    }

    private void drawDots(Canvas c) {
        for (ShapeDrawable dot : dotsToDraw) {
            dot.draw(c);
        }
    }

    private float mX, mY;


    private void touch_start(float x, float y) {

        mPath.moveTo(x, y);
        mPathStay.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {

        mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
        mX = x;
        mY = y;

        checkPath((int) x, (int) y);
    }

    private void resetLevel() {
        ConnectDotsOneShotActivity.timeToReset = true;
        for (ShapeDrawable dot : dotsToDraw) {
            dot.getPaint().setColor(Color.WHITE);
            changedDotSet.clear();
        }
        ConnectDotsOneShotActivity.setAdapterToShow();
    }

    private void touch_up() {
        mPath.reset();

        if (dotIdForSet.size() > 1 && changedDotSet.size() > 1) {

            Collections.sort(dotIdForSet);
            pmSetList.add(dotIdForSet);
            if (temp != pmSetList.size()) {


                score++;

                this.buildDrawingCache(false);

                Bitmap b = Bitmap.createBitmap(this.getDrawingCache(false));
                ImageAdapter.addImage(b);
                this.destroyDrawingCache();
                temp = pmSetList.size();

            }
            dotIdForSet.clear();
        }
        resetLevel();
    }

    float startX = 0;
    float startY = 0;


    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                touch_start(startX, startY);
                invalidate();
                pathsToStay.clear();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    public void setDotsToDraw() {
        int diameter = 50;
        for (Point p : createFixedPoints()) {
            this.dotsToDraw.add(createDot(p.x, p.y, diameter));

        }

    }

    private List<Point> createFixedPoints() {

        List<Point> pointList = new ArrayList<>();
        Point mPointA = new Point();
        Point mPointB = new Point();
        Point mPointC = new Point();
        Point mPointD = new Point();
        Point mPointE = new Point();
        int width;
        int height;

        // Set Point A of the rectangle

        width = (mPoint.x / 4);
        height = (mPoint.y / 8);
        mPointA.set(width, height);
        pointList.add(mPointA);

        // Set Point B of the rectangle

        width = (3 * mPoint.x / 4);
        height = (mPoint.y / 8);
        mPointB.set(width, height);
        pointList.add(mPointB);

        // Set Point C of the rectangle

        width = (mPoint.x / 4);
        height = (3 * mPoint.y / 8);
        mPointC.set(width, height);
        pointList.add(mPointC);

        // Set Point D of the rectangle

        width = (3 * mPoint.x / 4);
        height = (3 * mPoint.y / 8);
        mPointD.set(width, height);

        pointList.add(mPointD);

        // Set Point E in the center of the rectangle

        width = (mPoint.x / 2);
        height = (mPoint.y / 4);
        mPointE.set(width, height);
        pointList.add(mPointE);
        return pointList;
    }

    private ShapeDrawable createDot(int width, int height, int diameter) {
        ShapeDrawable dot = new ShapeDrawable(new OvalShape());
        dot.setBounds(width, height, width + diameter, height + diameter);
        dot.getPaint().setColor(Color.WHITE);
        return dot;
    }


    private void checkPath(int x, int y) {

        for (ShapeDrawable dot : mSpeedArray) {
            if (Math.abs(x - dot.getBounds().exactCenterX()) < 90 && Math.abs(y - dot.getBounds().exactCenterY()) < 90) {


                if (!changedDotSet.contains(dot)) {

                    if (count == 0) {
                        startX = dot.getBounds().centerX();
                        startY = dot.getBounds().centerY();
                        count++;
                    }
                    changedDotSet.add(dot);
                    mPathStay.moveTo(startX, startY);
                    mPathStay.lineTo(dot.getBounds().centerX(), dot.getBounds().centerY());
                    mPathStay = new Path();
                    pathsToStay.add(mPathStay);

                    startX = dot.getBounds().centerX();
                    startY = dot.getBounds().centerY();
                    dotIdForSet.add(dotsToDraw.indexOf(dot));
                }
            }
        }
    }
}

