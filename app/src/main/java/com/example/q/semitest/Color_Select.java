package com.example.q.semitest;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.q.semitest.R;

public class Color_Select extends Activity {
    protected int r;
    protected int g;
    protected int b;

    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;
    private ImageView color;
    private Intent intent;

    protected void color_set() {
        color.setBackgroundColor(Color.rgb(r, g, b));
        red.setProgress(r);
        green.setProgress(g);
        blue.setProgress(b);
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_select);

        red = (SeekBar) findViewById(R.id.r);
        green = (SeekBar) findViewById(R.id.g);
        blue = (SeekBar) findViewById(R.id.b);
        color = (ImageView) findViewById(R.id.color);

        intent = getIntent();
        final int color_int = intent.getIntExtra("color", 0);
        r = Color.red(color_int);
        g = Color.green(color_int);
        b = Color.blue(color_int);
        setResult(Color.rgb(r, g, b));
        color_set();

        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress;
                color_set();
                return;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                return;
            }
        });

        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress;
                color_set();
                return;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                return;
            }
        });

        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress;
                color_set();
                return;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                return;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                return;
            }
        });

        findViewById(R.id.confirm_paint).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        setResult(Color.rgb(r, g, b));
                        finish();
                    }
                }
        );
    }
}
