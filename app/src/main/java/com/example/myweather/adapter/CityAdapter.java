package com.example.myweather.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myweather.R;
import com.example.myweather.data.Location;
import com.example.myweather.pages.OnItemClickListener;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<Location> dataset;
    public static OnItemClickListener itemClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public void submit(List<Location> cities){
        dataset = cities;
        notifyDataSetChanged();
    }

    public CityAdapter(List<Location> data, OnItemClickListener listener) {
        dataset = data;
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city,parent,false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.bind(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {

        private final TextView city;
        private final View layout;
        private final Context context;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);
            layout = itemView.findViewById(R.id.container);
            context = itemView.getContext();
        }

        @SuppressLint("SetTextI18n")
        public void bind(Location item){
            city.setText(item.getCity());
            if(item.getSelected()){
                layout.setBackground(context.getDrawable(R.drawable.button));
            }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(item);
                }
            });
        }
    }

}
