package com.example.q.semitest;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Paint extends AppCompatActivity {
    private RelativeLayout mRelativeLayout;
    private DrawLine mDrawLine;
    private Bitmap mBitmap;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        mDrawLine = (DrawLine) findViewById(R.id.canvas);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.layout);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (mDrawLine.Bit() == null) {
            Intent intent = getIntent();
            path = intent.getStringExtra("path");

            mBitmap = BitmapFactory.decodeFile(path);
            mBitmap = mBitmap.copy(mBitmap.getConfig(), true);
            mBitmap = Bitmap_scale();
            mDrawLine.init(mBitmap);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mBitmap.getWidth(), mBitmap.getHeight());
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mDrawLine.setLayoutParams(params);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //메뉴 생성
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //메뉴 선택시 작동

        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.color:
                intent = new Intent(getApplicationContext(), Color_Select.class);
                intent.putExtra("color", mDrawLine.Color());
                startActivityForResult(intent, 0);
                return true;

            case R.id.save:
                String newFilePath = new StringBuffer().append(path).append(".paint.png").toString();
                System.out.println(newFilePath);

                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + " = ?", new String[] {newFilePath});

                try {
                    File file = new File(newFilePath);
                    FileOutputStream fos = new FileOutputStream(file);

                    Bitmap bm = mDrawLine.Bit();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();

                    MediaScanner scanner = MediaScanner.newInstance(getApplicationContext());
                    scanner.mediaScanning(newFilePath);

                    Toast.makeText(getApplicationContext(), "Save complete!", Toast.LENGTH_SHORT).show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            MainActivity.UpdateImageList();
                        }
                    }, 100);


                    super.onBackPressed();

                    return true;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.thick:
                intent = new Intent(getApplicationContext(), Thick_Select.class);
                intent.putExtra("thick", mDrawLine.Thick());
                startActivityForResult(intent, 1);
                return true;

            case R.id.cancel:
                Toast.makeText(getApplicationContext(), "Drawing is Canceled", Toast.LENGTH_SHORT).show();
                super.onBackPressed();

                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //호출한 Activity 종료시 작동

        switch (requestCode) {
            case 0:
                mDrawLine.setLineColor(resultCode);
                break;
            case 1:
                mDrawLine.setLineWidth(resultCode);
                break;
        }

        return;
    }

    protected Bitmap Bitmap_scale() {
        //Bitmap 사이즈를 화면에 맞게 resizing

        int screenx = mRelativeLayout.getWidth();
        int screeny = mRelativeLayout.getHeight();
        int bitx = mBitmap.getWidth();
        int bity = mBitmap.getHeight();
        float ratx = 1;
        float raty = 1;

        if (bitx <= screenx && bity < screeny) return mBitmap;

        ratx = screenx / (float) bitx;
        raty = screeny / (float) bity;

        if (ratx <= raty)
            return Bitmap.createScaledBitmap(mBitmap, (int) (bitx * ratx), (int) (bity * ratx), true);

        else
            return Bitmap.createScaledBitmap(mBitmap, (int) (bitx * raty), (int) (bity * raty), true);
    }
}
