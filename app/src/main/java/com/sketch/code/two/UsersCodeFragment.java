package com.sketch.code.two;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.froyo.codeview.CodeView;
import com.froyo.codeview.Language;
import com.froyo.codeview.Theme;

import static com.sketch.code.two.JsonUtil.getValue;
import static com.sketch.code.two.JsonUtil.showMessage;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersCodeFragment extends Fragment {
    CodeView codeView;
    TextView dev;
    TextView title;
    TextView subtitle;
    ProfilesManager profilesManager;

    public UsersCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_code, container, false);
        AdManager adManager = new AdManager(getActivity());
        adManager.show();
        dev = (TextView) view.findViewById(R.id.dev);
        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);
        title.setText(getActivity().getIntent().getStringExtra("name"));
        subtitle.setText(getActivity().getIntent().getStringExtra("subtitle"));
        codeView = (CodeView) view.findViewById(R.id.codeview);
        profilesManager = new ProfilesManager(getActivity());
        profilesManager.getProfile(getActivity().getIntent().getStringExtra("devid"), new ProfilesManager.Profile() {
            @Override
            public void onLoad(String data) {
                dev.setText(getValue(data, "nickname"));
            }

            @Override
            public void onError(String data) {
                dev.setText("Unknown user");
            }
        });


        codeView.setTheme(Theme.ATOM_ONE_LIGHT);
        codeView.setLanguage(Language.JAVA);
        codeView.setWrapLine(true);
        codeView.setCode("\n" + getActivity().getIntent().getStringExtra("code") + "\n");
        codeView.setShowLineNumber(false);
        codeView.setZoomEnabled(false);
        codeView.setFontSize(12);
        codeView.setOnHighlightListener(new CodeView.OnHighlightListener() {
            @Override
            public void onStartCodeHighlight() {

            }

            @Override
            public void onFinishCodeHighlight() {

            }

            @Override
            public void onLanguageDetected(Language language, int i) {

            }

            @Override
            public void onFontSizeChanged(int i) {

            }

            @Override
            public void onLineClicked(int i, String s) {

            }
        });
        codeView.apply();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("sketchcode", getActivity().getIntent().getStringExtra("code"));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });
        dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilesManager.getProfile(getActivity().getIntent().getStringExtra("devid"), new ProfilesManager.Profile() {
                    @Override
                    public void onLoad(String data) {
                        Intent i = new Intent();
                        i.setClass(getActivity(), PubProfActivity.class);
                        i.putExtra("name", getValue(data, "nickname"));
                        i.putExtra("email", getValue(data, "email"));
                        i.putExtra("avatar", getValue(data, "avatar"));
                        i.putExtra("id", getActivity().getIntent().getStringExtra("devid"));
                        startActivity(i);
                    }

                    @Override
                    public void onError(String data) {
                        dev.setText("Dev");
                    }
                });
            }
        });
        return view;
    }

}
