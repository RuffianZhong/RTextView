package com.ruffian.library;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * RTextView
 * <p>
 * 1. RTextView 让你从此不再编写和管理大量 selector 文件（这个太可恨了）
 * 2. RTextView 改造了 drawableLeft/drawableXXX 图片的大小，从此你不在需要使用 LinearLayout + ImageView + TextView 就能够直接实现文字带图片的功能，关键还能设置icon大小
 * 3. RTextView 能够直接设置各种圆角效果： 四周圆角，某一方向圆角，甚至椭圆，圆形都简单实现。 边框效果，虚线边框都是一个属性搞定
 * 4. RTextView 不仅能够定义默认状态的背景，边框，连按下/点击状态通通一起搞定
 * 5. RTextView 按下变色支持：背景色，边框，文字，drawableLeft/xxx （这个赞啊）
 * <p>
 * https://github.com/RuffianZhong/RTextView
 *
 * @author ZhongDaFeng
 */
public class RTextView extends TextView {

    //default value
    public static final int ICON_DIR_LEFT = 1, ICON_DIR_TOP = 2, ICON_DIR_RIGHT = 3, ICON_DIR_BOTTOM = 4;

    //icon
    private int mIconHeight;
    private int mIconWidth;
    private int mIconDirection;

    //corner
    private float mCornerRadius;
    private float mCornerRadiusTopLeft;
    private float mCornerRadiusTopRight;
    private float mCornerRadiusBottomLeft;
    private float mCornerRadiusBottomRight;

    //BorderWidth
    private float mBorderDashWidth = 0;
    private float mBorderDashGap = 0;
    private int mBorderWidthNormal = 0;
    private int mBorderWidthPressed = 0;
    private int mBorderWidthUnable = 0;

    //BorderColor
    private int mBorderColorNormal;
    private int mBorderColorPressed;
    private int mBorderColorUnable;

    //Background
    private int mBackgroundColorNormal;
    private int mBackgroundColorPressed;
    private int mBackgroundColorUnable;
    private GradientDrawable mBackgroundNormal;
    private GradientDrawable mBackgroundPressed;
    private GradientDrawable mBackgroundUnable;

    // Text
    private int mTextColorNormal;
    private int mTextColorPressed;
    private int mTextColorUnable;
    private ColorStateList mTextColorStateList;

    //Icon
    private Drawable mIcon = null;
    private Drawable mIconNormal;
    private Drawable mIconPressed;
    private Drawable mIconUnable;

    //typeface
    private String mTypefacePath;

    private int[][] states = new int[4][];
    private StateListDrawable mStateBackground;
    private float mBorderRadii[] = new float[8];

    /**
     * Cache the touch slop from the context that created the view.
     */
    private int mTouchSlop;
    private Context mContext;
    private GestureDetector mGestureDetector;

    /**
     * 是否设置对应的属性
     */
    private boolean mHasPressedBgColor = false;
    private boolean mHasUnableBgColor = false;
    private boolean mHasPressedBorderColor = false;
    private boolean mHasUnableBorderColor = false;
    private boolean mHasPressedBorderWidth = false;
    private boolean mHasUnableBorderWidth = false;

    public RTextView(Context context) {
        this(context, null);
    }

