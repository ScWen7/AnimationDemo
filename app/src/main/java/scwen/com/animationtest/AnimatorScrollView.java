package scwen.com.animationtest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxh on 2018/7/16.
 */

public class AnimatorScrollView extends ScrollView {
    public AnimatorScrollView(Context context) {
        super(context);
    }

    public AnimatorScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatorScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    AnimatorLinearLayout mLinearLayout;
    private Map<View, AnimatorProvider> mAnimatorProviderMap;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            mLinearLayout = (AnimatorLinearLayout) getChildAt(0);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mLinearLayout != null) {
            mAnimatorProviderMap = mLinearLayout.getAnimatorProviderMap();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //注意这里的 t 值为 scrollView 最顶部 内容的坐标
        //例如 scroll向上滑动了  50px  那么 t 的值就为 50

        int scrollViewHeight = getHeight();

        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {

            View childAt = mLinearLayout.getChildAt(i);

            if (mAnimatorProviderMap.containsKey(childAt)) {

                //需要执行动画
                AnimatorProvider provider = mAnimatorProviderMap.get(childAt);

                int childAtTop = childAt.getTop();

                int disTop = childAtTop - t; //子视图 顶部距离 界面顶部 距离

                if (disTop < 0) {

                } else if (disTop < scrollViewHeight ) {



                    //说明视图 处于可见位置
                    int visiableGap = scrollViewHeight - disTop;

                    if(visiableGap<=childAt.getHeight()) {

                        float ratio = visiableGap * 1.0f / childAt.getHeight();

                        provider.onAnim(ratio);
                    }else {
                        provider.onAnim(1.0f);
                    }

                } else {
//                    //说明视图 在下方 不可见
                    provider.resetAnim();
                }


            } else {
                continue;
            }


        }
    }
}
