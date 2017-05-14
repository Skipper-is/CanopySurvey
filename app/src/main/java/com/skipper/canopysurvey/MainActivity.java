package com.skipper.canopysurvey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1 ;
    private static Bundle extras;

    public Button takePhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Define objects
        takePhoto = (Button) findViewById(R.id.takePhoto);



        takePhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               dispatchTakePictureIntent();
            }
        });


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Intent passthrough = new Intent(this, ProcessPreview.class);
            Bundle passBundle = new Bundle();
            //Convert bitmap to byte array

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            passBundle.putByteArray("image",byteArray);
          //If we want to add anything to the bundle....
            passthrough.putExtras(passBundle);
            startActivity(passthrough);

        }
    }

}
