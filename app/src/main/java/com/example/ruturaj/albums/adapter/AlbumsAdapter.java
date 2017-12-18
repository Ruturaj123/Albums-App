package com.example.ruturaj.albums.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ruturaj.albums.R;

import java.util.ArrayList;

/**
 * Created by ruturaj on 16/12/17.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    private ArrayList<Uri> imageUriList;
    private Context context;

    public AlbumsAdapter(ArrayList<Uri> imageUriList, Context context){
        this.imageUriList = imageUriList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context).load(imageUriList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUriList.size();
    }
}
