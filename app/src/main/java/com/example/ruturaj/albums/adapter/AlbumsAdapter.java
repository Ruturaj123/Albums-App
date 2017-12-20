package com.example.ruturaj.albums.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ruturaj.albums.R;

import com.example.ruturaj.albums.activity.WallpaperActivity;
import java.util.ArrayList;

/**
 * Created by ruturaj on 16/12/17.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

  private static ArrayList<Uri> imageUriList;
  private static Context context;

  public AlbumsAdapter(ArrayList<Uri> imageUriList, Context context) {
    this.imageUriList = imageUriList;
    this.context = context;
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;

    public MyViewHolder(View view) {
      super(view);
      imageView = (ImageView) view.findViewById(R.id.image_view);
      view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      Uri imageUri = imageUriList.get(getAdapterPosition());
      Intent intent = new Intent(context, WallpaperActivity.class);
      Log.i("Wallpaper:", imageUri.toString());
      intent.putExtra("imageUri", imageUri.toString());
      context.startActivity(intent);
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
