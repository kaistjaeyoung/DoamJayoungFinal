package com.example.q.semitest;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static ContentResolver contentResolver;

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
    private static ArrayList<Map<String, String>> ImageList = new ArrayList<>();

    private void requestPermission(String permissionCode) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                permissionCode)) {
        } else {
            // No explanation needed, we can request the permission.
            if (permissionCode == Manifest.permission.READ_CONTACTS) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permissionCode},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else if (permissionCode == Manifest.permission.READ_EXTERNAL_STORAGE) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permissionCode},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else if (permissionCode == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permissionCode},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length <= 0 ||                                             // 승인안됨
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
                Toast.makeText(getApplicationContext(), "연락처 읽기 기능을 거부하면 app을 실행할수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length <= 0 ||                                             //승인안됨
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "갤러리 읽기 기능을 거부하면 app을 실행할수 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            } else {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length <= 0 ||                                             //승인안됨
                    grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "갤러리 쓰기 기능을 거부하면 app을 실행할수 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            } else {
                setView();
            }
        }
    }

    private void setView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(3);

        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private void checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(permission);
        } else {
            if (permission == Manifest.permission.READ_CONTACTS) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else if (permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                setView();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, loading.class);
        startActivity(intent);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            contentResolver = getContentResolver();
            ImageList = fetchAllImages();
            setView();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private static ArrayList<Map<String, String>> fetchAllImages() {
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        Cursor imageCursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        ArrayList<Map<String, String>> result = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[1]);

        if (imageCursor == null) {
        } else if (imageCursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                String filePath = imageCursor.getString(dataColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);

                map.put("thumb", imageId);
                map.put("img", filePath);

                result.add(map);
            } while (imageCursor.moveToNext());
        } else {
        }

        imageCursor.close();
        return result;
    }

    public static ArrayList<Map<String, String>> getImageList() {
        return ImageList;
    }

    public static void UpdateImageList() {
        ImageList = fetchAllImages();
        Frag2.init();
        Frag3.init();
    }

}
