package com.mihai.colorcodecreator;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlidingFragment extends Fragment {

    public static final String BUNDLE_COD = "bundleCod";
    public static final String BUNDLE_NUME = "bundleNume";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View radacina = inflater.inflate(R.layout.fragment_sliding, container, false);
        Bundle b = getArguments();
        int culoare = b.getInt(BUNDLE_COD);
        String nume = b.getString(BUNDLE_NUME);
        ConstraintLayout sliderLayout = (ConstraintLayout) radacina.findViewById(R.id.sliderLayout);
        sliderLayout.setBackgroundColor(culoare);
        TextView sliderCod = (TextView) radacina.findViewById(R.id.sliderCodText);
        sliderCod.setText(nume);
        return radacina;
    }

}
