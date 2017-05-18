package com.mao.view;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;




/**
 * Created by Farble on 2015/3/10.
 */
public class PhotoCropView extends View {
    private static final String TAG = "PhotoCropView";

    private onLocationListener locationListener;/*listen to the Rect */
    private onChangeLocationlistener changeLocationlistener;/*listening position changed */

    private int MODE;
    private static final int MODE_OUTSIDE = 0x000000aa;/*170*/
    private static final int MODE_INSIDE = 0x000000bb;/*187*/
    private static final int MODE_POINT = 0X000000cc;/*204*/
    private static final int MODE_ILLEGAL = 0X000000dd;/*221*/

    private static final int minWidth = 100;/*the minimum width of the rectangle*/
    private static final int minHeight = 200;/*the minimum height of the rectangle*/

    private static final int START_X = 200;
    private static final int START_Y = 200;

    private static final float EDGE_WIDTH = 1.8f;
    private static final int ACCURACY= 15;/*touch accuracy*/

    private int pointPosition;/*vertex of a rectangle*/

    private int sX;/*start X location*/
    private int sY;/*start Y location*/
    private int eX;/*end X location*/
    private int eY;/*end Y location*/

    private int pressX;/*X coordinate values while finger press*/
    private int pressY;/*Y coordinate values while finger press*/

    private int memonyX;/*the last time the coordinate values of X*/
    private int memonyY;/*the last time the coordinate values of Y*/

    private int coverWidth = 300;/*width of selection box*/
    private int coverHeight = 400;/*height of selection box*/

    private Paint mPaint;
    private Paint mPaintLine;
    private Bitmap mBitmapCover;
    private Bitmap mBitmapRectBlack;
    private PorterDuffXfermode xfermode;/*paint mode*/

    public PhotoCropView(Context context) {
        super(context);
        init();
    }

    public PhotoCropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("deprecation")
    private void init() {
        sX = START_X;
        sY = START_Y;
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();
        mBitmapCover = makeBitmap(width, height, 0x5A000000, 0, 0);
        mBitmapRectBlack = makeBitmap(coverWidth, coverHeight, 0xff000000, coverWidth, coverHeight);

        eX = sX + coverWidth;
        eY = sY + coverHeight;
        pressX = 0;
        pressY = 0;

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaintLine = new Paint();
        mPaintLine.setColor(Color.WHITE);
        mPaintLine.setStrokeWidth(2.0f);
    }

