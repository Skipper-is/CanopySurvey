package com.skipper.canopysurvey;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static java.security.AccessController.getContext;

public class DisplayData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        //add header to table
        TableRow row = new TableRow(this);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(dpToInt(2),dpToInt(2),dpToInt(2),dpToInt(2));
        params.gravity = Gravity.CENTER;
        row.setLayoutParams(params);

        String[] headerText={"ID","Image","Cover %", "Delete",};
        for (String c:headerText){
            TextView tv = new TextView(this);

            tv.setLayoutParams(params);

            tv.setTextSize(dpToInt(5));
            tv.setTextColor(ContextCompat.getColor(this,R.color.backgroundColour));
            tv.setText(c);
            row.addView(tv);
        }
        row.setBackgroundResource(R.color.colorPrimaryDark);
        tableLayout.addView(row);

        final DatabaseHelper db = new DatabaseHelper(this);
        SQLiteDatabase database = db.getReadableDatabase();
        database.beginTransaction();
        try{
            String selectQuery = "Select * FROM " + DatabaseHelper.TABLE_NAME;
            Cursor cursor = database.rawQuery(selectQuery,null);

            if(cursor.getCount()>0) {
                while (cursor.moveToNext()) {
                    final int _id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_ID));
                    byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_IMAGE));
                    float cover = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_COVER));
                    double lat = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_LAT));
                    double lng = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_LON));

                    row = new TableRow(this);
                    row.setLayoutParams(params);

                    //Oh this is annoying, I'm going to have to do each one individually
                    TextView tv = new TextView(this);

                    tv.setLayoutParams(params);
                    tv.setTextSize(dpToInt(5));
                    tv.setText(String.valueOf(_id));
                    row.addView(tv);

                    //Image! This is the tricky bit
                    ImageView imageView = new ImageView(this);

                    imageView.setImageBitmap(new bitmapHelper().bitmapHelper(byteArray));
                    imageView.setLayoutParams(params);
                    row.addView(imageView);


                    tv = new TextView(this);
                    tv.setLayoutParams(params);

                    tv.setTextSize(dpToInt(5));
                    tv.setText(String.format("%.2f",cover) + "%");
                    row.addView(tv);

                    ImageButton imageButton = new ImageButton (this);
                    Drawable icon = getResources().getDrawable(android.R.drawable.ic_delete);
                    imageButton.setBackgroundDrawable(icon);
                    imageButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));


                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteRecord(_id);

                            reset();
                        }
                    });

                    row.addView(imageButton);

                    tableLayout.addView(row);


                }
                database.setTransactionSuccessful();
            }
        }catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            database.endTransaction();
            database.close();
            db.close();
        }







    }

    private int dpToInt(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

    private void deleteRecord(int id) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteRecord(id);
        reset();
    }

    private void reset() {
        Intent intent = new Intent(this, DisplayData.class);
        startActivity(intent);
    }
}
