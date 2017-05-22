package com.mechdome.aboutmechdome;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mechdome.external.AppleAppStore;

import static android.view.View.GONE;

/**
 * Created by nandu on 5/10/17.
 */

public class DonateMechDomeFragment extends Fragment {
    private View view;
    private RadioGroup radioGroup;
    private Button donateButton;
    private void hideAds() {
        donateButton.setVisibility(GONE);
        radioGroup.setVisibility(GONE);
        getActivity().findViewById(android.R.id.content).requestLayout();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_mechdome_donate, container, false);
        donateButton = (Button)view.findViewById(R.id.buttonDonate);
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if(checkedRadioButtonId == -1){
                    Toast.makeText(getActivity(), "Select a category below to Donate.", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    RadioButton radioButton = (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
                    String radioTitle = (String) radioButton.getText();
                    Bundle bundle = null;
                    try {
                        ApplicationInfo ai = getActivity().getPackageManager().getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
                        bundle = ai.metaData;
                    } catch (Exception e) { /* ignore */ }

                    String productId = "";
                    if (radioTitle.equalsIgnoreCase("Large")){
                        productId = (bundle==null?null:bundle.getString("md_donate_large"));
                    }else if (radioTitle.equalsIgnoreCase("Medium")){
                        productId = (bundle==null?null:bundle.getString("md_donate_medium"));
                    }else{
                        productId = (bundle==null?null:bundle.getString("md_donate_small"));
                    }
//                    Toast.makeText(getActivity(), productId, Toast.LENGTH_LONG).show();
                    AppleAppStore.buy(productId, 1, new AppleAppStore.TransactionListener() {
                        public void onComplete(AppleAppStore.Status status) {
                            if (status == AppleAppStore.Status.Ok) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Thanks for Donating this app on iOS")
                                                .setTitle("Thank you");
                                        builder.create().show();

                                        hideAds();
                                    }
                                });
                            }else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Donation Failed")
                                                .setTitle("OK");
                                        builder.create().show();


                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView textView = (TextView) view.findViewById(R.id.textView_donate);
        TextView textOSS = (TextView)view.findViewById(R.id.textViewSupportOSS_donate);

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
