package com.fmsirvent.ParallaxEverywhere.Utils;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by fmsirvent on 06/11/14.
 */
public class InterpolatorSelector {
    private static final int LINEAR = 0;
    private static final int ACCELERATE_DECELERATE = 1;
    private static final int ACCELERATE = 2;
    private static final int ANTICIPATE = 3;
    private static final int ANTICIPATE_OVERSHOOT = 4;
    private static final int BOUNCE = 5;
    private static final int DECELERATE = 6;
    private static final int OVERSHOOT = 7;

    public static Interpolator interpolatorId(int interpolationId) {
        switch (interpolationId) {
            case LINEAR:
            default:
                return new LinearInterpolator();
            case ACCELERATE_DECELERATE:
                return new AccelerateDecelerateInterpolator();
            case ACCELERATE:
                return new AccelerateInterpolator();
            case ANTICIPATE:
                return new AnticipateInterpolator();
            case ANTICIPATE_OVERSHOOT:
                return new AnticipateOvershootInterpolator();
            case BOUNCE:
                return new BounceInterpolator();
            case DECELERATE:
                return new DecelerateInterpolator();
            case OVERSHOOT:
                return new OvershootInterpolator();
            //TODO: this interpolations needs parameters
            //case CYCLE:
            //    return new CycleInterpolator();
            //case PATH:
            //    return new PathInterpolator();
        }
    }
}
