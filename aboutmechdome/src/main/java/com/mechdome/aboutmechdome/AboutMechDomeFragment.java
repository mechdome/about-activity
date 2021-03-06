package com.mechdome.aboutmechdome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.mechdome.external.AppleAppStore;


import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AboutMechDomeFragment extends Fragment {
    private View view;
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    private void hideAds() {
        Button buyButton = (Button)view.findViewById(R.id.buttonBuy);
        Button restoreButton = (Button)view.findViewById(R.id.buttonRestore);
        TextView textOSS = (TextView)view.findViewById(R.id.textViewSupportOSS);

        buyButton.setVisibility(GONE);
        restoreButton.setVisibility(GONE);
        textOSS.setVisibility(GONE);
        mAdView.setSystemUiVisibility(GONE);

        getActivity().findViewById(android.R.id.content).requestLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_mech_dome, container, false);

        Button buyButton = (Button)view.findViewById(R.id.buttonBuy);
        Button restoreButton = (Button)view.findViewById(R.id.buttonRestore);

        Bundle bundle = null;
        try {
            ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (Exception e) { /* ignore */ }

        final String productId = bundle==null?null:bundle.getString("md_remove_ads");
        if (productId == null) {
            buyButton.setEnabled(false);
            restoreButton.setEnabled(false);
        } else {
            if (AppleAppStore.hasProduct(productId)) {
                hideAds();
            } else {
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppleAppStore.buy(productId, 1, new AppleAppStore.TransactionListener() {
                            public void onComplete(AppleAppStore.Status status) {
                                if (status == AppleAppStore.Status.Ok) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Thanks for supporting this app on iOS")
                                                    .setTitle("Thank you");
                                            builder.create().show();

                                            hideAds();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                restoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppleAppStore.restore(productId, new AppleAppStore.TransactionListener() {
                            public void onComplete(AppleAppStore.Status status) {
                                if (status == AppleAppStore.Status.Ok) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideAds();
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        }

        // Locate the NativeExpressAdView.
        mAdView = (NativeExpressAdView) view.findViewById(R.id.adView);

        // Set its video options.
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());

        // The VideoController can be used to get lifecycle events and info about an ad's video
        // asset. One will always be returned by getVideoController, even if the ad has no video
        // asset.
        mVideoController = mAdView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });

        mAdView.loadAd(new AdRequest.Builder().build());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView textView = (TextView) view.findViewById(R.id.textView);
        TextView textOSS = (TextView)view.findViewById(R.id.textViewSupportOSS);

        ColorDrawable drawable = (ColorDrawable) view.getRootView().getBackground();
        if (isColorDark(drawable.getColor())) {
            textView.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
            textOSS.setTextColor(getResources().getColor(android.R.color.primary_text_dark));
        } else {
            textView.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            textOSS.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        }
    }

    public boolean isColorDark(int color) {
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if (darkness<0.4) {
            return false;
        } else {
            return true;
        }
    }
}