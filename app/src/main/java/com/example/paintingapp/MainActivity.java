package com.example.paintingapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private SeekBar brushSizeSeekBar;
    private int red = 0, green = 0, blue = 0;
    private Button redButton, greenButton, blueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.drawing_view);
        brushSizeSeekBar = findViewById(R.id.brush_size_seekbar);
        redButton = findViewById(R.id.button_red);
        greenButton = findViewById(R.id.button_green);
        blueButton = findViewById(R.id.button_blue);


        // Brush size seek bar listener
        brushSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawingView.setBrushSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Color picker dialogs for Red, Green, Blue
        redButton.setOnClickListener(view -> showColorPickerDialog("Red"));
        greenButton.setOnClickListener(view -> showColorPickerDialog("Green"));
        blueButton.setOnClickListener(view -> showColorPickerDialog("Blue"));
        Button clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(view -> drawingView.clearCanvas());

        // Clear button
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 and above, no need for WRITE_EXTERNAL_STORAGE permission
                drawingView.saveCanvas(this);
            } else {
                // For Android 6.0 to 9.0, request WRITE_EXTERNAL_STORAGE permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    drawingView.saveCanvas(this);
                }
            }
        });

    }

    // Method to show the color picker dialog
    private void showColorPickerDialog(final String colorType) {
        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View colorPickerView = inflater.inflate(R.layout.color_picker_dialog, null);

        SeekBar redSeekBar = colorPickerView.findViewById(R.id.seekBar_red);
        SeekBar greenSeekBar = colorPickerView.findViewById(R.id.seekBar_green);
        SeekBar blueSeekBar = colorPickerView.findViewById(R.id.seekBar_blue);
        ImageView colorDisplay = colorPickerView.findViewById(R.id.color_display);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        // Change color based on SeekBar values
        SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = redSeekBar.getProgress();
                green = greenSeekBar.getProgress();
                blue = blueSeekBar.getProgress();

                // Update the color display
                int selectedColor = Color.rgb(red, green, blue);
                colorDisplay.setBackgroundColor(selectedColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        };

        redSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangeListener);

        // Create and show the dialog
        new AlertDialog.Builder(this)
                .setTitle("Pick " + colorType + " Color")
                .setView(colorPickerView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Apply the selected color to the brush
                        int selectedColor = Color.rgb(red, green, blue);
                        drawingView.setColor(String.format("#%06X", (0xFFFFFF & selectedColor)));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            drawingView.saveCanvas(this);  // Call saveCanvas when permission is granted
        } else {
            Toast.makeText(this, "Permission denied! Cannot save image.", Toast.LENGTH_SHORT).show();
        }
    }
}
