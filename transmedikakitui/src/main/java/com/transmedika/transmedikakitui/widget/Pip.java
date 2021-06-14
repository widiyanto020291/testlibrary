package com.transmedika.transmedikakitui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.webrtc.SurfaceViewRenderer;

public class Pip extends SurfaceViewRenderer {

    public Pip(Context context) {
        super(context);
    }

    public Pip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                performClick();
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + event.getAction());
        }
    }

    // Because we call this from onTouchEvent, this code will be executed for both
    // normal touch events and for when the system calls this using Accessibility
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
