package com.deputy.test.mariolopez;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.deputy.test.mariolopez.api.ApiModule;
import com.deputy.test.mariolopez.api.DeputyRestApi;
import com.deputy.test.mariolopez.beans.Shift;
import com.deputy.test.mariolopez.beans.ShiftInfo;
import com.deputy.test.mariolopez.databinding.ControllerShiftBinding;
import com.deputy.test.mariolopez.presenter.BasePresenter;
import com.deputy.test.mariolopez.presenter.ShiftAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static butterknife.ButterKnife.findById;

/**
 * Created by mario on 23/02/2017.
 */
interface ShiftView {
    void refreshShifts(List<Shift> shifts);

    void startShift();

    void endShift();

    void showErrorUi(Throwable error);


}

public class ShiftController extends Controller implements ShiftView {
    private DeputyRestApi restApi = ApiModule.provideDeputyRestApi();
    private ShiftPresenter presenter = new ShiftPresenter(restApi);
    private ShiftInfo currentShift;
    private ControllerShiftBinding controllerShiftBinding;
    private ShiftAdapter shiftAdapter;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_shift, null, false);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        presenter.bindView(this);
        controllerShiftBinding = DataBindingUtil.bind(view);
        //todo fetch data from db in the presenter
        currentShift = new ShiftInfo(new Date(), Double.valueOf("41.8919300"), Double.valueOf("12.5113300"));
        controllerShiftBinding.setCurrentShiftInfo(currentShift);
        initView(view);
    }

    private void initView(@NonNull View view) {
        RecyclerView recyclerView = findById(view, R.id.shifts_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        shiftAdapter = new ShiftAdapter(R.layout.shift_list_item);
        shiftAdapter.setDataSet(new ArrayList<>()); //todo load list from db.
        recyclerView.setAdapter(shiftAdapter);
        presenter.showShifts();

        //we could use the data binding method as well for the on click listener
        findById(view, R.id.shift_button).setOnClickListener(v -> {
            if (!currentShift.isStarted()) {
                LocationManager locationManager = getLocationManager();
                //it's save to call get activity here with no check
                Location lastKnownLocation = getLastKnownLocation(getActivity(), locationManager);
                refreshShiftInfo(lastKnownLocation);
                presenter.startShift(currentShift);
            } else {
                presenter.endShift(currentShift);
            }
        });
    }

    private void refreshShiftInfo(Location lastKnownLocation) {
        //for convinience I add Rome as current location
        if (lastKnownLocation == null) {
            currentShift = new ShiftInfo(new Date(),
                    Double.valueOf("41.8919300"),
                    Double.valueOf("12.5113300"));
        } else {
            currentShift = new ShiftInfo(new Date(),
                    lastKnownLocation.getLatitude(),
                    lastKnownLocation.getLongitude());
        }
    }

    private Location getLastKnownLocation(Context context, LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED &&
                    ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
//                I avoid all the code for requesting and checking permission  as it's always the same stuff,
//                I believe it's out of scope for this test
//                     note for automated test this gets a little bit tricky
//                        as you have to build a script that grant the permission before installing the app in each flavour.

                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        presenter.unbindView(this);
        controllerShiftBinding = null;
    }


    @Override
    public void refreshShifts(List<Shift> shifts) {
        if (!isActivityFinishing(getActivity())) {
            getActivity().runOnUiThread(() -> {
                shiftAdapter.setDataSet(shifts);
                shiftAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void startShift() {
        currentShift.setStarted(true);
        notifyShiftInfoBinding();
    }

    private void notifyShiftInfoBinding() {
        controllerShiftBinding.setCurrentShiftInfo(currentShift);
        controllerShiftBinding.notifyPropertyChanged(BR.currentShiftInfo);
    }

    @Override
    public void endShift() {
        Activity activity = getActivity();
        if (!isActivityFinishing(activity)) {
            refreshShiftInfo(getLastKnownLocation(activity, getLocationManager()));
            notifyShiftInfoBinding();
        }
        presenter.showShifts();

    }

    private boolean isActivityFinishing(Activity activity) {
        if (activity != null && !activity.isFinishing())
            return false;
        return true;
    }

    private LocationManager getLocationManager() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            return (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        }
        return null;
    }

    @Override
    public void showErrorUi(Throwable error) {
        //do something for the feedback
        Timber.e(error);
    }
}

class ShiftPresenter extends BasePresenter<ShiftView> {

    private final DeputyRestApi restApi;

    public ShiftPresenter(@NonNull DeputyRestApi restapi) {
        this.restApi = restapi;
    }

    public void showShifts() {
//collection loader can be easily injected
//the use of the interface DataManager allow a better testability
        final Subscription subscription = restApi.getShifts()
                .subscribeOn(Schedulers.io())
                .subscribe(
                        shifts -> {
                            ShiftView view = view();
                            if (view != null && shifts != null)
                                view.refreshShifts(shifts);
                        },
                        error -> {
                            ShiftView view = view();
                            if (view != null)
                                view.showErrorUi(error);
                        }
                );
        // Prevent memory leak.
        disposeOnUnbindView(subscription);
    }

    public void startShift(ShiftInfo shiftInfo) {
        restApi.startShift(shiftInfo)
                .subscribeOn(Schedulers.io())
                .doOnCompleted(() ->
                {
                    ShiftView view = view();
                    if (view != null)
                        view.startShift();
                })
                .doOnError(
                        error -> {
                            ShiftView view = view();
                            if (view != null)
                                view.showErrorUi(error);
                            //todo check based on the error (not what it actually returns, if the shift
                            //has already started
                        }
                ).subscribe();
    }

    public void endShift(ShiftInfo currentShift) {
        restApi.endShift(currentShift)
//                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())

                .doOnCompleted
                        (() ->
                        {
                            ShiftView view = view();
                            if (view != null) {
                                view.endShift();
                            }
                        })
                .doOnError(
                        error -> {
                            ShiftView view = view();
                            if (view != null)
                                view.showErrorUi(error);
                        }
                )
                .subscribe();
    }

}
