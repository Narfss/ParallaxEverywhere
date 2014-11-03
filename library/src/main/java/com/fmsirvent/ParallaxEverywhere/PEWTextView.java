package com.fmsirvent.ParallaxEverywhere;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by fmsirvent on 03/11/14.
 */
public class PEWTextView extends TextView {
    private boolean reverseX = false;
    private boolean reverseY = false;
    private int scrollSpaceX = 0;
    private int scrollSpaceY = 0;
    private int screenHeight;
    private int screenWidth;
    private float heightView;
    private float widthView;

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

    public PEWTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            checkAttributes(attrs);
            parallaxAnimation();
        }
    }

    private void checkAttributes(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PEWAttrs);
        int reverse = arr.getInt(R.styleable.PEWAttrs_reverse, 1);

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

        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                heightView = (float) getHeight();
                widthView = (float) getWidth();

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
            float locationUsableY = locationY + heightView / 2;
            float scrollDeltaY = locationUsableY / screenHeight;

            if (reverseY)
                setScrollY((int) (Math.min(Math.max((0.5f - scrollDeltaY), -0.5f), 0.5f) * -scrollSpaceY));
            else
                setScrollY((int) (Math.min(Math.max((0.5f - scrollDeltaY), -0.5f), 0.5f) * scrollSpaceY));
        }

        if (scrollSpaceX != 0) {
            float locationX = (float) location[0];
            float locationUsableX = locationX + widthView / 2;
            float scrollDeltaX = locationUsableX / screenWidth;

            if (reverseX) {
                setScrollX((int) (Math.min(Math.max((0.5f - scrollDeltaX), -0.5f), 0.5f) * -scrollSpaceX));
            } else {
                Log.d("Scroll value", "Value: "+ (int) (Math.min(Math.max((0.5f - scrollDeltaX), -0.5f), 0.5f) * scrollSpaceX));
                setScrollX((int) (Math.min(Math.max((0.5f - scrollDeltaX), -0.5f), 0.5f) * scrollSpaceX));
            }
        }
    }

}
