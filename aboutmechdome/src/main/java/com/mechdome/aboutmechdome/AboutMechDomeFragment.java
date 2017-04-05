package com.mechdome.aboutmechdome;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mechdome.view.google.AdMobNativeView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AboutMechDomeFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_about_mech_dome, container, false);
        AdMobNativeView adView = (AdMobNativeView)view.findViewById(R.id.adview);
        adView.init("ca-app-pub-2729669460650010~5828110486", "ca-app-pub-2729669460650010/7304843682", false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView textView = (TextView) view.findViewById(R.id.textView);
        ColorDrawable drawable = (ColorDrawable) view.getRootView().getBackground();
        if (isColorDark(drawable.getColor())){
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            textView.setTextColor(Color.parseColor("#000000"));
        }
    }


    public boolean isColorDark(int color) {
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.4){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }
}