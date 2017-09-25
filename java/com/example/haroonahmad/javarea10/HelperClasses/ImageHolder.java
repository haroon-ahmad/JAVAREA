package com.example.haroonahmad.javarea10.HelperClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haroonahmad.javarea10.R;

/**
 * Created by Haroon Ahmad on 2/26/2017.
 */

public class ImageHolder {

    ImageView imageView;
    TextView textView;
    public ImageHolder(View v){
        imageView = (ImageView) v.findViewById(R.id.roomIcon);
        textView = (TextView) v.findViewById(R.id.roomName);
    }

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
}
