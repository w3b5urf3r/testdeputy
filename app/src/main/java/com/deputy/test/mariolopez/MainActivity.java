package com.deputy.test.mariolopez;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.deputy.test.mariolopez.beans.Shift;
import com.deputy.test.mariolopez.ui.controllers.ShiftController;
import com.deputy.test.mariolopez.ui.controllers.ShiftDetailController;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

public class MainActivity extends AppCompatActivity {

    private Router router;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind(this);
        router = Conductor.attachRouter(this, findById(this, R.id.controller_container)
                , savedInstanceState);
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(new ShiftController()));
        }
    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }

    public void showShiftDetail(Shift shift) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShiftDetailController.BUNDLE_SHIFT_KEY,shift);

        router.pushController(RouterTransaction.with(new ShiftDetailController(bundle))
                .pushChangeHandler(FadeChangeHandler.fromBundle(null))
                .popChangeHandler(FadeChangeHandler.fromBundle(null)));
    }
}