package com.example.weathertest.presentation.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathertest.R;
import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private final Context context;
    private List<ApiResponse> items;
    private OnClickListener clickListener;

    public CitiesAdapter(Context context, List<ApiResponse> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false));
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApiResponse item = items.get(position);
        holder.cityName.setText(item.getName());
        item.getWeather().forEach(string -> holder.condition.setText(string.getMain()));
        holder.temp.setText(item.getMain().getTemp().toString());
        item.getWeather().forEach(icon-> Picasso.get().load(Constants.ICON_URL + icon.getIcon() + ".png").into(holder.icon));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClick(position, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setClickListener(OnClickListener onClickListener) {
        clickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, ApiResponse model);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView cityName;
        private final TextView condition;
        private final TextView temp;
        private final ImageView icon;

        ViewHolder(View view) {
            super(view);
            cityName = view.findViewById(R.id.city_name);
            condition = view.findViewById(R.id.cond);
            temp = view.findViewById(R.id.temp);
            icon = view.findViewById(R.id.icon);
        }
    }
}
