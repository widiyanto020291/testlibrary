package com.transmedika.transmedikakitui.utils;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class HideUtil {
    private static final int TRANSLATE_DURATION_MILLIS = 200;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private boolean mVisible = true;

    private final View viewToVisible;
    private final View viewMain;
    private boolean plus = true;

    public HideUtil(View viewToVisible, View viewMain, boolean plus) {
        this.viewToVisible = viewToVisible;
        this.viewMain = viewMain;
        this.plus = plus;
    }

    public void hide() {
        hide(true);
    }

    public void show() {
        show(true);
    }

    public void hide(boolean anim) {
        toggle(false, anim, false);
    }

    public void show(boolean anim) {
        toggle(true, anim, false);
    }

    public boolean isVisible() {
        return mVisible;
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = viewToVisible.getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = viewMain.getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = viewMain.getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height;
            if (animate) {
                viewToVisible.animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(plus ? translationY : -translationY);
            } else {
                viewToVisible.setTranslationY(translationY);
            }
        }
    }
}
