package com.techstar.intelligentpatrolplatform.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.techstar.intelligentpatrolplatform.R;
import com.techstar.intelligentpatrolplatform.utils.LogUtils;

/**
 * author lrzg on 16/11/10.
 * 描述：自定义标题栏
 */

public class CustomTitleBarView extends FrameLayout {

    private CustomTitleBarViewDelegate mDelegate;

    public interface OnTitleBarListener {
        void onLeftImgClick();

        void onLeftTextClick();

        void onRightImgClick();

        void onRightTextClick();
    }

    public CustomTitleBarView(Context context) {
        this(context, null);
    }

    public CustomTitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = createCustomTitleBarViewDelegate(context, attrs, defStyleAttr);
    }

    private CustomTitleBarViewDelegate createCustomTitleBarViewDelegate(Context context, AttributeSet attrs, int defStyleAttr) {
        return new CustomTitleBarViewDelegate(this, context, attrs, defStyleAttr);
    }

    public void setOnTitleListener(OnTitleBarListener onLJJTitleBarListener) {
        mDelegate.setListener(onLJJTitleBarListener);
    }

    /**
     * 设置 title
     *
     * @param title title
     */
    public void setTitle(String title) {
        mDelegate.setTitle(title);
    }

    /**
     * 设置 titleColor
     *
     * @param titleColor titleColor
     */
    public void setTitleColor(int titleColor) {
        mDelegate.setTitleColor(titleColor);
    }

    /**
     * 设置 titleSize
     *
     * @param titleSize titleSize
     */
    public void setTitleSize(int titleSize) {
        mDelegate.setTitleSize(titleSize);
    }

    /**
     * 设置左侧标题
     *
     * @param leftText leftText
     */
    public void setLeftText(String leftText) {
        mDelegate.setLeftText(leftText);
    }

    /**
     * 设置左侧标题颜色
     *
     * @param leftTextColor leftTextColor
     */
    public void setLeftTextColorr(int leftTextColor) {
        mDelegate.setLeftTextColor(leftTextColor);
    }

    /**
     * 设置左侧标题大小
     *
     * @param leftTextSize leftTextSize
     */
    public void setLeftTextSize(int leftTextSize) {
        mDelegate.setLeftTextSize(leftTextSize);
    }

    /**
     * 设置左侧图标
     *
     * @param res res
     */
    public void setLeftImg(int res) {
        mDelegate.setLeftImg(res);
    }

    public void setLeftImg(Drawable drawable) {
        mDelegate.setLeftImg(drawable);
    }

    /**
     * 设置右侧标题
     *
     * @param rightText rightText
     */
    public void setRightText(String rightText) {
        mDelegate.setRightText(rightText);
    }

    /**
     * 设置右侧标题颜色
     *
     * @param rightTextColorr rightTextColorr
     */
    public void setRightTextColorr(int rightTextColorr) {
        mDelegate.setLeftTextColor(rightTextColorr);
    }

    /**
     * 设置右侧标题大小
     *
     * @param rightTextSize rightTextSize
     */
    public void setRightTextSize(int rightTextSize) {
        mDelegate.setLeftTextSize(rightTextSize);
    }


    public void setRightImg(int res) {
        mDelegate.setRightImg(res);
    }

    /**
     * 设置右侧图标
     *
     * @param drawable drawable
     */
    public void setRightImg(Drawable drawable) {
        mDelegate.setRightImg(drawable);
    }

    private static class CustomTitleBarViewDelegate implements OnClickListener {

        CustomTitleBarView mDelegator;
        Context mContext;

        private TextView mTitle_text;

        private ImageButton mLeft_img;
        private TextView mLeft_text;

        private ImageButton mRight_img;
        private TextView mRight_text;
        private OnTitleBarListener mListener;


        CustomTitleBarViewDelegate(CustomTitleBarView delegator, Context context, AttributeSet attrs, int defStyleAttr) {

            mDelegator = delegator;
            mContext = context;

            LayoutInflater.from(context).inflate(R.layout.title_bar, delegator, true);
            mTitle_text = (TextView) mDelegator.findViewById(R.id.text_bar_title);
            mLeft_img = (ImageButton) mDelegator.findViewById(R.id.img_bar_left);
            mLeft_text = (TextView) mDelegator.findViewById(R.id.text_bar_left);
            mRight_img = (ImageButton) mDelegator.findViewById(R.id.img_bar_right);
            mRight_text = (TextView) mDelegator.findViewById(R.id.text_bar_right);

            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBarView);

            final String title = typedArray.getString(R.styleable.CustomTitleBarView_ipp_title);
            final float titleSize = typedArray.getDimensionPixelSize(R.styleable.CustomTitleBarView_ipp_titleSize, mDelegator.sp2px(16));
            final int titleColor = typedArray.getColor(R.styleable.CustomTitleBarView_ipp_titleColor, context.getResources().getColor(R.color.main_text_black));

            final String rightTitle = typedArray.getString(R.styleable.CustomTitleBarView_ipp_rightText);
            final float rightTitleSize = typedArray.getDimensionPixelSize(R.styleable.CustomTitleBarView_ipp_rightTextSize, mDelegator.sp2px(11));
            final ColorStateList rightTitleColor = typedArray.getColorStateList(R.styleable.CustomTitleBarView_ipp_rightTextColor);
            final Drawable drawableRight = typedArray.getDrawable(R.styleable.CustomTitleBarView_ipp_rightImg);

            final String leftTitle = typedArray.getString(R.styleable.CustomTitleBarView_ipp_leftText);
            final float leftTitleSize = typedArray.getDimensionPixelSize(R.styleable.CustomTitleBarView_ipp_leftTextSize, mDelegator.sp2px(16));
            final ColorStateList leftTitleColor = typedArray.getColorStateList(R.styleable.CustomTitleBarView_ipp_leftTextColor);

            final Drawable drawableLeft = typedArray.getDrawable(R.styleable.CustomTitleBarView_ipp_leftImg);

            typedArray.recycle();

            setTitle(title);
            setTitleColor(titleColor);
            setTitleSize(mDelegator.dx2sp(titleSize));

            if (drawableLeft != null) {
                setLeftImg(drawableLeft);
            }

            if (!TextUtils.isEmpty(leftTitle)) {
                setLeftText(leftTitle);
                setLeftTextColor(leftTitleColor == null ? context.getResources().getColorStateList(R.color.blue_color_list) : leftTitleColor);
                setLeftTextSize(mDelegator.dx2sp(leftTitleSize));
            }

            if (drawableRight != null) {
                setRightImg(drawableRight);
            }

            if (!TextUtils.isEmpty(rightTitle)) {
                setRightText(rightTitle);
                setRightTextColor(rightTitleColor == null ? context.getResources().getColorStateList(R.color.blue_color_list) : rightTitleColor);
                setRightTextSize(mDelegator.dx2sp(rightTitleSize));
            }
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_bar_left:
                    if (mListener != null) {
                        mListener.onLeftImgClick();
                    }
                    LogUtils.e("left_menu_img");
                    break;
                case R.id.text_bar_left:
                    if (mListener != null) {
                        mListener.onLeftTextClick();
                    }
                    LogUtils.e("left_menu_text");
                    break;
                case R.id.img_bar_right:
                    if (mListener != null) {
                        mListener.onRightImgClick();
                    }
                    LogUtils.e("right_menu_img");
                    break;
                case R.id.text_bar_right:
                    if (mListener != null) {
                        mListener.onRightTextClick();
                    }
                    LogUtils.e("right_menu_text");
                    break;
            }
        }

        private void setListener(OnTitleBarListener listener) {
            mListener = listener;
        }

        private void setTitle(String title) {
            mTitle_text.setText(title);
        }

        private void setTitleColor(int titleColor) {
            mTitle_text.setTextColor(titleColor);
        }

        private void setTitleSize(int titleSize) {
            mTitle_text.setTextSize(titleSize);
        }

        private void setLeftText(String text) {
            mLeft_text.setOnClickListener(this);
            mLeft_text.setText(text);
        }

        private void setLeftTextColor(int leftTextColor) {
            setLeftTextColor(ColorStateList.valueOf(leftTextColor));
        }

        private void setLeftTextColor(ColorStateList colorStateList) {
            mLeft_text.setTextColor(colorStateList);
        }

        private void setLeftTextSize(int leftTextSize) {
            mLeft_text.setTextSize(leftTextSize);
        }

        private void setLeftImg(int res) {
            mLeft_img.setOnClickListener(this);
            mLeft_img.setImageResource(res);
        }

        private void setLeftImg(Drawable drawable) {
            mLeft_img.setOnClickListener(this);
            mLeft_img.setImageDrawable(drawable);
        }

        private void setRightText(String text) {
            mRight_text.setOnClickListener(this);
            mRight_text.setText(text);
        }

        private void setRightTextColor(int rightTextColor) {
            setRightTextColor(ColorStateList.valueOf(rightTextColor));
        }

        private void setRightTextColor(ColorStateList colorStateList) {
            mRight_text.setTextColor(colorStateList);
        }

        private void setRightTextSize(int rightTextSize) {
            mRight_text.setTextSize(rightTextSize);
        }

        private void setRightImg(int res) {
            mRight_img.setOnClickListener(this);
            mRight_img.setImageResource(res);
        }

        private void setRightImg(Drawable drawable) {
            mRight_img.setOnClickListener(this);
            mRight_img.setImageDrawable(drawable);
        }
    }

    private int dx2sp(float px) {
        final float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity + 0.5f);
    }

    private int sp2px(float sp) {
        final float scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scaledDensity + 0.5f);
    }
}
