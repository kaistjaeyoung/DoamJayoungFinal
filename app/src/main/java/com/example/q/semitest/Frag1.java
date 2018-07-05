package com.example.q.semitest;

import android.app.AlertDialog;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Frag1 extends Fragment  {
    private View frag1_view;
    private View category_add_view;

    private Button mCertainButtonForAddition;
    private Button mCancelButtonForAddition;
    private TextView mTextViewForAddition;

    private Button mCertainButtonForRemove;
    private Button mCancelButtonForRemove;
    private TextView mTextViewForRemove;

    private EditText mEditText;
    private EditText mEditText2;

    private Button mBtnAddress1;
    private Button mBtnAddress2;

    private TextView mEditCategorytextView;

    private ListView ContackListView;
    private ListView SampleListView;

    private ArrayList<Map<String, String>> ContackList;

    ArrayList<String> peopleList = new ArrayList<>();
    ArrayList<Integer> mSelected = new ArrayList<>();
    ArrayList<String> mCategoryList = new ArrayList<>();
    ArrayList<CategoryList> mCategoryListofList = new ArrayList<>();

    private int clickcount = 0;

    private ArrayAdapter mSampleadapter;
    private ArrayAdapter mSampleadapterforCategory;
    private LinkedHashMap<Item,ArrayList<Item>> mAlldataContact;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View frag1_view = inflater.inflate(R.layout.frag1_layout, container, false);
        final View category_add_view = inflater.inflate(R.layout.catagoryaddition_layout, container, false);
        final View category_remove_view = inflater.inflate(R.layout.catagoryremove_layout, container, false);
        super.onCreate(savedInstanceState);

        ArrayList<Item> Gruops  = new ArrayList<Item>();
        final ArrayList<String > mCategoryList = new ArrayList<String >();

        Gruops = fetchGroups();
        for (int num = 0 ; num <Gruops.size(); num++){
            mCategoryList.add(Gruops.get(num).name);
            CategoryList newinstance =  new CategoryList();
            newinstance.setName(Gruops.get(num).name);

            ArrayList<Item> toUse = new ArrayList<Item>() ;

            String[] ids = Gruops.get(num).id.split(",");
            ArrayList<Item> groupMembers =new ArrayList<Item>();
            for(int i=0;i<ids.length;i++){
                String groupId = ids[i];
                groupMembers.addAll(fetchGroupMembers(groupId));
            }

            for (int mem =0 ; mem < groupMembers.size(); mem++){
                toUse.add(groupMembers.get(mem));
            }

            for (int membernum = 0 ; membernum < toUse.size(); membernum++){
                newinstance.addtoPeopleList(toUse.get(membernum).name, toUse.get(membernum).phNo);
            }
            mCategoryListofList.add(newinstance);
        }

        final FloatingActionButton fab1 = (FloatingActionButton) frag1_view.findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Category를 선택하고 클릭하면 각 Category를 편집할 수 있습니다.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

        ContackListView = (ListView) frag1_view.findViewById(R.id.ContackListView);
        SampleListView = (ListView) frag1_view.findViewById(R.id.Category);
        mBtnAddress1 = (Button) frag1_view.findViewById(R.id.btnAddress);
        mBtnAddress2 = (Button) frag1_view.findViewById(R.id.categoryTable);
        mEditCategorytextView = (TextView) frag1_view.findViewById(R.id.CategoryAddition) ;
        ContackList = new ArrayList<Map<String, String>>();

        mCertainButtonForAddition = (Button) category_add_view.findViewById(R.id.CertainButton);
        mTextViewForAddition = (TextView) category_add_view.findViewById(R.id.CategoryAddition);
        mEditText = (EditText) category_add_view.findViewById(R.id.EditCategory);
        mEditText2 = (EditText) category_add_view.findViewById(R.id.EditCategory2);

        mCertainButtonForRemove = (Button) category_remove_view.findViewById(R.id.CertainButton);
        mCancelButtonForRemove = (Button) category_remove_view.findViewById(R.id.CancelButton);
        mTextViewForRemove = (TextView) category_remove_view.findViewById(R.id.CategoryRemove);

        takeContacts(ContackList);

        mAlldataContact = initContactList();

        mSampleadapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mCategoryList);
        mSampleadapterforCategory = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mCategoryListofList);
        SampleListView.setAdapter(mSampleadapter);
        ContackListView.setAdapter(mSampleadapterforCategory);

        //group Category Listener
        SampleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String Data = mCategoryList.get(position);
                final FloatingActionButton fab1 = (FloatingActionButton) frag1_view.findViewById(R.id.fab1);
                final int end = mCategoryListofList.size();

                //오른쪽하단버튼 온클릭 리스너. 작업해줘야 한다.
                fab1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Snackbar.make(view, "목록 편집하기", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();


                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Frag1.this.getActivity());
                        View mView = getLayoutInflater().inflate(R.layout.addcontactoncategory_layout, null);

                        mCertainButtonForAddition = mView.findViewById(R.id.CertainButton);
                        mCancelButtonForAddition = mView.findViewById(R.id.CancelButton);

                        mEditText = mView.findViewById(R.id.EditCategory);
                        mEditText2 = mView.findViewById(R.id.EditCategory2);

                        mBuilder.setView(mView);
                        final AlertDialog mEditCategoryDialog = mBuilder.create();
                        mEditCategoryDialog.show();

                        mCertainButtonForAddition.setOnClickListener(
                                new Button.OnClickListener(){
                                    public void onClick(View vv){
                                        if (! mCategoryList.contains(mEditText.getText().toString())){
                                            String name = mEditText.getText().toString();
                                            String pN = mEditText2.getText().toString();


                                            for (int listnum = 0 ; listnum < end; listnum ++) {
                                                if (mCategoryListofList.get(listnum).mName == Data) {
                                                    CategoryList holdingCategory = mCategoryListofList.get(listnum);
                                                    holdingCategory.addtoPeopleList(name, pN);
                                                    Toast.makeText(Frag1.this.getActivity(), "성공적으로 Category에 연락처를 추가하였습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        } else {
                                            Toast.makeText(Frag1.this.getActivity(), mEditText.getText().toString() + " 은/는 이미 Category 안에 들어있는 연락처 입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        mSampleadapterforCategory.notifyDataSetChanged();
                                        mEditCategoryDialog.dismiss();
                                    }
                                });

                        mCancelButtonForAddition.setOnClickListener(
                                new Button.OnClickListener(){
                                    public void onClick(View vv){
                                        mEditCategoryDialog.dismiss();
                                    }
                                });

                    }
                });


                for (int listnum = 0 ; listnum < end; listnum ++){
                    if (mCategoryListofList.get(listnum).mName == Data){
                        CategoryList holdingCategory= mCategoryListofList.get(listnum);

                        ContackListView.setAdapter(null);
                        SimpleAdapter adapter =
                                new SimpleAdapter(Frag1.this.getActivity().getApplicationContext() ,
                                        holdingCategory.mCategoryContactList,
                                        android.R.layout.simple_list_item_2,
                                        new String[]{"CateMemName","telNum"},
                                        new int[] {android.R.id.text1,android.R.id.text2});
                        ContackListView.setAdapter(adapter);

                    }
                }
            }
        });

        SampleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                Toast.makeText(getActivity().getApplicationContext(), "길게 눌러보았더니" , Toast.LENGTH_LONG).show();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Frag1.this.getActivity());
                View mView = getLayoutInflater().inflate(R.layout.catagoryremove_layout, null);

                mCertainButtonForAddition = mView.findViewById(R.id.CertainButton);
                mEditText = mView.findViewById(R.id.EditCategory);

                mCertainButtonForRemove = (Button) mView.findViewById(R.id.CertainButton);
                mCancelButtonForRemove = (Button) mView.findViewById(R.id.CancelButton);
                mTextViewForRemove = (TextView) mView.findViewById(R.id.CategoryRemove);

                mBuilder.setView(mView);
                final AlertDialog mEditCategoryDialog = mBuilder.create();
                mEditCategoryDialog.show();

                mCertainButtonForRemove.setOnClickListener(
                        new Button.OnClickListener(){
                            public void onClick(View v){
                                mCategoryList.remove(SampleListView.getItemAtPosition(position));
                                int end = mCategoryListofList.size();

                                loop :  //여기 왜 break로 탈출 안하는지 모르겠다.
                                for (int listnum = 0 ; listnum < end ; listnum ++) {
                                    if (mCategoryListofList.get(listnum).mName == SampleListView.getItemAtPosition(position)) {
                                        mCategoryListofList.remove(listnum);
                                        break loop;
                                    }
                                }

                                Toast.makeText(Frag1.this.getActivity(), "선택된 Category를 지웠습니다.", Toast.LENGTH_SHORT).show();
                                mSampleadapter.notifyDataSetChanged();
                                mEditCategoryDialog.dismiss();
                            }
                        });

                mCancelButtonForRemove.setOnClickListener(
                        new Button.OnClickListener(){
                            public  void  onClick(View v){
                                mEditCategoryDialog.dismiss();
                            }
                        });
                return true;
            }
        });

        frag1_view.findViewById(R.id.btnAddress).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if (clickcount % 2 == 0) {
                            SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(),
                                    ContackList,
                                    android.R.layout.simple_list_item_2,
                                    new String[]{"name", "phone"},
                                    new int[]{android.R.id.text1, android.R.id.text2});
                            ContackListView.setAdapter(adapter);
                        }
                        else {
                            ContackListView.setAdapter(null);
                        }
                        clickcount += 1;











                    }
                }
        );

        frag1_view.findViewById(R.id.categoryTable).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Frag1.this.getActivity());
                        View mView = getLayoutInflater().inflate(R.layout.catagoryaddition_layout, null);

                        mCertainButtonForAddition = mView.findViewById(R.id.CertainButton);
                        mCancelButtonForAddition = mView.findViewById(R.id.CancelButton);

                        mEditText = mView.findViewById(R.id.EditCategory);
                        mBuilder.setView(mView);
                        final AlertDialog mEditCategoryDialog = mBuilder.create();
                        mEditCategoryDialog.show();

                        mCertainButtonForAddition.setOnClickListener(
                                new Button.OnClickListener(){
                                    public void onClick(View vv){
                                        if (! mCategoryList.contains(mEditText.getText().toString())){
                                            String name = mEditText.getText().toString();
                                            CategoryList newCategory = new CategoryList();
                                            mCategoryListofList.add(newCategory);
                                            mCategoryList.add(name);
                                            newCategory.setName(name);
                                            Toast.makeText(Frag1.this.getActivity(), mEditText.getText().toString() + "를 새로운 Category로 추가하였습니다..", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(Frag1.this.getActivity(), mEditText.getText().toString() + "는 이미 존재하는 Category입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        mSampleadapter.notifyDataSetChanged();
                                        mEditCategoryDialog.dismiss();
                                    }
                                });

                        mCancelButtonForAddition.setOnClickListener(
                                new Button.OnClickListener(){
                                    public void onClick(View vv){
                                        mEditCategoryDialog.dismiss();
                                    }
                                });

                    }
                }
        );

        ContackListView.setAdapter(null);

        return frag1_view;
    }

    public void takeContacts (ArrayList<Map<String, String>> DataList) {
        Cursor c = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (c.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            // 연락처 id 값
            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
            // 연락처 대표 이름
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
            map.put("name", name);

            // ID로 전화 정보 조회
            Cursor phoneCursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null);

            // 데이터가 있는 경우
            if (phoneCursor.moveToFirst()) {
                String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                map.put("phone", number);
            }
            phoneCursor.close();
            DataList.add(map);
        }// end while
        c.close();
    }


    public class Item{
        public String name,id,phNo,phDisplayName,phType;
        public boolean isChecked =false;
    }

    private ArrayList<Item> fetchGroups() {
        ArrayList<Item> groupList = new ArrayList<Item>();
        String[] projection = new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE};
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Groups.CONTENT_URI,
                projection, null, null, null);
        ArrayList<String> groupTitle = new ArrayList<String>();
        while (cursor.moveToNext()) {
            Item item = new Item();
            item.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
            String groupName = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));

            if (groupName.contains("Group:"))
                groupName = groupName.substring(groupName.indexOf("Group:") + "Group:".length()).trim();

            if (groupName.contains("Favorite_"))
                groupName = "Favorite";

            if (groupName.contains("Starred in Android") || groupName.contains("My Contacts"))
                continue;

            if (groupTitle.contains(groupName)) {
                for (Item group : groupList) {
                    if (group.name.equals(groupName)) {
                        group.id += "," + item.id;
                        break;
                    }
                }
            } else {
                groupTitle.add(groupName);
                item.name = groupName;
                groupList.add(item);
            }
        }

        cursor.close();
        Collections.sort(groupList, new Comparator<Item>() {
            public int compare(Item item1, Item item2) {
                return item2.name.compareTo(item1.name) < 0
                        ? 0 : -1;
            }
        });
        return groupList;

    }

    private ArrayList<Item> fetchGroupMembers(String groupId){
        ArrayList<Item> groupMembers = new ArrayList<Item>();
        String where =  ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID +"="+groupId
                +" AND "
                +ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE+"='"
                +ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID, ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, where,null,
                ContactsContract.Data.DISPLAY_NAME+" COLLATE LOCALIZED ASC");
        while(cursor.moveToNext()){
            Item item = new Item();
            item.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            item.id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID));
            Cursor phoneFetchCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.TYPE},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+item.id,null,null);
            while(phoneFetchCursor.moveToNext()){
                item.phNo = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                item.phDisplayName = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                item.phType = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            }
            phoneFetchCursor.close();
            groupMembers.add(item);
        }
        cursor.close();
        return groupMembers;
    }

    private LinkedHashMap<Item,ArrayList<Item>> initContactList(){
        LinkedHashMap<Item,ArrayList<Item>> groupList = new LinkedHashMap<Item,ArrayList<Item>>();
        ArrayList<Item> groupsList = fetchGroups();
        for(Item item:groupsList){
            String[] ids = item.id.split(",");
            ArrayList<Item> groupMembers =new ArrayList<Item>();
            for(int i=0;i<ids.length;i++){
                String groupId = ids[i];
                groupMembers.addAll(fetchGroupMembers(groupId));
            }
            item.name = item.name +" ("+groupMembers.size()+")";
            groupList.put(item,groupMembers);
        }

        return groupList;
    }

}
