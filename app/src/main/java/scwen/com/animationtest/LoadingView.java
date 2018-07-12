package scwen.com.animationtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by xxh on 2018/7/12.
 */

public class LoadingView extends View {

    private ValueAnimator mAnimator;


    //小圆的半径
    private int mCircleRidus = 18;
    //小圆的颜色
    private int[] mCircleColors;
    //坐标中心到 小圆 圆心的距离
    private float mRotationRadis = 90;


    //整体的背景
    private int bg = Color.WHITE;

    //画笔
    private Paint mCirclePaint;

    // 扩散时的 实时半径
    private float mCurrentRotationRadis = mRotationRadis;

    //旋转的角度
    private float mCurrentRotationRangle = 0f;

    //空心圆的实时半径
    private float mHoleRadis = 0f;

    private int mCenterX;
    private int mCenterY;
    private float mDiagonal;


    private int defaultTime = 1000;



    private LoadingState mState;


    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

        mDiagonal = (float) (Math.sqrt(w * w + h * h) / 2);

        Log.e("TAG", ""+mDiagonal);
    }

    private void init(Context context) {
        //获取颜色数组
        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);
        //画笔初始化
        mCirclePaint = new Paint();

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mState == null) {
            mState = new RotateState();
        }

        mState.drawState(canvas);


    }

    /**
     * 策略模式 视图的三种状态
     * 将绘制的动作 交由 子类去完成
     */
    private abstract class LoadingState {

        public abstract void drawState(Canvas canvas);

        public void cancle() {
            if (mAnimator != null) {
                mAnimator.cancel();
            }
        }
    }


    public void loadEnd() {
        //加载结束   切换撞他

        if (mState != null && mState instanceof LoadingState) {
            mState.cancle();
            post(new Runnable() {
                @Override
                public void run() {
                    //修改状态  并进行重绘
                    mState = new StrenchState();
                    postInvalidate();
                }
            });
        }
    }


    /**
     * 旋转状态
     * <p>
     * 控制圆形的坐标
     */
    public class RotateState extends LoadingState {


        public RotateState() {
              mAnimator = ValueAnimator.ofFloat(0f, (float) Math.PI * 2);
            mAnimator.setDuration(defaultTime);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //动画执行过程
                    //修改 旋转角度  并进行重绘
                    mCurrentRotationRangle = (float) animation.getAnimatedValue();

                    postInvalidate();
                }
            });
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawableBackground(canvas);
            drawCircles(canvas);
        }
    }


    /**
     * 聚合动画
     */
    public class StrenchState extends LoadingState {

        public StrenchState() {
            mAnimator = ValueAnimator.ofFloat(mRotationRadis, 0);
            mAnimator.setInterpolator(new OvershootInterpolator(20f));
            mAnimator.setDuration(defaultTime);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotationRadis = (float) animation.getAnimatedValue();

                    postInvalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    mState = new DiffusionState();

                    postInvalidate();
                }
            });
            mAnimator.start();

        }

        @Override
        public void drawState(Canvas canvas) {
            drawableBackground(canvas);
            drawCircles(canvas);
        }
    }

    /**
     * 最后的水波纹 扩散动画
     */
    public class DiffusionState extends LoadingState {

        public DiffusionState() {
            mAnimator = ValueAnimator.ofFloat(mHoleRadis, mDiagonal);
            mAnimator.setInterpolator(new AccelerateInterpolator());

            mAnimator.setDuration(defaultTime);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mHoleRadis = (float) animation.getAnimatedValue();

                    postInvalidate();
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setVisibility(GONE);
                }
            });
            mAnimator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            if(mHoleRadis==0) {
                canvas.drawColor(bg);
            }
            mCirclePaint.setColor(bg);
            mCirclePaint.setStyle(Paint.Style.STROKE);

            float strokeWidth = mDiagonal - mHoleRadis;
            mCirclePaint.setStrokeWidth(strokeWidth);

            float ridus = mHoleRadis + strokeWidth/2;
            canvas.drawCircle(mCenterX, mCenterY,ridus , mCirclePaint);

        }
    }


    private void drawableBackground(Canvas canvas) {
        canvas.drawColor(bg);
    }


    private void drawCircles(Canvas canvas) {
        float rotationRangle = (float) (2 * Math.PI / mCircleColors.length);

        for (int i = 0; i < mCircleColors.length; i++) {
            float angle = i * rotationRangle + mCurrentRotationRangle;

            float cx = (float) (mCurrentRotationRadis * Math.cos(angle) + mCenterX);
            float cy = (float) (mCurrentRotationRadis * Math.sin(angle) + mCenterY);
            mCirclePaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx, cy, mCircleRidus, mCirclePaint);
        }
    }







}
