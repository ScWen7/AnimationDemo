package scwen.com.animationtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxh on 2018/7/16.
 */

public class AnimatorLinearLayout extends LinearLayout {


    private Map<View, AnimatorProvider> mAnimatorProviderMap = new HashMap<>();

    public Map<View, AnimatorProvider> getAnimatorProviderMap() {
        return mAnimatorProviderMap;
    }

    public AnimatorLinearLayout(Context context) {
        super(context);
    }

    public AnimatorLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatorLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AnimatorLayoutParam(getContext(), attrs);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {

        AnimatorLayoutParam layoutParam = (AnimatorLayoutParam) params;

        if (isDiscrollvable(layoutParam)) {
            mAnimatorProviderMap.put(child, new AnimatorProviderImpl(child,layoutParam));
        }

        super.addView(child, params);
    }

    private boolean isDiscrollvable(AnimatorLayoutParam layoutParams) {
        return layoutParams.mDiscrollveAlpha ||
                layoutParams.mDiscrollveScaleX ||
                layoutParams.mDiscrollveScaleY ||
                layoutParams.mDisCrollveTranslation != -1 ||
                (layoutParams.mDiscrollveFromBgColor != -1 &&
                        layoutParams.mDiscrollveToBgColor != -1);
    }

}
