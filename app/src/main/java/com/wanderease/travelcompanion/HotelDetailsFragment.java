package com.wanderease.travelcompanion;

import android.content.Intent;
import android.net.Uri;
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
        webViewHotelUrl.setWebViewClient(new CustomWebViewClient());

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

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Check if the URL is a phone number (starts with "tel:")
            if (url.startsWith("tel:")) {
                // Initiate a phone call using Intent
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true; // Indicate that the URL loading is handled
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
