package edu.hsb.proto.test.service;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLng;

public interface IMapService {
    void initMap(Fragment fragment);
    void resetMap();
    void centerOnLocation(LatLng latLng);
}