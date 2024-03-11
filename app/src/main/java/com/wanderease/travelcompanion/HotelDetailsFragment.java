package com.wanderease.travelcompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

public class HotelDetailsFragment extends Fragment {

    private WebView webViewHotelUrl;

    public HotelDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_details, container, false);

        // Initialize WebView
        webViewHotelUrl = view.findViewById(R.id.webViewHotelUrl);

        // Enable JavaScript in WebView
        webViewHotelUrl.getSettings().setJavaScriptEnabled(true);

        // Load URL in WebView
        webViewHotelUrl.setWebViewClient(new WebViewClient());

        // Retrieve selected hotel information from arguments
        Bundle args = getArguments();
        if (args != null && args.containsKey("selectedHotel")) {
            String selectedHotelJson = args.getString("selectedHotel");
            Gson gson = new Gson();
            Hotel selectedHotel = gson.fromJson(selectedHotelJson, Hotel.class);
            if (selectedHotel != null) {
                // Load hotel URL in WebView
                webViewHotelUrl.loadUrl(selectedHotel.getUrl());
            }
        }

        return view;
    }
}
