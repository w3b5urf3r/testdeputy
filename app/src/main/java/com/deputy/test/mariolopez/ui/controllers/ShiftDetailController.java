package com.deputy.test.mariolopez.ui.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluelinelabs.conductor.RestoreViewOnCreateController;
import com.deputy.test.mariolopez.R;
import com.deputy.test.mariolopez.beans.Shift;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static butterknife.ButterKnife.findById;

/**
 * Created by mario on 24/02/2017.
 */

public class ShiftDetailController extends RestoreViewOnCreateController implements OnMapReadyCallback {
    public static final String BUNDLE_SHIFT_KEY = "bundle shift key";
    private MapView mapView;
    private Shift shift;

    public ShiftDetailController(Bundle bundle) {
        shift = (Shift) bundle.get(BUNDLE_SHIFT_KEY);
    }
    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedViewState) {
        View view = inflater.inflate(R.layout.controller_shift_detail, container, false);
        mapView = findById(view, R.id.map_view);
        mapView.onCreate(savedViewState);
        mapView.getMapAsync(this);
        ((TextView)findById(view,R.id.shift_time_start)).setText(shift.getStart().toString());
        ((TextView)findById(view,R.id.shift_time_end)).setText(shift.getEnd().toString());
        return view;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        mapView.onResume();
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//       Tip: the service have a dirty double value as string which may crash the code, the case
//        is not handled in this example as in this specific case I consider it an api error.
//        it's on the first element startLongitude "12.5113300\\""

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Shift.getCoordinate(shift.getStartLatitude()),
                        Shift.getCoordinate(shift.getStartLongitude())))
                        .title("start"));
        //not visible if on the same spot
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Shift.getCoordinate(shift.getEndLatitude()),
                        Shift.getCoordinate(shift.getEndLongitude())))
                        .title("end"));

    }
}
