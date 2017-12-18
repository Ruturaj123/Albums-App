package com.example.ruturaj.albums.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.ruturaj.albums.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private ListView albums_list;
  private ArrayList<String> album_names = new ArrayList<String>();
  private FloatingActionButton fab;
  private ArrayAdapter<String> adapter;
  private SQLiteDatabase AlbumNamesDB;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AlbumNamesDB = this.openOrCreateDatabase("AlbumNames", MODE_PRIVATE, null);
    AlbumNamesDB.execSQL("CREATE TABLE IF NOT EXISTS albumNames (name VARCHAR)");

    updateList();

    albums_list = (ListView) findViewById(R.id.albums_list);
    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, album_names);
    albums_list.setAdapter(adapter);
    adapter.notifyDataSetChanged();
    fab = (FloatingActionButton) findViewById(R.id.new_album_fab);
    final EditText input = new EditText(getApplicationContext());

    fab.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        YoYo.with(Techniques.RubberBand).duration(1000).playOn(findViewById(R.id.new_album_fab));
        final AlertDialog.Builder builder = new Builder(MainActivity.this);
        builder.setTitle("Title");
        if(input.getParent() != null){
          ((ViewGroup)input.getParent()).removeView(input);
          input.setText("");
        }
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            album_names.add(input.getText().toString());
            String sql= "INSERT INTO albumNames VALUES (?)";
            SQLiteStatement statement = AlbumNamesDB.compileStatement(sql);
            statement.bindString(1, input.getText().toString());
            statement.execute();
            updateList();
            Log.i("name:", input.getText().toString());
            adapter.notifyDataSetChanged();
          }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
      }
    });

    albums_list.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), SelectedAlbumActivity.class);
        intent.putExtra("albumName", album_names.get(i));
        startActivity(intent);
      }
    });
  }

  public void updateList(){
    Cursor cursor = AlbumNamesDB.rawQuery("SELECT * FROM albumNames", null);
    if(cursor.moveToFirst())
      album_names.clear();
    else
      return;

    do{
      album_names.add(cursor.getString(cursor.getColumnIndex("name")));
      Log.i("Album Name:", cursor.getString(cursor.getColumnIndex("name")));
    }while (cursor.moveToNext());
    cursor.close();
  }
}
