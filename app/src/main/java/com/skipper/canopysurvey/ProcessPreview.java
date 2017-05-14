package com.skipper.canopysurvey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Skipper on 14/05/2017.
 */

public class ProcessPreview extends AppCompatActivity {
    private static ImageView imageView;
    private static SeekBar seekBar;
    private static Button process, reset;
    private static TextView progressText, percentageCover;
    private static Bitmap initialImage;

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

        progressText.setText(String.valueOf(seekBar.getProgress()));

        //Read bundle
        byte[] byteArray = getIntent().getExtras().getByteArray("image");
        initialImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imageView.setImageBitmap(initialImage); //And set the imageView to our bundled bitmap.

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
            float cover = ((float)black/(float)total)*100;
            percentageCover.setText(String.format("%.2f",cover));
            percentageCover.setVisibility(View.VISIBLE);
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


}
