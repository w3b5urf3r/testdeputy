package com.deputy.test.mariolopez.ui.controllers.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deputy.test.mariolopez.BR;
import com.deputy.test.mariolopez.beans.Shift;
import com.deputy.test.mariolopez.databinding.ShiftListItemBinding;

import java.util.List;

/**
 * Created by mario on 24/02/2017.
 */

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {
    private final int layoutId;
    private final View.OnClickListener clickListener;
    private List<Shift> dataSet;

    public void setDataSet(List<Shift> dataSet) {
        this.dataSet = dataSet;
    }

    public List<Shift> getDataSet() {
        return dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private final ShiftListItemBinding binding;

        public ViewHolder(ShiftListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Shift shift, View.OnClickListener clickListener) {
            binding.setVariable(BR.shift, shift);
            binding.executePendingBindings();
            binding.getRoot().setTag(shift);
            binding.getRoot().setOnClickListener(clickListener);
        }
    }

    public ShiftAdapter(int layoutID, View.OnClickListener onItemClickListener) {
        this.layoutId=layoutID;
        this.clickListener=onItemClickListener;
    }

    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ShiftListItemBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(dataSet.get(position),clickListener);

    }

    @Override
    public int getItemViewType(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

