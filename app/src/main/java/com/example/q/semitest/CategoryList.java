package com.example.q.semitest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryList extends Fragment {

    public String mName;
    public ArrayList<HashMap<String, String>> mCategoryContactList = new ArrayList<HashMap<String,String >>();
    public HashMap<String, String> mapData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setName(String name){
        mName = name;
    }

    public void addtoPeopleList(String name, String telNum){
        mapData = new HashMap<String , String >();
        mapData.put("CateMemName", name);
        mapData.put("telNum", telNum);
        mCategoryContactList.add(mapData);
    }
}
