package scwen.com.animationtest;

import android.animation.ArgbEvaluator;
import android.util.Log;
import android.view.View;

/**
 * Created by xxh on 2018/7/16.
 */

public class AnimatorProviderImpl extends AnimatorProvider {

    private AnimatorLayoutParam mLayoutParam;


    /**
     * <attr name="discrollve_translation">
     * <flag name="fromTop" value="0x01" />
     * <flag name="fromBottom" value="0x02" />
     * <flag name="fromLeft" value="0x04" />
     * <flag name="fromRight" value="0x08" />
     * </attr>
     * 0000000001
     * 0000000010
     * 0000000100
     * 0000001000
     * top|left
     * 0000000001 top
     * 0000000100 left 或运算 |
     * 0000000101
     * 反过来就使用& 与运算
     */
    //保存自定义属性
    //定义很多的自定义属性
    private static final int TRANSLATION_FROM_TOP = 0x01;
    private static final int TRANSLATION_FROM_BOTTOM = 0x02;
    private static final int TRANSLATION_FROM_LEFT = 0x04;
    private static final int TRANSLATION_FROM_RIGHT = 0x08;

    //颜色估值器
    private static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    /**
     * 自定义属性的一些接收的变量
     */
    private boolean mDiscrollveAlpha;//是否需要透明度动画
    private int mDiscrollveFromBgColor;//背景颜色变化开始值
    private int mDiscrollveToBgColor;//背景颜色变化结束值
    private int mDisCrollveTranslation;//平移值
    private boolean mDiscrollveScaleX;//是否需要x轴方向缩放
    private boolean mDiscrollveScaleY;//是否需要y轴方向缩放
    private int mHeight;//本view的高度
    private int mWidth;//宽度


    public AnimatorProviderImpl(View view,AnimatorLayoutParam layoutParam) {
        super(view,layoutParam);
        mLayoutParam = layoutParam;
        this.mDiscrollveAlpha = mLayoutParam.mDiscrollveAlpha;
        this.mDiscrollveFromBgColor = mLayoutParam.mDiscrollveFromBgColor;
        this.mDiscrollveToBgColor = mLayoutParam.mDiscrollveToBgColor;
        mDisCrollveTranslation = mLayoutParam.mDisCrollveTranslation;
        mDiscrollveScaleX = mLayoutParam.mDiscrollveScaleX;
        mDiscrollveScaleY = mLayoutParam.mDiscrollveScaleY;

    }
    public void setmDiscrollveFromBgColor(int mDiscrollveFromBgColor) {
        this.mDiscrollveFromBgColor = mDiscrollveFromBgColor;
    }

    public void setmDiscrollveToBgColor(int mDiscrollveToBgColor) {
        this.mDiscrollveToBgColor = mDiscrollveToBgColor;
    }

    public void setmDiscrollveAlpha(boolean mDiscrollveAlpha) {
        this.mDiscrollveAlpha = mDiscrollveAlpha;
    }

    public void setmDisCrollveTranslation(int mDisCrollveTranslation) {
        this.mDisCrollveTranslation = mDisCrollveTranslation;
    }

    public void setmDiscrollveScaleX(boolean mDiscrollveScaleX) {
        this.mDiscrollveScaleX = mDiscrollveScaleX;
    }

    public void setmDiscrollveScaleY(boolean mDiscrollveScaleY) {
        this.mDiscrollveScaleY = mDiscrollveScaleY;
    }



    private boolean isTranslationFrom(int translationMask){
        if(mDisCrollveTranslation ==-1){
            return false;
        }
        //fromLeft|fromeBottom & fromBottom = fromBottom
        return (mDisCrollveTranslation & translationMask) == translationMask;
    }

    @Override
    void onAnim(float ratio) {
        if(mWidth==0) {
            mWidth = mView.getWidth();
        }

        if(mHeight==0) {
            mHeight = mView.getHeight();
        }
        //执行动画ratio：0~1
        if(mDiscrollveAlpha){
            mView.setAlpha(ratio);
        }
        if(mDiscrollveScaleX){
            Log.e("TAG", "scaleX:"+ratio);
            mView.setScaleX(ratio);
        }
        if(mDiscrollveScaleY){
            Log.e("TAG", "scaleY:"+ratio);
            mView.setScaleY(ratio);
        }
        //平移动画  int值：left,right,top,bottom    left|bottom
        if(isTranslationFrom(TRANSLATION_FROM_BOTTOM)){//是否包含bottom
            mView. setTranslationY(mHeight*(1-ratio));//height--->0(0代表恢复到原来的位置)
        }
        if(isTranslationFrom(TRANSLATION_FROM_TOP)){//是否包含bottom
            mView.setTranslationY(-mHeight*(1-ratio));//-height--->0(0代表恢复到原来的位置)
        }
        if(isTranslationFrom(TRANSLATION_FROM_LEFT)){
            mView. setTranslationX(-mWidth*(1-ratio));//mWidth--->0(0代表恢复到本来原来的位置)
        }
        if(isTranslationFrom(TRANSLATION_FROM_RIGHT)){
            mView.setTranslationX(mWidth*(1-ratio));//-mWidth--->0(0代表恢复到本来原来的位置)
        }
        //判断从什么颜色到什么颜色
        if(mDiscrollveFromBgColor!=-1&&mDiscrollveToBgColor!=-1){
            mView.setBackgroundColor((int) sArgbEvaluator.evaluate(ratio, mDiscrollveFromBgColor, mDiscrollveToBgColor));
        }
    }

    @Override
    void resetAnim() {

            if(mDiscrollveAlpha){
                mView.setAlpha(0);
            }
            if(mDiscrollveScaleX){
                mView.setScaleX(0);
            }
            if(mDiscrollveScaleY){
                mView.setScaleY(0);
            }
            //平移动画  int值：left,right,top,bottom    left|bottom
            if(isTranslationFrom(TRANSLATION_FROM_BOTTOM)){//是否包含bottom
                mView.setTranslationY(mHeight);//height--->0(0代表恢复到原来的位置)
            }
            if(isTranslationFrom(TRANSLATION_FROM_TOP)){//是否包含bottom
                mView.setTranslationY(-mHeight);//-height--->0(0代表恢复到原来的位置)
            }
            if(isTranslationFrom(TRANSLATION_FROM_LEFT)){
                mView.setTranslationX(-mWidth);//mWidth--->0(0代表恢复到本来原来的位置)
            }
            if(isTranslationFrom(TRANSLATION_FROM_RIGHT)){
                mView.setTranslationX(mWidth);//-mWidth--->0(0代表恢复到本来原来的位置)
            }
    }
}
