package edu.hsb.proto.test;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import edu.hsb.proto.test.service.IMapProviderCallback;
import edu.hsb.proto.test.service.IMapService;

public class MapFragment extends LocationBase implements IMapProviderCallback {

    @Inject
    IMapService mapService;

    private FloatingActionButton fabMonitoring;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabMonitoring = view.findViewById(R.id.fab_monitoring);
        mapService.initMap(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        fabMonitoring.setEnabled(Boolean.TRUE);
        fabMonitoring.setOnClickListener(v -> {
            if (!isLocationMonitoring()) {
                startLocationMonitoring();
                fabMonitoring.setImageResource(R.drawable.baseline_stop_24);
            } else {
                stopLocationMonitoring();
                mapService.clearMap();
                fabMonitoring.setImageResource(R.drawable.baseline_play_arrow_24);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLocationMonitoring();
        mapService.resetMap();
    }

    @Override
    public void onLocationChanged(Location location) {
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mapService.centerOnLocation(latLng);
        mapService.markLocation(latLng);
    }
}