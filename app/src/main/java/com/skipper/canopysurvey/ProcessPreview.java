package com.skipper.canopysurvey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Skipper on 14/05/2017.
 */

public class ProcessPreview extends AppCompatActivity {
    private static ImageView imageView;
    private static SeekBar seekBar;
    private static Button process, reset, next;
    private static TextView progressText, percentageCover;
    private static Bitmap initialImage;
    private static float cover = 0;
    private String byteArrayName;
    private byte[] byteArray;
    private static int done = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_preview);

      


        //Define the objects
        imageView = (ImageView) findViewById(R.id.imageView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        reset = (Button) findViewById(R.id.reset);
        process = (Button) findViewById(R.id.process);
        progressText = (TextView) findViewById(R.id.seekBarProgress);
        percentageCover = (TextView) findViewById(R.id.percentageCover);
        next = (Button) findViewById(R.id.nextGetLocation);

        progressText.setText(String.valueOf(seekBar.getProgress()));


        //Read bundle - and see if we've got a saved instance
        if (savedInstanceState != null){
            byteArrayName = "imagesave";
            byteArray = savedInstanceState.getByteArray(byteArrayName);
            seekBar.setProgress(savedInstanceState.getInt("seekBarProgress"));

            percentageCover.setText(String.format("%.2f",savedInstanceState.getFloat("percentCover")) + "% cover");
            progressText.setText(String.valueOf(savedInstanceState.getInt("seekBarProgress")));
            if (done == 1){
                percentageCover.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                 }else{
                percentageCover.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);
            }



            Bitmap restoredImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(restoredImage);

        }else{ //this is in case we've not already saved/rotated/processed etc the images.
            byteArrayName = "image";

            byteArray = getIntent().getExtras().getByteArray(byteArrayName);

            initialImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(initialImage);

            percentageCover.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
        }


        //Process the SeekBar

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressText.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public void processPressed (View v){
        imageView.setImageBitmap(runProcessing(initialImage));

    }

    public void resetPressed (View v){
        imageView.setImageBitmap(initialImage);
        percentageCover.setText("");
        percentageCover.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        done = 0;
    }

    public void nextPressed (View v){
        Bundle bundle = new Bundle();

        Bitmap save = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
        save.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray2 = stream.toByteArray();
        bundle.putByteArray("imagesave",byteArray2);
        bundle.putFloat("percentCover",cover);

        Intent intent = new Intent(this,getLocation.class);
        intent.putExtras(bundle);
        startActivity(intent);

            }

    public static Bitmap runProcessing(Bitmap before){
        if (before !=null){
            int BLUE = 0;
            Bitmap after = Bitmap.createBitmap(before.getWidth(),before.getHeight(),before.getConfig());

            int pixel, width, height, total, black = 0, blue = 0;
            width = before.getWidth();
            height = before.getHeight();
            total = width* height;

            for (int x = 0; x < width; ++x){
                for (int y = 0; y < height; ++y){

                    pixel = before.getPixel(x,y);
                    BLUE = Color.blue(pixel);

                    if(BLUE < seekBar.getProgress()){
                        BLUE = 0;
                        black ++;
                    }else{
                        BLUE = 255;
                        blue++;
                    }
                    after.setPixel(x,y,Color.argb(255,0,0,BLUE));

                }
            }
            cover = ((float)black/(float)total)*100;
            percentageCover.setText(String.format("%.2f",cover) + "% cover");
            percentageCover.setVisibility(View.VISIBLE);
            done = 1;
            next.setVisibility(View.VISIBLE);
            return after;
        } else {
            return null;
            //TODO add a null return.
        }

    }

/*        if(extras != null){ //If the bundle is already there... Dont know if this will solve the rotation issue
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Bitmap save = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        int percentsave = seekBar.getProgress();
        //float cover is there too

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        save.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray2 = stream.toByteArray();
        outState.putByteArray("imagesave",byteArray2);


        outState.putInt("seekBarProgress",percentsave);

        outState.putFloat("percentCover",cover);

        super.onSaveInstanceState(outState);
    }


}
