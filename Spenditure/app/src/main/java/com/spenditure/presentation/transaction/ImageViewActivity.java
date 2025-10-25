/**
 * ImageViewActivity.java
 * <p>
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Tuesday, March 5, 2024
 * <p>
 * PURPOSE:
 * This file allows users to view images attached to a transaction
 **/

package com.spenditure.presentation.transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.spenditure.R;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        setUpConfirmButton();

        // Get the passed in information from the calling activity
        Intent data = getIntent();
        byte[] imageBytes = getIntent().getByteArrayExtra("imageBytes");

        if (imageBytes != null) {
            // Decompress the byte[]
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            ImageView imageView = (ImageView) findViewById(R.id.imageview_image);
            imageView.setImageBitmap(bitmap);

            // Rotate the image so it displays properly
            imageView.setRotation(270);
        } else {
            Toast.makeText(ImageViewActivity.this, "Error retrieving image.", Toast.LENGTH_LONG).show();
        }
    }

    // Set up the confirm button
    private void setUpConfirmButton(){
        Button button = (Button) findViewById(R.id.button_back);

        button.setOnClickListener(view -> {
            // Return to the calling activity
            finish();
        });
    }
}