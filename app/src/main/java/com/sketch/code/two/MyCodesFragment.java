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


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCodesFragment extends Fragment {


    public MyCodesFragment() {
        // Required empty public constructor
    }
    public ListView listView;
    ProfilesManager profilesManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_codes, container, false);
        profilesManager = new ProfilesManager(getActivity());
        listView = (ListView) view.findViewById(R.id.list);
        profilesManager.loadListCodes(getActivity().getIntent().getStringExtra("id"), new ProfilesManager.Profile() {
            @Override
            public void onLoad(String data) {
                try {
                    Debug.writeResponse(data);
                    final ArrayList<String> strings = new ArrayList<>();
                    final ArrayList<HashMap<String, Object>> map = JsonUtil.ArrayfromJson(data);
                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.activity, android.R.layout.simple_list_item_1, JsonUtil.getStringFromHashMap(map, "name")));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent i = new Intent();
                            i.putExtra("type", "code");
                            int x=0;
                            for(x=0; x<map.size(); x++){
                                if(map.get(x).get("name").equals(listView.getItemAtPosition(position))){
                                    i.putExtra("code", map.get(x).get("code").toString());
                                    i.putExtra("name", listView.getItemAtPosition(position).toString());
                                    i.putExtra("id", map.get(x).get("id").toString());
                                    i.putExtra("subtitle", map.get(x).get("subtitle").toString());
                                    i.putExtra("devid", map.get(x).get("authorId").toString());
                                    i.setClass(getActivity(), UsersContentActivity.class);
                                    startActivity(i);
                                }
                            }

                        }
                    });

                }catch (Exception e) {
                    Debug.reportError(e);
                }
            }

            @Override
            public void onError(String data) {

            }
        });
        return view;
    }

}
