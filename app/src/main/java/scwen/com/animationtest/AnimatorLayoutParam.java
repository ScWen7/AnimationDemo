package scwen.com.animationtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by xxh on 2018/7/16.
 */

public class AnimatorLayoutParam extends LinearLayout.LayoutParams {


    public boolean mDiscrollveAlpha;
    public boolean mDiscrollveScaleX;
    public boolean mDiscrollveScaleY;
    public int mDisCrollveTranslation;
    public int mDiscrollveFromBgColor;
    public int mDiscrollveToBgColor;


    public AnimatorLayoutParam(Context c, AttributeSet attrs) {
        super(c, attrs);

        TypedArray a = c.obtainStyledAttributes(attrs,R.styleable.AnimatorFrame);
        //没有传属性过来,给默认值FALSE
        mDiscrollveAlpha = a.getBoolean(R.styleable.AnimatorFrame_discrollve_alpha, false);
        mDiscrollveScaleX = a.getBoolean(R.styleable.AnimatorFrame_discrollve_scaleX, false);
        mDiscrollveScaleY = a.getBoolean(R.styleable.AnimatorFrame_discrollve_scaleY, false);
        mDisCrollveTranslation = a.getInt(R.styleable.AnimatorFrame_discrollve_translation, -1);
        mDiscrollveFromBgColor = a.getColor(R.styleable.AnimatorFrame_discrollve_fromBgColor, -1);
        mDiscrollveToBgColor = a.getColor(R.styleable.AnimatorFrame_discrollve_toBgColor, -1);
        a.recycle();
    }
}
