package com.example.jiefly.multiparametermonitor.measuring.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.jiefly.multiparametermonitor.R;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Created by chgao on 17-5-9.
 */

public class EcgView extends SurfaceView implements SurfaceHolder.Callback {

    public static boolean isRunning;
    private static Queue<Integer> ecg0Datas = new LinkedList<Integer>();
    private static Queue<Integer> ecg1Datas = new LinkedList<Integer>();
    private static SoundPool soundPool;
    private static int soundId;//心跳提示音
    private Context mContext;
    private SurfaceHolder surfaceHolder;
    private Canvas mCanvas;
    private float ecgMax = 4096;//心电的最大值
    private int wave_speed = 25;//波速: 25mm/
    private int sleepTime = 16; //每次锁屏的时间间距，单位:ms
    private float lockWidth;//每次锁屏需要画的
    private int ecgPerCount = 8;//每次画心电数据的个数，心电每秒有500个数据包
    private Paint mBackgroundPaint;//画背景的画笔
    private Paint mBackgroundDashPaint;
    private Path mBackgroundLinePath;
    private Paint mPaint;//画波形图的画笔
    private int mWidth;//控件宽度
    private int mHeight;//控件高度
    private float ecgYRatio;
    private int startY0;
    private int startY1;
    private int yOffset1;//波2的Y坐标偏移值
    private int mUnitWidth;//心电图图纸每个小格的宽度
    private int mBigUnitWidth;//心电图图纸每个大格的宽度
    private Rect rect;
    private int startX;//每次画线的X坐标起点
    private double ecgXOffset;//每次X坐标偏移的像素
    private int blankLineWidth = 6;//右侧空白点的宽度
    Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                long startTime = System.currentTimeMillis();
                startDrawWave();

                long endTime = System.currentTimeMillis();
                if (endTime - startTime < sleepTime) {
                    try {
                        Thread.sleep(sleepTime - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        rect = new Rect();
        converXOffset();
    }

    public static void addEcgData0(int data) {
        ecg0Datas.add(data);
    }

    public static void addEcgData1(int data) {
        ecg1Datas.add(data);
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStrokeWidth(1);
        mBackgroundPaint.setColor(Color.RED);

        mBackgroundDashPaint = new Paint();
        mBackgroundDashPaint.setAntiAlias(true);
        mBackgroundDashPaint.setStrokeWidth(0.8f);
        mBackgroundDashPaint.setColor(Color.RED);
        mBackgroundDashPaint.setStyle(Paint.Style.STROKE);
        mBackgroundDashPaint.setPathEffect(new DashPathEffect(new float[]{1.0f, 2.0f}, 1));

        mBackgroundLinePath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);


        soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
        soundId = soundPool.load(mContext, R.raw.heartbeat, 1);

        ecgXOffset = lockWidth / ecgPerCount;
        startY0 = mHeight * (1 / 4);//波1初始Y坐标是控件高度的1/4
        startY1 = mHeight & (3 / 4);
        ecgYRatio = mHeight / 2 / ecgMax;

        yOffset1 = mHeight / 2;
    }

    /**
     * 根据波速计算每次X坐标增加的像素
     * <p>
     * 计算出每次锁屏应该画的px值
     */
    private void converXOffset() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //获取屏幕对角线的长度，单位:px
        double diagonalMm = Math.sqrt(width * width + height * height) / dm.densityDpi;//单位：英寸
        diagonalMm = diagonalMm * 2.54 * 10;//转换单位为：毫米
        double diagonalPx = width * width + height * height;
        diagonalPx = Math.sqrt(diagonalPx);
        //每毫米有多少px
        double px1mm = diagonalPx / diagonalMm;
        mUnitWidth = (int) px1mm;
        mBigUnitWidth = 5 * mUnitWidth;//每个大格子中有五个小格子,每个大格子应该在200ms内走完
        //每秒画多少px
        double px1s = wave_speed * px1mm;
        //每次锁屏所需画的宽度
        lockWidth = (float) (px1s * (sleepTime / 1000f));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        //canvas.drawColor(Color.parseColor(bgColor));
        drawBackground(canvas);
        holder.unlockCanvasAndPost(canvas);
        startThread();
    }

