package com.mechdome.aboutmechdome;

import android.app.AlertDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mechdome.external.AppleAppStore;
import com.mechdome.view.google.AdMobNativeView;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AboutMechDomeFragment extends Fragment {
    private View view;

    private void hideAds() {
        Button buyButton = (Button)view.findViewById(R.id.buttonBuy);
        Button restoreButton = (Button)view.findViewById(R.id.buttonRestore);
        View adView = view.findViewById(R.id.adview);

        adView.setVisibility(GONE);
        buyButton.setVisibility(GONE);
        restoreButton.setVisibility(GONE);

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

        boolean testMode = Build.CPU_ABI.contains("86") ? true : ((AboutMechDomeActivity)getActivity()).inTestMode;
        AdMobNativeView adView = (AdMobNativeView)view.findViewById(R.id.adview);
        adView.init("ca-app-pub-2729669460650010~5828110486", "ca-app-pub-2729669460650010/7304843682", testMode);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView textView = (TextView) view.findViewById(R.id.textView);
        ColorDrawable drawable = (ColorDrawable) view.getRootView().getBackground();
        if (isColorDark(drawable.getColor())) {
            textView.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            textView.setTextColor(Color.parseColor("#000000"));
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