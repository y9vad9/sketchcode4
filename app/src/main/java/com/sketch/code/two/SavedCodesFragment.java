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
import java.util.zip.Inflater;

import static com.sketch.code.two.CodesFragment.listView;
import static com.sketch.code.two.CodesManager.*;


public class SavedCodesFragment extends Fragment {

    public SavedCodesFragment() {
        // Required empty public constructor
    }
    public static ListView list;
    static Intent ii;
    static LayoutInflater inf;
    static ViewGroup contain;
    static Bundle sis;
    CodesManager codesManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_codes, container, false);
        codesManager = new CodesManager(getActivity());
        list = (ListView) view.findViewById(R.id.list);
        final ArrayList<HashMap<String, Object>> map = codesManager.getSavedCodes();
        list.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, codesManager.getSavedCodesString()));
        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(getActivity(), CodeActivity.class);
                i.putExtra("name", list.getItemAtPosition(position).toString());
                int x=0;
                for(x=0; x<map.size(); x++){
                    if(map.get(x).get("name").equals(list.getItemAtPosition(position))){
                        i.putExtra("code", map.get(x).get("code").toString());
                        i.putExtra("help", map.get(x).get("help").toString());
                        startActivity(i);
                    }
                }
            }
        });
        inf = inflater;
        contain = container;
        sis = savedInstanceState;
        MainActivity.activity.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.clearTextFilter();
                ((ArrayAdapter) list.getAdapter()).getFilter().filter(newText);
                SavedMoreblocksFragment.search(newText);
                return true;
            }
        });

        return view;
    }


}
