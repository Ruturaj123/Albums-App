package com.example.ruturaj.albums.activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.ruturaj.albums.R;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WallpaperActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wallpaper);

    Intent intent = getIntent();
    String imageUri = intent.getStringExtra("imageUri");
    final Uri uri = Uri.parse(imageUri);
    ImageView imageView = (ImageView) findViewById(R.id.wallpaperImage);
    Glide.with(getApplicationContext()).load(uri).into(imageView);
    Button setWallpaperButton = (Button) findViewById(R.id.setWallpaperButton);
    setWallpaperButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallpaperActivity.this);
        try{
          Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
          DisplayMetrics metrics = new DisplayMetrics();
          getWindowManager().getDefaultDisplay().getMetrics(metrics);
          int height = metrics.heightPixels;
          int width = metrics.widthPixels;
          wallpaperManager.setBitmap(bitmap);
          wallpaperManager.suggestDesiredDimensions(width, height);
          Toast.makeText(getApplicationContext(), "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
          Toast.makeText(getApplicationContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