    protected void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (mBackgroundLinePath == null) {
            mBackgroundLinePath = new Path();
        } else {
            mBackgroundLinePath.reset();
        }
        for (int i = 0; ; i++) {
            if (mBigUnitWidth * i <= mWidth && mBigUnitWidth * i <= mHeight) {
                //横线
//                mBackgroundLinePath.moveTo(0, i * mBigUnitWidth);
//                mBackgroundLinePath.lineTo(mWidth, i * mBigUnitWidth);
                //竖线
//                mBackgroundLinePath.moveTo(i * mBigUnitWidth, 0);
//                mBackgroundLinePath.lineTo(i * mBigUnitWidth, mHeight);
                //横线
                canvas.drawLine(0, i * mBigUnitWidth, mWidth, i * mBigUnitWidth, mBackgroundPaint);
                //竖线
                canvas.drawLine(i * mBigUnitWidth, 0, i * mBigUnitWidth, mHeight, mBackgroundPaint);
            } else if (mBigUnitWidth * i <= mWidth) {
                //竖线
//                mBackgroundLinePath.moveTo(i * mBigUnitWidth, 0);
//                mBackgroundLinePath.lineTo(i * mBigUnitWidth, mHeight);
                canvas.drawLine(i * mBigUnitWidth, 0, i * mBigUnitWidth, mHeight, mBackgroundPaint);
            } else if (mBigUnitWidth * i <= mHeight) {
                //横线
//                mBackgroundLinePath.moveTo(0, i * mBigUnitWidth);
//                mBackgroundLinePath.lineTo(mWidth, i * mBigUnitWidth);
                canvas.drawLine(0, i * mBigUnitWidth, mWidth, i * mBigUnitWidth, mBackgroundPaint);
            } else {
                break;
            }
        }
//        canvas.drawPath(mBackgroundLinePath,mBackgroundPaint);
//        mBackgroundLinePath.reset();
        for (int i = 0; ; i++) {
            if (mUnitWidth * i % mBigUnitWidth == 0) {
                continue;
            }
            if (mUnitWidth * i <= mWidth && mUnitWidth * i <= mHeight) {
                //横线
                mBackgroundLinePath.moveTo(0, i * mUnitWidth);
                mBackgroundLinePath.lineTo(mWidth, i * mUnitWidth);
                //竖线
                mBackgroundLinePath.moveTo(i * mUnitWidth, 0);
                mBackgroundLinePath.lineTo(i * mUnitWidth, mHeight);
            } else if (mUnitWidth * i <= mWidth) {
                //竖线
                mBackgroundLinePath.moveTo(i * mUnitWidth, 0);
                mBackgroundLinePath.lineTo(i * mUnitWidth, mHeight);
            } else if (mUnitWidth * i <= mHeight) {
                //横线
                mBackgroundLinePath.moveTo(0, i * mUnitWidth);
                mBackgroundLinePath.lineTo(mWidth, i * mUnitWidth);
            } else {
                break;
            }
        }
        canvas.drawPath(mBackgroundLinePath, mBackgroundDashPaint);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        isRunning = true;
        init();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread();
    }

    private void startThread() {
        isRunning = true;
        new Thread(drawRunnable).start();
    }

    private void stopThread() {
        isRunning = false;
    }

    private void startDrawWave() {
        rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth), mHeight);
        mCanvas = surfaceHolder.lockCanvas(rect);
        if (mCanvas == null) return;
        //mCanvas.drawColor(Color.parseColor(bgColor));
        drawBackground(mCanvas);
        drawWave0();
        drawWave1();
        surfaceHolder.unlockCanvasAndPost(mCanvas);

        startX = (int) (startX + lockWidth);
        if (startX > mWidth) {
            startX = 0;
        }
    }

    /**
     * 画波1
     */
    private void drawWave0() {
        try {
            float mStartX = startX;
            if (ecg0Datas.size() > ecgPerCount) {
                for (int i = 0; i < ecgPerCount; i++) {
                    float newX = (float) (mStartX + ecgXOffset);
                    int newY = ecgConver(ecg0Datas.poll());
                    mCanvas.drawLine(mStartX, startY0, newX, newY, mPaint);
                    mStartX = newX;
                    startY0 = newY;
                }
            } else {
                /**
                 * 如果没有数据
                 * 因为有数据一次画ecgPerCount个数，那么无数据时候就应该画ecgPercount倍数长度的中线
                 */
                int newX = (int) (mStartX + ecgXOffset * ecgPerCount);
                int newY = ecgConver((int) (ecgMax / 2));
                mCanvas.drawLine(mStartX, startY0, newX, newY, mPaint);
                startY0 = newY;
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 画波2
     */
    private void drawWave1() {
        try {
            float mStartX = startX;
            if (ecg1Datas.size() > ecgPerCount) {
                for (int i = 0; i < ecgPerCount; i++) {
                    float newX = (float) (mStartX + ecgXOffset);
                    int newY = ecgConver(ecg1Datas.poll()) + yOffset1;
                    mCanvas.drawLine(mStartX, startY1, newX, newY, mPaint);
                    mStartX = newX;
                    startY1 = newY;
                }
            } else {
                /**
                 * 如果没有数据
                 * 因为有数据一次画ecgPerCount个数，那么无数据时候就应该画ecgPercount倍数长度的中线
                 */
                int newX = (int) (mStartX + ecgXOffset * ecgPerCount);
                int newY = ecgConver((int) (ecgMax / 2)) + yOffset1;
                mCanvas.drawLine(mStartX, startY1, newX, newY, mPaint);
                startY1 = newY;
            }
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     *
     * @param data
     * @return
     */
    private int ecgConver(int data) {
        data = (int) (ecgMax - data);
        data = (int) (data * ecgYRatio);
        return data;
    }

}
