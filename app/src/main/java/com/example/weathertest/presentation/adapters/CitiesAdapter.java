package com.example.weathertest.presentation.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathertest.R;
import com.example.weathertest.data.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final List<ApiResponse> items;
    private OnClickListener clickListener;

    public CitiesAdapter(Context context) {
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false));
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onClick(position, items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ApiResponse> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void setClickListener(OnClickListener onClickListener) {
        clickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, ApiResponse model);
    }
}
