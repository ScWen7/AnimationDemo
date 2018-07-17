package scwen.com.animationtest;

import android.view.View;

/**
 * Created by xxh on 2018/7/16.
 */

public abstract class AnimatorProvider {

    protected View mView;
    protected AnimatorLayoutParam mLayoutParam;

    public AnimatorProvider(View view,AnimatorLayoutParam layoutParam) {
        mView = view;
        mLayoutParam = layoutParam;
    }

    abstract void onAnim(float ratio);

    abstract void resetAnim();
}