    public RTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context, new SimpleOnGesture());
        initAttributeSet(context, attrs);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            if (mIconNormal != null) {
                mIcon = mIconNormal;
                setIcon();
            }
        } else {
            if (mIconUnable != null) {
                mIcon = mIconUnable;
                setIcon();
            }
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (!isEnabled()) return true;
        mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP://抬起
                if (mIconNormal != null) {
                    mIcon = mIconNormal;
                    setIcon();
                }
                break;
            case MotionEvent.ACTION_MOVE://移动
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (isOutsideView(x, y)) {
                    if (mIconNormal != null) {
                        mIcon = mIconNormal;
                        setIcon();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL://父级控件获取控制权
                if (mIconNormal != null) {
                    mIcon = mIconNormal;
                    setIcon();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 初始化控件属性
     *
     * @param context
     * @param attrs
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            setup();
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RTextView);
        //corner
        mCornerRadius = a.getDimensionPixelSize(R.styleable.RTextView_corner_radius, -1);
        mCornerRadiusTopLeft = a.getDimensionPixelSize(R.styleable.RTextView_corner_radius_top_left, 0);
        mCornerRadiusTopRight = a.getDimensionPixelSize(R.styleable.RTextView_corner_radius_top_right, 0);
        mCornerRadiusBottomLeft = a.getDimensionPixelSize(R.styleable.RTextView_corner_radius_bottom_left, 0);
        mCornerRadiusBottomRight = a.getDimensionPixelSize(R.styleable.RTextView_corner_radius_bottom_right, 0);
        //border
        mBorderDashWidth = a.getDimensionPixelSize(R.styleable.RTextView_border_dash_width, 0);
        mBorderDashGap = a.getDimensionPixelSize(R.styleable.RTextView_border_dash_gap, 0);
        mBorderWidthNormal = a.getDimensionPixelSize(R.styleable.RTextView_border_width_normal, 0);
        mBorderWidthPressed = a.getDimensionPixelSize(R.styleable.RTextView_border_width_pressed, 0);
        mBorderWidthUnable = a.getDimensionPixelSize(R.styleable.RTextView_border_width_unable, 0);
        mBorderColorNormal = a.getColor(R.styleable.RTextView_border_color_normal, Color.TRANSPARENT);
        mBorderColorPressed = a.getColor(R.styleable.RTextView_border_color_pressed, Color.TRANSPARENT);
        mBorderColorUnable = a.getColor(R.styleable.RTextView_border_color_unable, Color.TRANSPARENT);
        //icon
        //Vector兼容处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIconNormal = a.getDrawable(R.styleable.RTextView_icon_src_normal);
            mIconPressed = a.getDrawable(R.styleable.RTextView_icon_src_pressed);
            mIconUnable = a.getDrawable(R.styleable.RTextView_icon_src_unable);
        } else {
            int normalId = a.getResourceId(R.styleable.RTextView_icon_src_normal, -1);
            int pressedId = a.getResourceId(R.styleable.RTextView_icon_src_pressed, -1);
            int unableId = a.getResourceId(R.styleable.RTextView_icon_src_unable, -1);

            if (normalId != -1)
                mIconNormal = AppCompatResources.getDrawable(context, normalId);
            if (pressedId != -1)
                mIconPressed = AppCompatResources.getDrawable(context, pressedId);
            if (unableId != -1)
                mIconUnable = AppCompatResources.getDrawable(context, unableId);
        }
        mIconWidth = a.getDimensionPixelSize(R.styleable.RTextView_icon_width, 0);
        mIconHeight = a.getDimensionPixelSize(R.styleable.RTextView_icon_height, 0);
        mIconDirection = a.getInt(R.styleable.RTextView_icon_direction, ICON_DIR_LEFT);
        //text
        mTextColorNormal = a.getColor(R.styleable.RTextView_text_color_normal, getCurrentTextColor());
        mTextColorPressed = a.getColor(R.styleable.RTextView_text_color_pressed, getCurrentTextColor());
        mTextColorUnable = a.getColor(R.styleable.RTextView_text_color_unable, getCurrentTextColor());
        //background
        mBackgroundColorNormal = a.getColor(R.styleable.RTextView_background_normal, 0);
        mBackgroundColorPressed = a.getColor(R.styleable.RTextView_background_pressed, 0);
        mBackgroundColorUnable = a.getColor(R.styleable.RTextView_background_unable, 0);
        //typeface
        mTypefacePath = a.getString(R.styleable.RTextView_text_typeface);

        a.recycle();

        mHasPressedBgColor = mBackgroundColorPressed != 0;
        mHasUnableBgColor = mBackgroundColorUnable != 0;
        mHasPressedBorderColor = mBorderColorPressed != 0;
        mHasUnableBorderColor = mBorderColorUnable != 0;
        mHasPressedBorderWidth = mBorderWidthPressed != 0;
        mHasUnableBorderWidth = mBorderWidthUnable != 0;

        //setup
        setup();

    }

    /**
     * 设置
     */
    private void setup() {

        mBackgroundNormal = new GradientDrawable();
        mBackgroundPressed = new GradientDrawable();
        mBackgroundUnable = new GradientDrawable();

        Drawable drawable = getBackground();
        if (drawable != null && drawable instanceof StateListDrawable) {
            mStateBackground = (StateListDrawable) drawable;
        } else {
            mStateBackground = new StateListDrawable();
        }

        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgColor) {
            mBackgroundColorPressed = mBackgroundColorNormal;
        }
        if (!mHasUnableBgColor) {
            mBackgroundColorUnable = mBackgroundColorNormal;
        }

        mBackgroundNormal.setColor(mBackgroundColorNormal);
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        mBackgroundUnable.setColor(mBackgroundColorUnable);

        //pressed, focused, normal, unable
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[3] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{android.R.attr.state_enabled};
        mStateBackground.addState(states[0], mBackgroundPressed);
        mStateBackground.addState(states[1], mBackgroundPressed);
        mStateBackground.addState(states[3], mBackgroundUnable);
        mStateBackground.addState(states[2], mBackgroundNormal);

        /**
         * icon
         */
        if (isEnabled() == false) {
            mIcon = mIconUnable;
        } else {
            mIcon = mIconNormal;
        }

        /**
         * 设置边框默认值
         */
        if (!mHasPressedBorderWidth) {
            mBorderWidthPressed = mBorderWidthNormal;
        }
        if (!mHasUnableBorderWidth) {
            mBorderWidthUnable = mBorderWidthNormal;
        }
        if (!mHasPressedBorderColor) {
            mBorderColorPressed = mBorderColorNormal;
        }
        if (!mHasUnableBorderColor) {
            mBorderColorUnable = mBorderColorNormal;
        }

        if (mBackgroundColorNormal == 0 && mBackgroundColorUnable == 0 && mBackgroundColorPressed == 0) {//未设置自定义背景色
           /* if (mBorderColorPressed == 0 && mBorderColorUnable == 0 && mBorderColorNormal == 0) {//未设置自定义边框
                //获取原生背景并设置
                setBackgroundState(true);
            } else {
                setBackgroundState(false);
            }*/
            //获取原生背景并设置
            setBackgroundState(true);
        } else {
            //设置背景资源
            setBackgroundState(false);
        }


        //设置文本颜色
        setTextColor();

        //设置边框
        setBorder();

        //设置ICON
        setIcon();

        //设置圆角
        setRadius();

        //设置文本字体样式
        setTypeface();

    }

    /**
     * 是否移出view
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isOutsideView(int x, int y) {
        boolean flag = false;
        // Be lenient about moving outside of buttons
        if ((x < 0 - mTouchSlop) || (x >= getWidth() + mTouchSlop) ||
                (y < 0 - mTouchSlop) || (y >= getHeight() + mTouchSlop)) {
            // Outside button
            flag = true;
        }
        return flag;
    }

    /*********************
     * BackgroundColor
     ********************/

    public RTextView setStateBackgroundColor(int normal, int pressed, int unable) {
        mBackgroundColorNormal = normal;
        mBackgroundColorPressed = pressed;
        mBackgroundColorUnable = unable;
        mHasPressedBgColor = true;
        mHasUnableBgColor = true;
        mBackgroundNormal.setColor(mBackgroundColorNormal);
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        mBackgroundUnable.setColor(mBackgroundColorUnable);
        setBackgroundState(false);
        return this;
    }

    public int getBackgroundColorNormal() {
        return mBackgroundColorNormal;
    }

    public RTextView setBackgroundColorNormal(int colorNormal) {
        this.mBackgroundColorNormal = colorNormal;
        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgColor) {
            mBackgroundColorPressed = mBackgroundColorNormal;
            mBackgroundPressed.setColor(mBackgroundColorPressed);
        }
        if (!mHasUnableBgColor) {
            mBackgroundColorUnable = mBackgroundColorNormal;
            mBackgroundUnable.setColor(mBackgroundColorUnable);
        }
        mBackgroundNormal.setColor(mBackgroundColorNormal);
        setBackgroundState(false);
        return this;
    }

    public int getBackgroundColorPressed() {
        return mBackgroundColorPressed;
    }

    public RTextView setBackgroundColorPressed(int colorPressed) {
        this.mBackgroundColorPressed = colorPressed;
        this.mHasPressedBgColor = true;
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        setBackgroundState(false);
        return this;
    }

    public int getBackgroundColorUnable() {
        return mBackgroundColorUnable;
    }

    public RTextView setBackgroundColorUnable(int colorUnable) {
        this.mBackgroundColorUnable = colorUnable;
        this.mHasUnableBgColor = true;
        mBackgroundUnable.setColor(mBackgroundColorUnable);
        setBackgroundState(false);
        return this;
    }

    private void setBackgroundState(boolean unset) {

        //未设置自定义属性,并且设置背景颜色时
        Drawable drawable = getBackground();
        if (unset && drawable instanceof ColorDrawable) {
            int color = ((ColorDrawable) drawable).getColor();
            setStateBackgroundColor(color, color, color);//获取背景颜色值设置 StateListDrawable
        }

        //设置背景资源
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(unset ? drawable : mStateBackground);
        } else {
            setBackground(unset ? drawable : mStateBackground);
        }
    }


    /************************
     * Typeface
     ************************/

    public RTextView setTypeface(String typefacePath) {
        this.mTypefacePath = typefacePath;
        setTypeface();
        return this;
    }

    public String getTypefacePath() {
        return mTypefacePath;
    }

    private void setTypeface() {
        if (!TextUtils.isEmpty(mTypefacePath)) {
            AssetManager assetManager = mContext.getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager, mTypefacePath);
            setTypeface(typeface);
        }
    }

    /************************
     * Icon
     ************************/

    public RTextView setIconNormal(Drawable icon) {
        this.mIconNormal = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    public Drawable getIconNormal() {
        return mIconNormal;
    }

    public RTextView setIconPressed(Drawable icon) {
        this.mIconPressed = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    public Drawable getIconPressed() {
        return mIconPressed;
    }

    public RTextView setIconUnable(Drawable icon) {
        this.mIconUnable = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    public Drawable getIconUnable() {
        return mIconUnable;
    }

    public RTextView setIconSize(int iconWidth, int iconHeight) {
        this.mIconWidth = iconWidth;
        this.mIconHeight = iconHeight;
        setIcon();
        return this;
    }

    public RTextView setIconWidth(int iconWidth) {
        this.mIconWidth = iconWidth;
        setIcon();
        return this;
    }

    public int getIconWidth() {
        return mIconWidth;
    }

    public RTextView setIconHeight(int iconHeight) {
        this.mIconHeight = iconHeight;
        setIcon();
        return this;
    }

    public int getIconHeight() {
        return mIconHeight;
    }

    public RTextView setIconDirection(int iconDirection) {
        this.mIconDirection = iconDirection;
        setIcon();
        return this;
    }

    public int getIconDirection() {
        return mIconDirection;
    }

    private void setIcon() {
        //未设置图片大小
        if (mIconHeight == 0 && mIconWidth == 0) {
            if (mIcon != null) {
                mIconWidth = mIcon.getIntrinsicWidth();
                mIconHeight = mIcon.getIntrinsicHeight();
            }
        }
        setIcon(mIcon, mIconWidth, mIconHeight, mIconDirection);
    }

    private void setIcon(Drawable drawable, int width, int height, int direction) {
        if (drawable != null) {
            if (width != 0 && height != 0) {
                drawable.setBounds(0, 0, width, height);
            }
            switch (direction) {
                case ICON_DIR_LEFT:
                    setCompoundDrawables(drawable, null, null, null);
                    break;
                case ICON_DIR_TOP:
                    setCompoundDrawables(null, drawable, null, null);
                    break;
                case ICON_DIR_RIGHT:
                    setCompoundDrawables(null, null, drawable, null);
                    break;
                case ICON_DIR_BOTTOM:
                    setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        }
    }

    /************************
     * text color
     ************************/

    public RTextView setTextColorNormal(int textColor) {
        this.mTextColorNormal = textColor;
        if (mTextColorPressed == 0) {
            mTextColorPressed = mTextColorNormal;
        }
        if (mTextColorUnable == 0) {
            mTextColorUnable = mTextColorNormal;
        }
        setTextColor();
        return this;
    }

    public int getTextColorNormal() {
        return mTextColorNormal;
    }

    public RTextView setPressedTextColor(int textColor) {
        this.mTextColorPressed = textColor;
        setTextColor();
        return this;
    }

    public int getPressedTextColor() {
        return mTextColorPressed;
    }

    public RTextView setTextColorUnable(int textColor) {
        this.mTextColorUnable = textColor;
        setTextColor();
        return this;
    }

    public int getTextColorUnable() {
        return mTextColorUnable;
    }

    public void setTextColor(int normal, int pressed, int unable) {
        this.mTextColorNormal = normal;
        this.mTextColorPressed = pressed;
        this.mTextColorUnable = unable;
        setTextColor();
    }

    private void setTextColor() {
        int[] colors = new int[]{mTextColorPressed, mTextColorPressed, mTextColorNormal, mTextColorUnable};
        mTextColorStateList = new ColorStateList(states, colors);
        setTextColor(mTextColorStateList);
    }

    /*********************
     * border
     *********************/

    public RTextView setBorderWidthNormal(int width) {
        this.mBorderWidthNormal = width;
        if (!mHasPressedBorderWidth) {
            mBorderWidthPressed = mBorderWidthNormal;
            setBorderPressed();
        }
        if (!mHasUnableBorderWidth) {
            mBorderWidthUnable = mBorderWidthNormal;
            setBorderUnable();
        }
        setBorderNormal();
        return this;
    }

    public int getBorderWidthNormal() {
        return mBorderWidthNormal;
    }

    public RTextView setBorderColorNormal(int color) {
        this.mBorderColorNormal = color;
        if (!mHasPressedBorderColor) {
            mBorderColorPressed = mBorderColorNormal;
            setBorderPressed();
        }
        if (!mHasUnableBorderColor) {
            mBorderColorUnable = mBorderColorNormal;
            setBorderUnable();
        }
        setBorderNormal();
        return this;
    }

    public int getBorderColorNormal() {
        return mBorderColorNormal;
    }

    public RTextView setBorderWidthPressed(int width) {
        this.mBorderWidthPressed = width;
        this.mHasPressedBorderWidth = true;
        setBorderPressed();
        return this;
    }

    public int getBorderWidthPressed() {
        return mBorderWidthPressed;
    }

    public RTextView setBorderColorPressed(int color) {
        this.mBorderColorPressed = color;
        this.mHasPressedBorderColor = true;
        setBorderPressed();
        return this;
    }

    public int getBorderColorPressed() {
        return mBorderColorPressed;
    }

    public RTextView setBorderWidthUnable(int width) {
        this.mBorderWidthUnable = width;
        this.mHasUnableBorderWidth = true;
        setBorderUnable();
        return this;
    }

    public int getBorderWidthUnable() {
        return mBorderWidthUnable;
    }

    public RTextView setBorderColorUnable(int color) {
        this.mBorderColorUnable = color;
        this.mHasUnableBorderColor = true;
        setBorderUnable();
        return this;
    }

    public int getBorderColorUnable() {
        return mBorderColorUnable;
    }

    public void setBorderWidth(int normal, int pressed, int unable) {
        this.mBorderWidthNormal = normal;
        this.mBorderWidthPressed = pressed;
        this.mBorderWidthUnable = unable;
        this.mHasPressedBorderWidth = true;
        this.mHasUnableBorderWidth = true;
        setBorder();
    }

    public void setBorderColor(int normal, int pressed, int unable) {
        this.mBorderColorNormal = normal;
        this.mBorderColorPressed = pressed;
        this.mBorderColorUnable = unable;
        this.mHasPressedBorderColor = true;
        this.mHasUnableBorderColor = true;
        setBorder();
    }

    public void setBorderDashWidth(float dashWidth) {
        this.mBorderDashWidth = dashWidth;
        setBorder();
    }

    public float getBorderDashWidth() {
        return mBorderDashWidth;
    }

    public void setBorderDashGap(float dashGap) {
        this.mBorderDashGap = dashGap;
        setBorder();
    }

    public float getBorderDashGap() {
        return mBorderDashGap;
    }

    public void setBorderDash(float dashWidth, float dashGap) {
        this.mBorderDashWidth = dashWidth;
        this.mBorderDashGap = dashGap;
        setBorder();
    }

    private void setBorder() {
        mBackgroundNormal.setStroke(mBorderWidthNormal, mBorderColorNormal, mBorderDashWidth, mBorderDashGap);
        mBackgroundPressed.setStroke(mBorderWidthPressed, mBorderColorPressed, mBorderDashWidth, mBorderDashGap);
        mBackgroundUnable.setStroke(mBorderWidthUnable, mBorderColorUnable, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }

    private void setBorderNormal() {
        mBackgroundNormal.setStroke(mBorderWidthNormal, mBorderColorNormal, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }

    private void setBorderPressed() {
        mBackgroundPressed.setStroke(mBorderWidthPressed, mBorderColorPressed, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }

    private void setBorderUnable() {
        mBackgroundUnable.setStroke(mBorderWidthUnable, mBorderColorUnable, mBorderDashWidth, mBorderDashGap);
        setBackgroundState(false);
    }


    /*********************
     * radius
     ********************/

    public void setCornerRadius(float radius) {
        this.mCornerRadius = radius;
        setRadius();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public RTextView setCornerRadiusTopLeft(float topLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        setRadius();
        return this;
    }

    public float getCornerRadiusTopLeft() {
        return mCornerRadiusTopLeft;
    }

    public RTextView setCornerRadiusTopRight(float topRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopRight = topRight;
        setRadius();
        return this;
    }

    public float getCornerRadiusTopRight() {
        return mCornerRadiusTopRight;
    }

    public RTextView setCornerRadiusBottomRight(float bottomRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomRight = bottomRight;
        setRadius();
        return this;
    }

    public float getCornerRadiusBottomRight() {
        return mCornerRadiusBottomRight;
    }

    public RTextView setCornerRadiusBottomLeft(float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomLeft = bottomLeft;
        setRadius();
        return this;
    }

    public float getCornerRadiusBottomLeft() {
        return mCornerRadiusBottomLeft;
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        this.mCornerRadiusTopRight = topRight;
        this.mCornerRadiusBottomRight = bottomRight;
        this.mCornerRadiusBottomLeft = bottomLeft;
        setRadius();
    }

    private void setRadiusRadii() {
        mBackgroundNormal.setCornerRadii(mBorderRadii);
        mBackgroundPressed.setCornerRadii(mBorderRadii);
        mBackgroundUnable.setCornerRadii(mBorderRadii);
        setBackgroundState(false);
    }

    private void setRadius() {
        if (mCornerRadius >= 0) {
            mBorderRadii[0] = mCornerRadius;
            mBorderRadii[1] = mCornerRadius;
            mBorderRadii[2] = mCornerRadius;
            mBorderRadii[3] = mCornerRadius;
            mBorderRadii[4] = mCornerRadius;
            mBorderRadii[5] = mCornerRadius;
            mBorderRadii[6] = mCornerRadius;
            mBorderRadii[7] = mCornerRadius;
            setRadiusRadii();
            return;
        }

        if (mCornerRadius < 0) {
            mBorderRadii[0] = mCornerRadiusTopLeft;
            mBorderRadii[1] = mCornerRadiusTopLeft;
            mBorderRadii[2] = mCornerRadiusTopRight;
            mBorderRadii[3] = mCornerRadiusTopRight;
            mBorderRadii[4] = mCornerRadiusBottomRight;
            mBorderRadii[5] = mCornerRadiusBottomRight;
            mBorderRadii[6] = mCornerRadiusBottomLeft;
            mBorderRadii[7] = mCornerRadiusBottomLeft;
            setRadiusRadii();
            return;
        }
    }

    private float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * 手势处理
     */
    class SimpleOnGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onShowPress(MotionEvent e) {
            if (mIconPressed != null) {
                mIcon = mIconPressed;
                setIcon();
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mIconNormal != null) {
                mIcon = mIconNormal;
                setIcon();
            }
            return false;
        }
    }

}
