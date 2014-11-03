package com.fmsirvent.ParallaxEverywhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by fmsirvent on 03/11/14.
 */
public class PEWImageView  extends ImageView {
    private static final int REVERSE_NONE = 1;
    private static final int REVERSE_X = 2;
    private static final int REVERSE_Y = 3;
    private static final int REVERSE_BOTH = 4;
    private float scrollSpaceX = 0;
    private float scrollSpaceY = 0;

    private int screenWidth;
    private int screenHeight;

    private float heightImageView;
    private float widthImageView;
    private boolean reverseX;
    private boolean reverseY;

    public PEWImageView(Context context) {
        super(context);
        if (!isInEditMode())
            parallaxAnimation();
    }

    public PEWImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        checkAttributes(attrs);
        if (!isInEditMode())
            parallaxAnimation();
    }

    public PEWImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        checkAttributes(attrs);
        if (!isInEditMode())
            parallaxAnimation();
    }

    private void checkAttributes(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PEWImageView);
        int reverse = arr.getInt(R.styleable.PEWImageView_reverse, 1);

        reverseX = false;
        reverseY = false;
        switch (reverse) {
            case REVERSE_NONE:
                break;
            case REVERSE_X:
                reverseX = true;
                break;
            case REVERSE_Y:
                reverseY = true;
                break;
            case REVERSE_BOTH:
                reverseX = true;
                reverseY = true;
                break;
        }

        switch (getScaleType()) {
            case CENTER:
            case CENTER_CROP:
            case CENTER_INSIDE:
                break;
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

        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //TODO: palarax with diference of image size and view size
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

            scrollSpaceY = (dnewHeight > vheight) ? (dnewHeight - vheight) : 0;
            scrollSpaceX = (dnewWidth > vwidth) ? (dnewWidth - vwidth) : 0;
        }
    }

    private void parallaxAnimation() {
        initSizeScreen();

        applyParallax();

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                applyParallax();
            }
        });
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                heightImageView = (float) getHeight();
                widthImageView = (float) getWidth();

                applyParallax();
            }
        });
    }

    private void initSizeScreen() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
    }

    private void applyParallax() {
        int[] location = new int[2];
        getLocationOnScreen(location);

        if (scrollSpaceY != 0) {
            float locationY = (float) location[1];
            float locationUsableY = locationY + heightImageView / 2;
            float scrollDeltaY = locationUsableY / screenHeight;

            if (reverseY)
                setScrollY((int) (Math.min(Math.max((0.5f - scrollDeltaY), -0.5f), 0.5f) * -scrollSpaceY));
            else
                setScrollY((int) (Math.min(Math.max((0.5f - scrollDeltaY), -0.5f), 0.5f) * scrollSpaceY));
        }

        if (scrollSpaceX != 0) {
            float locationX = (float) location[0];
            float locationUsableX = locationX + widthImageView / 2;
            float scrollDeltaX = locationUsableX / screenWidth;

            if (reverseX) {
                setScrollX((int) (Math.min(Math.max((0.5f - scrollDeltaX), -0.5f), 0.5f) * -scrollSpaceX));
            } else {
                setScrollX((int) (Math.min(Math.max((0.5f - scrollDeltaX), -0.5f), 0.5f) * scrollSpaceX));
            }
        }
    }

    public float getScrollSpaceX() {
        return scrollSpaceX;
    }

    public void setScrollSpaceX(float scrollSpaceX) {
        this.scrollSpaceX = scrollSpaceX;
    }

    public float getScrollSpaceY() {
        return scrollSpaceY;
    }

    public void setScrollSpaceY(float scrollSpaceY) {
        this.scrollSpaceY = scrollSpaceY;
    }
}