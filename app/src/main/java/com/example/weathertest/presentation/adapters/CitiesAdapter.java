package com.example.weathertest.presentation.adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathertest.data.model.ApiResponse;
import com.example.weathertest.databinding.ItemCityBinding;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {
    private final List<ApiResponse> items;
    private OnClickListener onClickListener;

    public CitiesAdapter(List<ApiResponse> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApiResponse item = items.get(position);
        holder.setCityName(item.getName());
        item.getWeather().forEach(string -> holder.setCondition(string.getMain()));
        holder.setTemp(item.getMain().getTemp().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, ApiResponse model);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCityBinding binding;
        ViewHolder(ItemCityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        final ItemCityBinding getBinding() {
            return binding;
        }

        final void setBinding(ItemCityBinding binding) {
            this.binding = binding;
        }

        final void setCityName(CharSequence value) {
            binding.cityName.setText(value);
        }

        final void setCondition (CharSequence value) {
            binding.condition.setText(value);
        }
        final void setTemp(CharSequence value){
            binding.temp.setText(value);
        }

        final CharSequence getTvName() {
            return binding.cityName.getText();
        }

        final CharSequence getCondition() {
            return binding.condition.getText();
        }

        final CharSequence getTemp(){
            return binding.temp.getText();
        }
    }
}
