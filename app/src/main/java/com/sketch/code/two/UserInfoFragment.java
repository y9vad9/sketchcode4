package com.sketch.code.two;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class UserInfoFragment extends Fragment {


    public UserInfoFragment() {
        // Required empty public constructor
    }
    TextView id;
    TextView regtime;
    TextView about;
    ProfilesManager profilesManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        id = (TextView) view.findViewById(R.id.id);
        regtime = (TextView) view.findViewById(R.id.regtime);
        about = (TextView) view.findViewById(R.id.about);
        id.setText(getActivity().getIntent().getStringExtra("id"));
        profilesManager = new ProfilesManager(getActivity());
        profilesManager.getProfile(getActivity().getIntent().getStringExtra("id"), new ProfilesManager.Profile() {
            @Override
            public void onLoad(String data) {
                regtime.setText(JsonUtil.getGsonDataKey(data, "regtime"));
                about.setText(JsonUtil.getGsonDataKey(data, "status"));
            }

            @Override
            public void onError(String data) {

            }
        });
        return view;
    }

}
