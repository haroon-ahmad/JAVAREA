package com.example.haroonahmad.javarea10.HelperClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haroonahmad.javarea10.R;

/**
 * Created by Haroon Ahmad on 2/26/2017.
 */

public class DeviceHolder {
    ImageView imageView;
    TextView textView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public DeviceHolder(View v){
        imageView = (ImageView) v.findViewById(R.id.deviceIcon);
        textView = (TextView) v.findViewById(R.id.deviceName);
    }
}
