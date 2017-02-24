package com.deputy.test.mariolopez.ui.controllers;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by mario on 18/02/2017.
 */

public class BasePresenter<V> {

    @NonNull
    private final CompositeSubscription subsToUnsubScribe = new CompositeSubscription();

    @Nullable
    private volatile V view;

    @CallSuper
    public void bindView(@NonNull V view) {
        final V previousView = this.view;

        if (previousView != null) {
            throw new IllegalStateException("Previous view is not unbounded! previousView = " + previousView);
        }

        this.view = view;
    }

    @Nullable
    protected V view() {
        return view;
    }

    public final void disposeOnUnbindView(@NonNull Subscription disposable, @NonNull Subscription... disposables) {
        subsToUnsubScribe.add(disposable);

        for (Subscription d : disposables) {
            subsToUnsubScribe.add(d);
        }
    }

    @CallSuper
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public void unbindView(@NonNull V view) {
        final V previousView = this.view;

        if (previousView == view) {
            this.view = null;
        } else {
            throw new IllegalStateException("Unexpected view! previousView = " + previousView + ", view to unbind = " + view);
        }

        // Dispose all subscriptions that need to be disposed in this lifecycle state.
        subsToUnsubScribe.clear();
    }
}
