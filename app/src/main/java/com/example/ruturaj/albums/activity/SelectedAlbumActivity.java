package com.example.ruturaj.albums.activity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.ruturaj.albums.R;
import com.example.ruturaj.albums.adapter.AlbumsAdapter;
import java.util.ArrayList;

public class SelectedAlbumActivity extends AppCompatActivity {

  public ArrayList<Uri> imageUriList = new ArrayList<Uri>();
  private AlbumsAdapter adapter;
  private Button button;
  private RecyclerView recyclerView;
  private GridLayoutManager layoutManager;
  private LayoutAnimationController animationController;
  private SQLiteDatabase AlbumsDB;
  private Intent intent;
  private String albumName;
  public static final int RESULT_GALLERY = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_selected_album);

    AlbumsDB = this.openOrCreateDatabase("ALBUMS", MODE_PRIVATE, null);
    AlbumsDB.execSQL("CREATE TABLE IF NOT EXISTS album (albumName VARCHAR, uri VARCHAR)");

    intent = getIntent();
    albumName = intent.getStringExtra("albumName");
    Log.i("Album Name", albumName);

    updateView();

    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    layoutManager = new GridLayoutManager(getApplicationContext(), 5);
    recyclerView.setLayoutManager(layoutManager);
    adapter = new AlbumsAdapter(imageUriList, getApplicationContext());
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
    button = (Button) findViewById(R.id.button);
    animationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(),
        R.anim.grid_layout_animation_from_bottom);
    recyclerView.setLayoutAnimation(animationController);

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        YoYo.with(Techniques.Pulse).duration(600).playOn(findViewById(R.id.button));
        Intent intent = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_GALLERY);
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    String sql = "INSERT INTO album (albumName, uri) VALUES (?, ?)";

    switch (requestCode) {
      case RESULT_GALLERY:
        if (data.getClipData() != null) {
          ClipData clipData = data.getClipData();
          for (int i = 0; i < clipData.getItemCount(); i++) {
            ClipData.Item item = clipData.getItemAt(i);
            Uri uri = item.getUri();
            imageUriList.add(uri);
            SQLiteStatement statement = AlbumsDB.compileStatement(sql);
            statement.bindString(1, albumName);
            statement.bindString(2, uri.toString());
            statement.execute();
          }
          updateView();
          Log.i("Uri:", imageUriList.toString());
          adapter = new AlbumsAdapter(imageUriList, getApplicationContext());
          adapter.notifyDataSetChanged();
          recyclerView.setAdapter(adapter);
          recyclerView.setLayoutAnimation(animationController);
          recyclerView.getAdapter().notifyDataSetChanged();
          recyclerView.scheduleLayoutAnimation();
        }
    }
  }

  public void updateView(){
    Cursor cursor = AlbumsDB.rawQuery("SELECT * FROM album WHERE albumName = '" + albumName + "'", null);
    if(cursor.moveToFirst())
      imageUriList.clear();
    else{
      return;
    }
    do{
      imageUriList.add(Uri.parse(cursor.getString(cursor.getColumnIndex("uri"))));
      Log.i("UriList:", cursor.getString(cursor.getColumnIndex("uri")));
    }while (cursor.moveToNext());
    cursor.close();
  }
}
