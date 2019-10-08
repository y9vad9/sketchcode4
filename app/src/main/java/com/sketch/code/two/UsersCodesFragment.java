package com.sketch.code.two;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;


public class UsersCodesFragment extends Fragment {
    ListView list;

    public UsersCodesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_codes, container, false);
        list = (ListView)view.findViewById(R.id.list);
        UsersContentManager usersContentManager = new UsersContentManager(getActivity());
        usersContentManager.loadCodes(new UsersContentManager.LoadListeners() {
            @Override
            public void onLoaded(final String data) {
                list.setAdapter(new ArrayAdapter<String>(MainActivity.activity, android.R.layout.simple_list_item_1, JsonUtil.getStringFromHashMap(JsonUtil.ArrayfromJson(data), "name")));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<HashMap<String, Object>> map = JsonUtil.ArrayfromJson(data);
                        Intent i = new Intent();
                        i.putExtra("type", "code");
                        int x=0;
                        for(x=0; x<map.size(); x++){
                            if(map.get(x).get("name").equals(list.getItemAtPosition(position))){
                                i.putExtra("code", map.get(x).get("code").toString());
                                i.putExtra("name", list.getItemAtPosition(position).toString());
                                i.putExtra("id", map.get(x).get("id").toString());
                                i.putExtra("subtitle", map.get(x).get("subtitle").toString());
                                i.putExtra("devid", map.get(x).get("authorId").toString());
                                i.setClass(getActivity(), UsersContentActivity.class);
                                startActivity(i);
                            }
                        }

                    }
                });
            }

            @Override
            public void onError(String message) {

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
                    list.clearTextFilter();
                    ((ArrayAdapter) list.getAdapter()).getFilter().filter(newText);
                }catch (Exception e) {

                }
                return true;
            }
        });
        return view;
    }

}