    /*生成bitmap*/
    private Bitmap makeBitmap(int mwidth, int mheight, int resource, int staX, int staY) {
        Bitmap bm = Bitmap.createBitmap(mwidth, mheight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(resource);
        c.drawRect(staX, staY, mwidth, mheight, p);
        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setFilterBitmap(false);
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(mBitmapCover, 0, 0, mPaint);
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(mBitmapRectBlack, sX, sY, mPaint);
        if (locationListener != null) {
            locationListener.locationRect(sX, sY, eX, eY);
        }
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);
        canvas.drawLine((float) sX - EDGE_WIDTH, (float) sY - EDGE_WIDTH, (float) eX + EDGE_WIDTH, (float) sY - EDGE_WIDTH, mPaintLine);/*up -*/
        canvas.drawLine((float) sX - EDGE_WIDTH, (float) eY + EDGE_WIDTH, (float) eX + EDGE_WIDTH, (float) eY + EDGE_WIDTH, mPaintLine);/*down -*/
        canvas.drawLine((float) sX - EDGE_WIDTH, (float) sY - EDGE_WIDTH, (float) sX - EDGE_WIDTH, (float) eY + EDGE_WIDTH, mPaintLine);/*left |*/
        canvas.drawLine((float) eX + EDGE_WIDTH, (float) sY - EDGE_WIDTH, (float) eX + EDGE_WIDTH, (float) eY + EDGE_WIDTH, mPaintLine);/*righ |*/

    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (changeLocationlistener != null) {
                    changeLocationlistener.locationChange("change self");
                } else {
                    changeLocationlistener = null;
                }

                memonyX = (int) event.getX();
                memonyY = (int) event.getY();
                checkMode(memonyX, memonyY);
                break;
            case MotionEvent.ACTION_MOVE: {
                switch (MODE) {
                    case MODE_ILLEGAL:
                        pressX = (int) event.getX();
                        pressY = (int) event.getY();
                        recoverFromIllegal(pressX, pressY);
                        postInvalidate();
                        break;
                    case MODE_OUTSIDE:
                        //do nothing;
                        break;
                    case MODE_INSIDE:
                        pressX = (int) event.getX();
                        pressY = (int) event.getY();
                        moveByTouch(pressX, pressY);
                        postInvalidate();
                        break;
                    default:
                        /*MODE_POINT*/
                        pressX = (int) event.getX();
                        pressY = (int) event.getY();
                        mPaintLine.setColor(Color.YELLOW);
                        moveByPoint(pressX, pressY);
                        postInvalidate();
                        break;
                }
            }
            break;
            case MotionEvent.ACTION_UP:
                mPaintLine.setColor(Color.WHITE);
                postInvalidate();
                break;
            default:
                break;
        }
        return true;
    }

    /*从非法状态恢复，这里处理的是达到最小值后能拉伸放大*/
    private void recoverFromIllegal(int rx, int ry) {
        if ((rx > sX && ry > sY) && (rx < eX && ry < eY)) {
            MODE = MODE_ILLEGAL;
        } else {
            MODE = MODE_POINT;
        }
    }

    private void checkMode(int cx, int cy) {
        if (cx > sX && cx < eX && cy > sY && cy < eY) {
            MODE = MODE_INSIDE;
        } else if (nearbyPoint(cx, cy) < 4) {
            MODE = MODE_POINT;
        } else {
            MODE = MODE_OUTSIDE;
        }
    }

    /*判断点(inX,inY)是否靠近矩形的4个顶点*/
    private int nearbyPoint(int inX, int inY) {
        if ((Math.abs(sX - inX) <= ACCURACY && (Math.abs(inY - sY) <= ACCURACY))) {/*left-up angle*/
            pointPosition = 0;
            return 0;
        }
        if ((Math.abs(eX - inX) <= ACCURACY && (Math.abs(inY - sY) <= ACCURACY))) {/*right-up  angle*/
            pointPosition = 1;
            return 1;
        }
        if ((Math.abs(sX - inX) <= ACCURACY && (Math.abs(inY - eY) <= ACCURACY))) {/*left-down angle*/
            pointPosition = 2;
            return 2;
        }
        if ((Math.abs(eX - inX) <= ACCURACY && (Math.abs(inY - eY) <= ACCURACY))) {/*right-down angle*/
            pointPosition = 3;
            return 3;
        }
        pointPosition = 100;
        return 100;
    }

    /*刷新矩形的坐标*/
    private void refreshLocation(int isx, int isy, int iex, int iey) {
        this.sX = isx;
        this.sY = isy;
        this.eX = iex;
        this.eY = iey;
    }

    /*矩形随手指移动*/
    private void moveByTouch(int mx, int my) {/*move center point*/
        int dX = mx - memonyX;
        int dY = my - memonyY;

        sX += dX;
        sY += dY;

        eX = sX + coverWidth;
        eY = sY + coverHeight;

        memonyX = mx;
        memonyY = my;

    }

    /*检测矩形是否达到最小值*/
    private boolean checkLegalRect(int cHeight, int cWidth) {
        return (cHeight > minHeight && cWidth > minWidth);
    }

    /*点击顶点附近时的缩放处理*/
    @SuppressWarnings("SuspiciousNameCombination")
    private void moveByPoint(int bx, int by) {
        switch (pointPosition) {
            case 0:/*left-up*/
                coverWidth = Math.abs(eX - bx);
                coverHeight = Math.abs(eY - by);
                //noinspection SuspiciousNameCombination
                if (!checkLegalRect(coverWidth, coverHeight)) {
                    MODE = MODE_ILLEGAL;
                } else {
                    mBitmapRectBlack = null;
                    mBitmapRectBlack = makeBitmap(coverWidth, coverHeight, 0xff000000, coverWidth, coverHeight);
                    refreshLocation(bx, by, eX, eY);
                }
                break;
            case 1:/*right-up*/
                coverWidth = Math.abs(bx - sX);
                coverHeight = Math.abs(eY - by);
                if (!checkLegalRect(coverWidth, coverHeight)) {
                    MODE = MODE_ILLEGAL;
                } else {
                    mBitmapRectBlack = null;
                    mBitmapRectBlack = makeBitmap(coverWidth, coverHeight, 0xff000000, coverWidth, coverHeight);
                    refreshLocation(sX, by, bx, eY);
                }
                break;
            case 2:/*left-down*/
                coverWidth = Math.abs(eX - bx);
                coverHeight = Math.abs(by - sY);
                if (!checkLegalRect(coverWidth, coverHeight)) {
                    MODE = MODE_ILLEGAL;
                } else {
                    mBitmapRectBlack = null;
                    mBitmapRectBlack = makeBitmap(coverWidth, coverHeight, 0xff000000, coverWidth, coverHeight);
                    refreshLocation(bx, sY, eX, by);
                }
                break;
            case 3:/*right-down*/
                coverWidth = Math.abs(bx - sX);
                coverHeight = Math.abs(by - sY);
                if (!checkLegalRect(coverWidth, coverHeight)) {
                    MODE = MODE_ILLEGAL;
                } else {
                    mBitmapRectBlack = null;
                    mBitmapRectBlack = makeBitmap(coverWidth, coverHeight, 0xff000000, coverWidth, coverHeight);
                    refreshLocation(sX, sY, bx, by);
                }
                break;
            default:
                break;
        }
    }

    public void setLocationListener(onLocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public interface onLocationListener {
        public void locationRect(int startX, int startY, int endX, int endY);
    }

    public interface onChangeLocationlistener {
        @SuppressWarnings("SameParameterValue")
        public void locationChange(String msg);
    }
}
