/*
 * Copyright (C) 2017 MechDome - http://www.mechdome.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mechdome.aboutmechdome;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.mechdome.view.google.AdMobNativeView;

public class AboutMechDomeActivity extends Activity {

    public static final String TEST_MODE = "testmode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_about_mech_dome);
        // Setup the AdMob View
        Bundle b = getIntent().getExtras();
        boolean testMode = false;
        if (b != null) {
            testMode = b.getBoolean(TEST_MODE);
        }

        AdMobNativeView adView = (AdMobNativeView)findViewById(R.id.adview);
        adView.init("ca-app-pub-2729669460650010~5828110486", "ca-app-pub-2729669460650010/7304843682", testMode);

        // Action bar

        ActionBar ab = getActionBar();
        if (ab != null) {
            Log.d("Drinks","Action bar is not null");
            ab.setTitle(R.string.about_mechdome);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            // No app icon
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            ab.show();
        }
        Log.d("Drinks","Action bar is " + ab);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
