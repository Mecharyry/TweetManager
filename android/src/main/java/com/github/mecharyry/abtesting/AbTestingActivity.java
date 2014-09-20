package com.github.mecharyry.abtesting;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import com.github.mecharyry.BuildConfig;
import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.annotations.Parser;

public class AbTestingActivity extends Activity {

    private LeanplumActivityHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (doAbTesting()) {
            initLeanplum();
        }
    }

    private void initLeanplum() {
        if (BuildConfig.DEBUG) {
            Leanplum.setAppIdForDevelopmentMode(BuildConfig.LEAN_PLUM_APP_ID, BuildConfig.LEAN_PLUM_DEV_KEY);
            Leanplum.enableVerboseLoggingInDevelopmentMode();
        } else {
            Leanplum.setAppIdForProductionMode(BuildConfig.LEAN_PLUM_APP_ID, BuildConfig.LEAN_PLUM_PRO_KEY);
        }

        Parser.parseVariablesForClasses(AbTestingActivity.class);   // TODO: Figure out a better way of parsing variables.
        Leanplum.syncResources();
        Leanplum.start(this);
    }

    @Override
    public void setContentView(final int layoutResID) {
        if (skipAbTesting()) {
            super.setContentView(layoutResID);
            return;
        }
        getHelper().setContentView(layoutResID);
    }

    private LeanplumActivityHelper getHelper() {
        if (helper == null) {
            helper = new LeanplumActivityHelper(this);
        }
        return helper;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (doAbTesting()) {
            getHelper().onResume();
        }
    }

    @Override
    public Resources getResources() {
        if (skipAbTesting()) {
            return super.getResources();
        }
        return getHelper().getLeanplumResources(super.getResources());
    }

    @Override
    protected void onPause() {
        if (doAbTesting()) {
            getHelper().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (doAbTesting()) {
            getHelper().onStop();
        }
        super.onStop();
    }

    protected boolean doAbTesting() {
        return BuildConfig.DO_AB_TESTING;
    }

    protected boolean skipAbTesting() {
        return !doAbTesting();
    }
}
