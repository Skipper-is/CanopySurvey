package com.skipper.canopysurvey;

import android.content.Intent;
import android.content.res.Resources;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DisplayData extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecordCardAdapter mAdapter;
    private List<CoverRecord> recordList = new ArrayList<>();
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        recyclerView = findViewById(R.id.recycler_view);
        new getRecordListAsync().execute();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.HORIZONTAL));
        mAdapter = new RecordCardAdapter(recordList);
        recyclerView.setAdapter(mAdapter);
    }

    private int dpToInt(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);

    }


    private void recordListFiller(){
        List<CoverRecord> _tempRecords = new ArrayList<>();

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
    private class getRecordListAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            recordList = getRecords();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("COVER","Length of list is:" + recordList.size());
            mAdapter = new RecordCardAdapter(recordList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }
    private List<CoverRecord> getRecords(){
        db = new DatabaseHelper(getApplicationContext());
        List<CoverRecord> list = db.getList();
        db.close();
        return list;
    }

}
