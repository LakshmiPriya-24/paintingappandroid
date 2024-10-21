package com.example.paintingapp;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.*;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;
    private float brushSize;
    private int paintColor = Color.BLACK;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
        brushSize = 20;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor(String newColor) {
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize) {
        brushSize = newSize;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void clearCanvas() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void saveCanvas(Context context) {
        String fileName = "drawing_" + System.currentTimeMillis() + ".png";  // Generate a unique filename

        OutputStream outputStream;
        Uri imageUri = null;

        // Set up content values for saving the image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        // Use the MediaStore API for Android 10 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);  // Save to Pictures directory
            imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            // For Android versions below 10, use the legacy method
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            values.put(MediaStore.Images.Media.DATA, imagesDir + "/" + fileName);
            imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        if (imageUri != null) {
            try {
                outputStream = context.getContentResolver().openOutputStream(imageUri);
                if (outputStream != null) {
                    // Compress the bitmap as PNG and write it to the output stream
                    canvasBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    // Show success message
                    Toast.makeText(context, "Image saved successfully in Gallery!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error: Could not open output stream.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error: Could not save image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Error: Could not create file.", Toast.LENGTH_SHORT).show();
        }
    }
}
