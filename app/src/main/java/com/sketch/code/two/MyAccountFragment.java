package com.sketch.code.two;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {


    public MyAccountFragment() {
        // Required empty public constructor
    }
    SharedPreferences data;
    TextInputEditText pass;
    Button change;
    AccountManager accountManager;
    LoadingDialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        data = getActivity().getSharedPreferences("account", Activity.MODE_PRIVATE);
        accountManager = new AccountManager(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        pass = (TextInputEditText) view.findViewById(R.id.pass);
        change = (Button) view.findViewById(R.id.change);
        pass.setText(data.getString("password",""));
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.show();
                accountManager.changePassword(pass.getText().toString(), new AccountManager.UpdateListener() {
                    @Override
                    public void onDataUpdated() {
                        Toast.makeText(getActivity(), "Changed!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
            }
        });
        return view;
    }

}
