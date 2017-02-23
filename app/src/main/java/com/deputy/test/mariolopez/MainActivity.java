package com.deputy.test.mariolopez;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;

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
        //todo through DI


    }

    @Override
    public void onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed();
        }
    }
}