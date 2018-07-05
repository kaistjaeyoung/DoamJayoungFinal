package com.example.q.semitest;

import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class Thick_Select extends Activity {
    private RelativeLayout.LayoutParams params;
    private ImageView circle;
    private SeekBar thick;
    private int r;

    protected void Set_params(int inp) {
        params = new RelativeLayout.LayoutParams(inp,inp);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        circle.setLayoutParams(params);
        r = inp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thick_select);

        circle = (ImageView) findViewById(R.id.circle);
        thick = (SeekBar) findViewById(R.id.thick);

        circle.setBackgroundColor(0);
        circle.setBackground(new ShapeDrawable(new OvalShape()));
        circle.setClipToOutline(true);

        r = getIntent().getIntExtra("thick", 10);
        Set_params(r);
        thick.setProgress(r);
        setResult(r);

        thick.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Set_params(progress);
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

        findViewById(R.id.confirm_thick).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        setResult(r);
                        finish();
                    }
                }
        );
    }
}
