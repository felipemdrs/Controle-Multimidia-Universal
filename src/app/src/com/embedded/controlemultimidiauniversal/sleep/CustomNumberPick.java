package com.embedded.controlemultimidiauniversal.sleep;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CustomNumberPick extends NumberPicker {

    public CustomNumberPick(Context context) {
        super(context);
    }

    public CustomNumberPick(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
    }

    public CustomNumberPick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributeSet(attrs);
    }
    private void processAttributeSet(AttributeSet attrs) {
        this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "max", 0));
    }
}