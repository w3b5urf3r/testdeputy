package com.deputy.test.mariolopez;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by mario on 23/02/2017.
 */

public final class DataBinder {

    private DataBinder() {
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @BindingAdapter("shiftButtonTriggerText")
    public static void setShiftButtonTriggerText(Button btn, boolean isStarted) {
        Context context = btn.getContext();
        if (!isStarted) {
            btn.setText(context.getString(R.string.start));
        } else {
            btn.setText(context.getString(R.string.end));
        }
    }
}