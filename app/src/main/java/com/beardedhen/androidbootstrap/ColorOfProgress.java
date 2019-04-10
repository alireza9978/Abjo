package com.beardedhen.androidbootstrap;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

import coleo.com.abjo.R;

public class ColorOfProgress implements BootstrapBrand {

    @Override
    public int defaultFill(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(R.color.login_submit_gradient_right);
        }
        return Color.rgb(190, 215, 59);
    }

    @Override
    public int defaultEdge(Context context) {
        return Color.rgb(242, 242, 243);
    }

    @Override
    public int defaultTextColor(Context context) {
        return Color.rgb(242, 242, 243);
    }

    @Override
    public int activeFill(Context context) {
        return Color.rgb(242, 242, 243);
    }

    @Override
    public int activeEdge(Context context) {
        return Color.rgb(242, 242, 243);
    }

    @Override
    public int activeTextColor(Context context) {
        return Color.rgb(242, 242, 243);
    }

    @Override
    public int disabledFill(Context context) {
        return Color.rgb(242, 242, 243);
    }

    @Override
    public int disabledEdge(Context context) {
        return Color.rgb(242, 242, 243);

    }

    @Override
    public int disabledTextColor(Context context) {
        return Color.rgb(242, 242, 243);
    }

    @Override
    public int getColor() {
        return Color.rgb(242, 242, 243);
    }


}


