package com.neerajms99b.neeraj.simpletodolist.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.neerajms99b.neeraj.simpletodolist.BuildConfig;
import com.neerajms99b.neeraj.simpletodolist.R;

public class MainActivity extends AppCompatActivity {

    private static final String MOBILE_ADS_APP_ID = BuildConfig.MOBILE_ADS_APP_ID;
    private static final String TEST_DEVICE_ID = BuildConfig.TEST_DEVICE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context context = this;

        MobileAds.initialize(getApplicationContext(), MOBILE_ADS_APP_ID);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(TEST_DEVICE_ID)
                .build();
        mAdView.loadAd(adRequest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddTodoActivity.class);
                startActivity(intent);

            }
        });
    }
}
