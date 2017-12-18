package com.example.user.mapgoogle;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class InfoMarkerFragment extends Fragment {

    Button buttonInfoMarker;
    Boolean fragmentBoolean;
    LinearLayout linearInfo;

    public InfoMarkerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonInfoMarker = getActivity().findViewById(R.id.buttonFragment);

        buttonInfoMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMarker();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info_marker, container, false);

        linearInfo = v.findViewById(R.id.linear);
        fragmentBoolean = true;

        return v;
    }

    public void onClickMarker() {
        if (fragmentBoolean) {
            linearInfo.animate().translationY(0f);
            fragmentBoolean = false;
        }
        else {
            linearInfo.animate().translationY(1f * linearInfo.getHeight());
            fragmentBoolean = true;
        }
    }

}
