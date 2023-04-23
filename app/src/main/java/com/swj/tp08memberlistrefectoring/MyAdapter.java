package com.swj.tp08memberlistrefectoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swj.tp08memberlistrefectoring.databinding.ListviewItemBinding;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Item> itemArrayList;

    public MyAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListviewItemBinding binding = ListviewItemBinding.inflate(LayoutInflater.from(context));

        Item item = itemArrayList.get(i);
        binding.tvName.setText(item.name);
        binding.tvNation.setText(item.nation);
        binding.tvGender.setText(item.gender);
        binding.ivNationalFlag.setImageResource(item.imgId);
        return binding.getRoot();
    }
}
