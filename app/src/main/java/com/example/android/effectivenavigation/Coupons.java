package com.example.android.effectivenavigation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;


public class Coupons extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.coupons_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.coupons_textView);
        ImageView icon1 = (ImageView) view.findViewById(R.id.imageView1);
        ImageView icon2 = (ImageView) view.findViewById(R.id.imageView2);
        ImageView icon3 = (ImageView) view.findViewById(R.id.imageView3);
        ImageView icon4 = (ImageView) view.findViewById(R.id.imageView4);
        return view;
    }
}