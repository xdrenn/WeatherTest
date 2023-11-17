package com.example.weathertest.presentation.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathertest.R;
import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.utils.Constants;
import com.squareup.picasso.Picasso;

class ViewHolder extends RecyclerView.ViewHolder {

    private final TextView cityName;
    private final TextView condition;
    private final TextView temp;
    private final ImageView weatherIcon;

    ViewHolder(@NonNull View view) {
        super(view);
        cityName = view.findViewById(R.id.city_name);
        condition = view.findViewById(R.id.cond);
        temp = view.findViewById(R.id.temp);
        weatherIcon = view.findViewById(R.id.icon);
    }

    @SuppressLint("SetTextI18n")
    public void bind(@NonNull ApiResponse item) {
        cityName.setText(item.getName());
        item.getWeather().forEach(string -> condition.setText(string.getMain()));
        temp.setText(Math.round(item.getMain().getTemp()) + "°");
        item.getWeather().forEach(icon -> Picasso.get().load(Constants.ICON_URL + icon.getIcon() + ".png").into(weatherIcon));
    }
}
