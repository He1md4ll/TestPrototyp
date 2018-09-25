package edu.hsb.proto.test.service.impl;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import edu.hsb.proto.test.R;
import edu.hsb.proto.test.service.IMapProviderCallback;
import edu.hsb.proto.test.service.IMapService;

public class GoogleMapService implements IMapService, OnMapReadyCallback {

    private GoogleMap map;
    private Fragment context;
    private IMapProviderCallback mapProviderCallback;

    @Override
    public void initMap(Fragment fragment) {
        this.context = fragment;
        this.mapProviderCallback = (IMapProviderCallback) fragment;

        final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        supportMapFragment.getMapAsync(this);
        fragment.getChildFragmentManager().beginTransaction().replace(R.id.map_wrapper,
                supportMapFragment).commit();
    }

    @Override
    public void resetMap() {
        final FragmentManager fragmentManager = context.getChildFragmentManager();
        final Fragment fragment = fragmentManager.findFragmentById(R.id.map_wrapper);
        fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();

        this.context = null;
        this.mapProviderCallback = null;
    }

    @Override
    public void centerOnLocation(LatLng latLng) {
        if (map != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        MapsInitializer.initialize(context.getContext());
        if (mapProviderCallback != null) {
            mapProviderCallback.onMapReady(map);
        }
        try {
            map.setMyLocationEnabled(false);
        } catch (SecurityException e) {
            Log.w(this.getClass().getSimpleName(), "Could not acquire location");
        }
    }
}