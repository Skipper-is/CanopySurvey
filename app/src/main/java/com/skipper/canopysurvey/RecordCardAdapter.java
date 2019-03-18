package com.skipper.canopysurvey;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Caplin on 18/03/2019.
 */
public class RecordCardAdapter extends RecyclerView.Adapter<RecordCardAdapter.MyViewHolder> {
    private List<CoverRecord> recordList = new ArrayList<>();
    private Activity activity;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView locationText, coverText;
        public ImageView imageView;
        public ImageButton deleteButton;
        public int id;
        public double lat, lng;
        public byte[] image;
        public MyViewHolder(View view){
            super(view);
            locationText = view.findViewById(R.id.card_location_data);
            coverText = view.findViewById(R.id.card_cover_percent);
            imageView = view.findViewById(R.id.cardImage);
            deleteButton = view.findViewById(R.id.cardDeleteButton);



        }
    }
    public RecordCardAdapter(List<CoverRecord> coverRecords){
        this.recordList = coverRecords;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    CoverRecord record = recordList.get(position);
    Log.d("List", String.valueOf(record.get_id()));
    holder.locationText.setText(String.valueOf(record.get_lat())+ ", " + String.valueOf(record.get_lng()));
    holder.coverText.setText(String.valueOf(record.get_cover()+ "%"));
    holder.imageView.setImageBitmap(imageCreator(record.get_image()));

    holder.deleteButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = record.get_id();
            Log.d("Cover","ID is:" + String.valueOf(id));
            DatabaseHelper db = new DatabaseHelper(v.getContext());
            db.deleteRecord(id);
            db.close();
            recordList.remove(position);
            notifyItemRemoved(position);

        }
    });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
    private Bitmap imageCreator(byte[] imageByte){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);
        return bitmap;
    }

}
