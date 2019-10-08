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

import java.util.ArrayList;
import java.util.HashMap;


public class SavedMoreblocksFragment extends Fragment {

    static ListView list;

    public SavedMoreblocksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_moreblocks, container, false);
        list = (ListView)view.findViewById(R.id.list);
        final MoreblockManager moreblockManager = new MoreblockManager(getActivity());
        list.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, moreblockManager.getSavedMoreblocksString()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<HashMap<String, Object>> map = moreblockManager.getSavedMoreblocks();
                Intent i = new Intent(getActivity(), MoreblockActivity.class);
                i.putExtra("name", list.getItemAtPosition(position).toString());
                int x=0;
                for(x=0; x<map.size(); x++) {
                    if (map.get(x).get("name").equals(list.getItemAtPosition(position))) {
                        i.putExtra("subtitle", map.get(x).get("subtitle").toString());
                        i.putExtra("data", map.get(x).get("data").toString());
                        startActivity(i);
                    }
                }
            }
        });
        return view;
    }
    public static void search(String s) {
        list.clearTextFilter();
        ((ArrayAdapter) list.getAdapter()).getFilter().filter(s);
    }

}
