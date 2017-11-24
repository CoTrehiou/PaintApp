package com.example.uqac.paintapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.util.TypedValue;


/**
 * Created by ctrehiou on 2017-11-22.
 */

public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap, imageBitmap;

    private float brushSize, lastBrushSize;
    private boolean erase=false;

    private int imageCount=0;

    public DrawingView(Context context, AttributeSet attrs){
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
        imageBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.image0);
        }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {//view given size
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createScaledBitmap(imageBitmap,w, h, false);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//draw viewx
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
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

    public void setColor(String newColor){
        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize){
//update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
//set erase true or false
        erase=isErase;
        if(erase) {
            //canvasBitmap.eraseColor(paintColor);
            this.setColor("#FFFFFFFF");
           //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        }

        else
          drawPaint.setColor(paintColor);
        //drawPaint.setXfermode(null);
    }


    public void startNew(){//reinitialisation de l'image
        canvasBitmap.recycle();
        canvasBitmap = Bitmap.createScaledBitmap(imageBitmap,canvasBitmap.getWidth(), canvasBitmap.getHeight(),true);
        drawCanvas = new Canvas(canvasBitmap);
        invalidate();
    }

    public void startNext() {
        imageCount++;
        String nameImage = new String("image"+imageCount);
        Log.e("IMAGE COUNT", nameImage);
        int idImage = getResources().getIdentifier(nameImage , "drawable", "com.example.uqac.paintapp");

        canvasBitmap.recycle();
        imageBitmap.recycle();
        imageBitmap = BitmapFactory.decodeResource(getResources(),idImage);
        canvasBitmap = Bitmap.createScaledBitmap(imageBitmap,canvasBitmap.getWidth(), canvasBitmap.getHeight(),true);
        drawCanvas = new Canvas(canvasBitmap);


        invalidate();
    }

    public void startPrevious() {
        if (imageCount > 0) {
            imageCount--;
            String nameImage = new String("image" + imageCount);
            Log.e("IMAGE COUNT", nameImage);
            int idImage = getResources().getIdentifier(nameImage, "drawable", "com.example.uqac.paintapp");

            canvasBitmap.recycle();
            imageBitmap.recycle();
            imageBitmap = BitmapFactory.decodeResource(getResources(), idImage);
            canvasBitmap = Bitmap.createScaledBitmap(imageBitmap, canvasBitmap.getWidth(), canvasBitmap.getHeight(), true);
            drawCanvas = new Canvas(canvasBitmap);
            invalidate();
        }
    }
}
