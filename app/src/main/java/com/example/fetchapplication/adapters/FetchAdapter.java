package com.example.fetchapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fetchapplication.model.Item;
import com.example.fetchapplication.R;

import java.util.List;

public class FetchAdapter extends RecyclerView.Adapter<FetchAdapter.MyViewHolder> {

    private List<Item> nameList;

    public FetchAdapter(List<Item> values) {
        nameList = values;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView listId;
        public TextView itemName;
        public LinearLayout linearLayout;

        public MyViewHolder(View v){

            super(v);
            itemName = (TextView)v.findViewById(R.id.item_text);
            listId = (TextView)v.findViewById(R.id.list_id_text);
            linearLayout = v.findViewById(R.id.linear_layout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final String name = nameList.get(position).getName();
        final int id = nameList.get(position).getListId();
        holder.itemName.setText(name);
        holder.listId.setText(String.valueOf(id));
    }
    @Override
    public int getItemCount() {
        return nameList.size();
    }

}