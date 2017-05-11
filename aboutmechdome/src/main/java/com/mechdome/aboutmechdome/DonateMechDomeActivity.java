package com.mechdome.aboutmechdome;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by nandu on 5/10/17.
 */

public class DonateMechDomeActivity extends Activity {
    public static final String TEST_MODE = "testmode";

    public boolean inTestMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_mechdome_donate);

        // Setup the AdMob View
        Bundle b = getIntent().getExtras();
        if (b != null) {
            inTestMode = b.getBoolean(TEST_MODE);
        }

        // Action bar

        ActionBar ab = getActionBar();
        if (ab != null) {
            ab.setTitle(R.string.about_mechdome);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
            // No app icon
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            ab.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
