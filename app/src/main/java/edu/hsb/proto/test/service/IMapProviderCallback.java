package edu.hsb.proto.test.service;

import com.google.android.gms.maps.GoogleMap;

public interface IMapProviderCallback {
    void onMapReady(GoogleMap map);
}