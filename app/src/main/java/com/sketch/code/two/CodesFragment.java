package com.sketch.code.two;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class CodesFragment extends Fragment {


    static ListView listView;
    SharedPreferences data;
    int x;
    ArrayList<HashMap<String, Object>> map;
    CodesManager codesManager;

    public CodesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_codes, container, false);

        codesManager = new CodesManager(getActivity());

        listView = view.findViewById(R.id.listview);

        map = codesManager.getCodes();

        listView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, codesManager.getCodesString()));

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(getActivity(), CodeActivity.class);
                i.putExtra("name", listView.getItemAtPosition(position).toString());
                x=0;
                for(x=0; x<map.size(); x++){
                    if(map.get(x).get("name").equals(listView.getItemAtPosition(position))){
                        i.putExtra("code", map.get(x).get("code").toString());
                        i.putExtra("help", map.get(x).get("help").toString());
                        startActivity(i);
                    }
                }
            }
        });
        MainActivity.activity.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    listView.clearTextFilter();
                    ((ArrayAdapter) listView.getAdapter()).getFilter().filter(newText);
                }catch (Exception e) {

                }
                return true;
            }
        });
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(hidden) listView.setVisibility(View.GONE);
        else       listView.setVisibility(View.VISIBLE);
    }
}
