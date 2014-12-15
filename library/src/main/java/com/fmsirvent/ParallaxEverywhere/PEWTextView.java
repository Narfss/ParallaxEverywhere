package com.fmsirvent.ParallaxEverywhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.fmsirvent.ParallaxEverywhere.Utils.InterpolatorSelector;

/**
 * Created by fmsirvent on 03/11/14.
 */
public class PEWTextView extends TextView {
    private boolean reverseX = false;
    private boolean reverseY = false;
    private int scrollSpaceX = 0;
    private int scrollSpaceY = 0;
    private boolean blockParallaxX = false;
    private boolean blockParallaxY = false;
    private int screenHeight;
    private int screenWidth;
    private float heightView;
    private float widthView;
    private Interpolator interpolator;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = null;

    public PEWTextView(Context context) {
        super(context);
        if (!isInEditMode()) {
            parallaxAnimation();
        }
    }

    public PEWTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            checkAttributes(attrs);
            parallaxAnimation();
        }
    }

    public PEWTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            checkAttributes(attrs);
            parallaxAnimation();
        }
    }

    private void checkAttributes(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PEWAttrs);
        int reverse = arr.getInt(R.styleable.PEWAttrs_reverse, 1);

        blockParallaxX = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_x, false);
        blockParallaxY = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_y, false);

        reverseX = false;
        reverseY = false;
        switch (reverse) {
            case AttrConstants.REVERSE_NONE:
                break;
            case AttrConstants.REVERSE_X:
                reverseX = true;
                break;
            case AttrConstants.REVERSE_Y:
                reverseY = true;
                break;
            case AttrConstants.REVERSE_BOTH:
                reverseX = true;
                reverseY = true;
                break;
        }

        scrollSpaceX = arr.getDimensionPixelSize(R.styleable.PEWAttrs_parallax_x, 0);
        scrollSpaceY = arr.getDimensionPixelSize(R.styleable.PEWAttrs_parallax_y, 0);


        int interpolationId = arr.getInt(R.styleable.PEWAttrs_interpolation, 0);

        interpolator = InterpolatorSelector.interpolatorId(interpolationId);

        arr.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                applyParallax();
            }
        };

        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                heightView = (float) getHeight();
                widthView = (float) getWidth();

                applyParallax();
            }
        };

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
        viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);

        parallaxAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        } else {
            viewTreeObserver.removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void parallaxAnimation() {
        initSizeScreen();

        applyParallax();
    }


    private void initSizeScreen() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
            screenWidth = size.x;
        } else {
            screenHeight = display.getHeight();
            screenWidth = display.getWidth();
        }
    }

    private void applyParallax() {
        int[] location = new int[2];
        getLocationOnScreen(location);

        if (scrollSpaceY != 0
                && !blockParallaxY) {
            float locationY = (float) location[1];
            float locationUsableY = locationY + heightView / 2;
            float scrollDeltaY = locationUsableY / screenHeight;

            float interpolatedScrollDeltaY = interpolator.getInterpolation(scrollDeltaY);

            if (reverseY)
                setMyScrollY((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f), 0.5f) * -scrollSpaceY));
            else
                setMyScrollY((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f), 0.5f) * scrollSpaceY));
        }

        if (scrollSpaceX != 0
                && !blockParallaxX) {
            float locationX = (float) location[0];
            float locationUsableX = locationX + widthView / 2;
            float scrollDeltaX = locationUsableX / screenWidth;

            float interpolatedScrollDeltaX = interpolator.getInterpolation(scrollDeltaX);

            if (reverseX) {
                setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * -scrollSpaceX));
            } else {
                setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * scrollSpaceX));
            }
        }
    }

    private void setMyScrollX(int value) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setScrollX(value);
        } else {
            scrollTo(value, getScrollY());
        }
    }

    private void setMyScrollY(int value) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setScrollY(value);
        } else {
            scrollTo(getScrollX(),value);
        }
    }
}
