package com.example.user.mapgoogle;

import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {
    public final View view;
    public final int profilePhoto;
    private final LatLng mPosition;

    public Person(LatLng position, View view, int pictureResource) {
        this.view = view;
        profilePhoto = pictureResource;
        mPosition = position;

    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }


}
