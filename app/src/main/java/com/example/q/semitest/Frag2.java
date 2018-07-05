package com.example.q.semitest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Frag2 extends Fragment{
    private static GridView mGridview;
    private static Context mContext;

    private static int firstitem = 0;
    private static int firstVisibleItem = -1;
    private static int visibleItemCount = -1;
    private static final int totalitem = 40;
    private static final int loaditem = 20;
    private static ArrayList<Map<String, String>> ImageList;
    private static ArrayList<Map<String, String>> GridImageList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageList = MainActivity.getImageList();
        mContext = getActivity();

        return;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root2view = inflater.inflate(R.layout.frag2_layout, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        mGridview = (GridView) root2view.findViewById(R.id.gridview_frag2);

        mGridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (firstVisibleItem == 0 && firstitem != 0) {
                        firstitem -= loaditem;
                        exe_async(1);
                    } else if (firstVisibleItem + visibleItemCount == totalitem && firstitem + totalitem < ImageList.size()) {
                        firstitem += loaditem;
                        exe_async(2);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int visibleItem, int totalItemCount) {
                firstVisibleItem = firstVisible;
                visibleItemCount = visibleItem;

                return;
            }
        });

        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                String path = (String)((HashMap)adapterView.getItemAtPosition(pos)).get("img");
                Toast.makeText(getActivity().getApplicationContext(), path, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getApplicationContext(), BigPicture.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        exe_async(0);

        return root2view;
    }


    private static class GridDataAsyncTask_Frag2 extends AsyncTask<Void, Void, SimpleAdapter> {
        private int mode;
        ContentResolver contentResolver = mContext.getContentResolver();

        public GridDataAsyncTask_Frag2(int mode_inp) {
            mode = mode_inp;
        }

        @Override
        protected SimpleAdapter doInBackground(Void... params) {
            GridImageList.clear();
            for (int i = firstitem; i < Math.min(firstitem + totalitem, ImageList.size()); i++) {
                HashMap<String, String> map = new HashMap<>();

                map.put("img", ImageList.get(i).get("img"));
                map.put("thumb", IDtoThumbnail(Integer.parseInt(ImageList.get(i).get("thumb"))));

                GridImageList.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(mContext.getApplicationContext(),
                    GridImageList,
                    R.layout.gridview_layout_frag2,
                    new String[]{"thumb"},
                    new int[]{R.id.thumb});
            return adapter;
        }

        @Override
        protected void onPostExecute(SimpleAdapter result) {
            super.onPostExecute(result);
            mGridview.setAdapter(result);

            if(mode == 1) mGridview.setSelection(loaditem);
            else if(mode == 2) mGridview.setSelection(loaditem - visibleItemCount + 2);

            return;
        }

        private String IDtoThumbnail(long fileId) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor thumbnailCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(contentResolver, fileId, MediaStore.Images.Thumbnails.MINI_KIND, projection);

            if (thumbnailCursor != null && thumbnailCursor.moveToFirst())
                return thumbnailCursor.getString(thumbnailCursor.getColumnIndex(projection[0]));

            else {
                MediaStore.Images.Thumbnails.getThumbnail(contentResolver, fileId, MediaStore.Images.Thumbnails.MINI_KIND, null);
                thumbnailCursor.close();
                return IDtoThumbnail(fileId);
            }
        }
    }

    private static void exe_async(int mode) {
        GridDataAsyncTask_Frag2 mGridDataAsyncTaskFrag2 = new GridDataAsyncTask_Frag2(mode);
        mGridDataAsyncTaskFrag2.execute();
        return;
    }

    public static void init() {
        firstitem = 0;
        firstVisibleItem = -1;
        visibleItemCount = -1;

        ImageList = MainActivity.getImageList();
        exe_async(0);
        return;
    }
}
