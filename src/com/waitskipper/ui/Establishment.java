package com.waitskipper.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.waitskipper.R;

public class Establishment extends LinearLayout {

    private final TextView name;

    public Establishment(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.establishment, this);
        name = (TextView) findViewById(R.id.name);
        name.setText("Howdy");
    }
}