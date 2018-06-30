package com.fmsirvent.ParallaxEverywhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.fmsirvent.ParallaxEverywhere.Utils.InterpolatorSelector;

import java.lang.Override;

/**
 * Created by fmsirvent on 03/11/14.
 *
 * Changed by @alexanderbezverhni on 10/06/15 - added support for parallax distance limit.
 */
public class PEWImageView extends ImageView {

    public static final float FACTOR_DEFAULT = -1.0f;

    public boolean reverseX = false;
    public boolean reverseY = false;
    public boolean updateOnDraw = false;
    public boolean blockParallaxX = false;
    public boolean blockParallaxY = false;

    private int screenWidth;
    private int screenHeight;
    private float scrollSpaceX = 0;
    private float scrollSpaceY = 0;
    private float heightImageView;
    private float widthImageView;
    private float factor = FACTOR_DEFAULT;

    private Interpolator interpolator = new LinearInterpolator();

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;
    private ViewTreeObserver.OnGlobalLayoutListener  mOnGlobalLayoutListener = null;
    private ViewTreeObserver.OnDrawListener onDrawListener = null;

    public PEWImageView(Context context) {
        super(context);
        checkScale();
    }

    public PEWImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            checkAttributes(attrs);
        }
        checkScale();
    }

    public PEWImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            checkAttributes(attrs);
        }
        checkScale();
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
                heightImageView = (float) getHeight();
                widthImageView = (float) getWidth();

                applyParallax();
            }
        };

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
        viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);

        if (updateOnDraw
                && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            onDrawListener = new ViewTreeObserver.OnDrawListener() {
                @Override
                public void onDraw() {
                    applyParallax();
                }
            };
            viewTreeObserver.addOnDrawListener(onDrawListener);
        }

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
        if (updateOnDraw
            && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewTreeObserver.removeOnDrawListener(onDrawListener);
        }
        super.onDetachedFromWindow();
    }

    private boolean checkScale() {
        switch (getScaleType()) {
            case CENTER:
            case CENTER_CROP:
            case CENTER_INSIDE:
                return true;
            case FIT_CENTER:
                Log.d("ParallaxEverywhere", "Scale type firCenter unsupported");
                break;
            case FIT_END:
                Log.d("ParallaxEverywhere", "Scale type fitEnd unsupported");
                break;
            case FIT_START:
                Log.d("ParallaxEverywhere", "Scale type fitStart unsupported");
                break;
            case FIT_XY:
                Log.d("ParallaxEverywhere", "Scale type fitXY unsupported");
                break;
            case MATRIX:
                Log.d("ParallaxEverywhere", "Scale type matrix unsupported");
                break;
        }
        return false;
    }

    private void checkAttributes(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PEWAttrs);
        int reverse = arr.getInt(R.styleable.PEWAttrs_reverse, 1);

        updateOnDraw = arr.getBoolean(R.styleable.PEWAttrs_update_onDraw, false);

        blockParallaxX = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_x, false);
        blockParallaxY = arr.getBoolean(R.styleable.PEWAttrs_block_parallax_y, false);

        float factor = arr.getFloat(R.styleable.PEWAttrs_factor, FACTOR_DEFAULT);
        if (factor != FACTOR_DEFAULT)
            setFactor(factor);

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

        checkScale();

        int interpolationId = arr.getInt(R.styleable.PEWAttrs_interpolation, 0);

        interpolator = InterpolatorSelector.interpolatorId(interpolationId);

        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getDrawable() != null) {

            int dheight = getDrawable().getIntrinsicHeight();
            int dwidth = getDrawable().getIntrinsicWidth();
            int vheight = getMeasuredHeight();
            int vwidth = getMeasuredWidth();

            float scale;

            float dnewHeight = 0;
            float dnewWidth = 0;

            switch (getScaleType()) {
                case CENTER_CROP:
                case CENTER:
                case CENTER_INSIDE:
                    if (dwidth * vheight > vwidth * dheight) {
                        scale = (float) vheight / (float) dheight;
                        dnewWidth = dwidth * scale;
                        dnewHeight = vheight;
                    } else {
                        scale = (float) vwidth / (float) dwidth;
                        dnewWidth = vwidth;
                        dnewHeight = dheight * scale;
                    }
                    break;
                case FIT_CENTER:
                case FIT_END:
                case FIT_START:
                case FIT_XY:
                case MATRIX:
                    break;
            }

            scrollSpaceY = getScrollSpace(vheight, dnewHeight, factor);
            scrollSpaceX = getScrollSpace(vwidth, dnewWidth, factor);
        }
        applyParallax();
    }

    private float getScrollSpace(int viewEdgeSize, float imageEdgeSize, float factor){
        if (viewEdgeSize > imageEdgeSize){
            return 0;
        }

        float maxScrollSpace = imageEdgeSize - viewEdgeSize;
        if (factor == FACTOR_DEFAULT) {
            return maxScrollSpace;
        }

        float factored = viewEdgeSize * (factor - 1.0f);
        return Math.min(factored, maxScrollSpace);
    }

    private void parallaxAnimation() {
        initSizeScreen();

        applyParallax();
    }

    private void initSizeScreen() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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

        if (scrollSpaceY != 0 && !blockParallaxY) {
            float locationY = (float) location[1];
            float locationUsableY = locationY + heightImageView / 2;
            float scrollDeltaY = locationUsableY / screenHeight;

            float interpolatedScrollDeltaY = interpolator.getInterpolation(scrollDeltaY);

            if (reverseY)
                setMyScrollY((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f), 0.5f) * -scrollSpaceY));
            else
                setMyScrollY((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaY), -0.5f), 0.5f) * scrollSpaceY));
        }else{
            setMyScrollY(0);
        }

        if (scrollSpaceX != 0 && !blockParallaxX) {
            float locationX = (float) location[0];
            float locationUsableX = locationX + widthImageView / 2;
            float scrollDeltaX = locationUsableX / screenWidth;

            float interpolatedScrollDeltaX = interpolator.getInterpolation(scrollDeltaX);

            if (reverseX) {
                setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * -scrollSpaceX));
            } else {
                setMyScrollX((int) (Math.min(Math.max((0.5f - interpolatedScrollDeltaX), -0.5f), 0.5f) * scrollSpaceX));
            }
        }else{
            setMyScrollX(0);
        }
    }

    private void setMyScrollX(int value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setScrollX(value);
        } else {
            scrollTo(value, getScrollY());
        }
    }

    private void setMyScrollY(int value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setScrollY(value);
        } else {
            scrollTo(getScrollX(),value);
        }
    }

    public void setInterpolator(Interpolator interpol) {
        interpolator = interpol;
    }

    public boolean isReverseX() {
        return reverseX;
    }

    public void setReverseX(boolean reverseX) {
        this.reverseX = reverseX;
    }

    public boolean isReverseY() {
        return reverseY;
    }

    public void setReverseY(boolean reverseY) {
        this.reverseY = reverseY;
    }

    public boolean isBlockParallaxX() {
        return blockParallaxX;
    }

    public void setBlockParallaxX(boolean blockParallaxX) {
        this.blockParallaxX = blockParallaxX;
    }

    public boolean isBlockParallaxY() {
        return blockParallaxY;
    }

    public void setBlockParallaxY(boolean blockParallaxY) {
        this.blockParallaxY = blockParallaxY;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(isBlockParallaxX() ? 0 : x, isBlockParallaxY() ? 0 : y);
    }

    /**
     * Limit parallax distance to some relative value, to make parallax animation more gentle.<br>
     *     Example: for an ImageView with 400x300 dimensions, we set an image with 400x1000 dimensions.
     *     Enabling Y axis animation, we will experience very "aggressive" animation, while image will be moved
     *     by 600 pixels vertically. Setting factor to 1.5f will result in 150px diff: 300*1.5=150
     *
     * @param factor
     *      must be larger or equal than 1.0f
     */
    public void setFactor(float factor) {
        if (factor < 1.0f)
            throw new IllegalArgumentException("Factor value must be larger or equal than 1.0f !");

        this.factor = factor;
    }

    /**
     * Get parallax animation bounds factor.
	 *
     * @return
     *     either {@value #FACTOR_DEFAULT} (default value, when none factor is applied) or value that is larger or equal to 1.0f.
     */
    public float getFactor(){
		return factor;
    }
}