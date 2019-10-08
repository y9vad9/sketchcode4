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

import static com.sketch.code.two.MoreblockManager.*;



public class MoreblocksFragment extends Fragment {
    ListView list;

    public MoreblocksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MoreblockManager moreblockManager = new MoreblockManager(getActivity());
        View view = inflater.inflate(R.layout.fragment_moreblocks, container, false);
        final ArrayList<HashMap<String, Object>> map = moreblockManager.getMoreblocks();
        list = (ListView)view.findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, moreblockManager.getMoreblocksString()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        MainActivity.activity.searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.clearTextFilter();
                ((ArrayAdapter) list.getAdapter()).getFilter().filter(newText);
                return true;
            }
        });
        return view;
    }

}
