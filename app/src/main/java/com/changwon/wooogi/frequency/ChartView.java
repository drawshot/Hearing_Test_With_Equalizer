package com.changwon.wooogi.frequency;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import java.util.ArrayList;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by it on 2017. 7. 24..
 */

public class ChartView extends View {
    int clientHeight;
    int clientWidth;
    public int innerVerticalTickCount;
    public int innerHorizontalTickCount;
    public int outerRectMargin;
    public ArrayList<String> topLabeles;
    public ArrayList<String> leftLabeles;
    public String strTopUnit;
    public String strLeftUnit;

    public ArrayList<Double> dataO;
    public ArrayList<Double> dataX;

    double minLevel;
    double maxLevel;

    public ChartView(Context context){
        super(context);

    }

    public void onDraw(Canvas canvas){
        //getSize
        clientHeight = this.getHeight();
        clientWidth = this.getWidth();

        //prepare Paint
        Paint pnt = new Paint();

        //fill background
        pnt.setColor(Color.WHITE);
        canvas.drawRect(0,0,clientWidth, clientHeight,pnt);

        //drawing outter Rectangle
        pnt.setColor(Color.BLACK);
        pnt.setStyle(Paint.Style.STROKE);
        pnt.setStrokeWidth(2);
        pnt.setAntiAlias(true);
        canvas.drawRect(outerRectMargin,outerRectMargin,clientWidth - outerRectMargin, clientHeight - outerRectMargin,pnt);

        pnt.setStrokeWidth(1);
        pnt.setTextSize(30);

        pnt.setTextAlign(Paint.Align.CENTER);
        //drawing Vertical Line
        float verticalCap = (clientWidth - (outerRectMargin*2)) / (innerVerticalTickCount + 1);
        for(int a = 0; innerVerticalTickCount > a ; a++){
            canvas.drawLine(outerRectMargin + verticalCap * (a+1), outerRectMargin, outerRectMargin + verticalCap * (a+1),clientHeight-outerRectMargin,pnt);
            canvas.drawText(topLabeles.get(a),outerRectMargin + verticalCap * (a+1),outerRectMargin - 15,pnt);
        }canvas.drawText(strTopUnit,outerRectMargin + verticalCap * (innerVerticalTickCount+1),outerRectMargin - 15,pnt);

        //drawing Horizontal
        pnt.setTextAlign(Paint.Align.RIGHT);
        float horizontalCap = (clientHeight - (outerRectMargin*2)) / (innerHorizontalTickCount +1);
        for(int a = 0 ; innerHorizontalTickCount > a ; a++){
            canvas.drawLine(outerRectMargin,outerRectMargin + horizontalCap * (a+1), clientWidth - outerRectMargin, outerRectMargin + horizontalCap * (a+1),pnt);
            canvas.drawText(leftLabeles.get(a),outerRectMargin - 15,outerRectMargin + horizontalCap * (a+0.5f),pnt);
        }canvas.drawText(strLeftUnit,outerRectMargin - 15,outerRectMargin + horizontalCap * (innerHorizontalTickCount+2),pnt);

        Resources res = getResources();

        //drawing dataO graph

        float prevMarkOLeft = -1;
        float prevMarkOTop = -1;
        float prevMarkXLeft = -1;
        float prevMarkXTop = -1;
        for(int a = 0; innerVerticalTickCount > a ; a++){

            //prepare image MarkO
            BitmapDrawable drawableMarkO;
            if(SDK_INT >= 21) {
                drawableMarkO = (BitmapDrawable)res.getDrawable(R.drawable.icon1, null);
            }
            else {
                drawableMarkO = (BitmapDrawable)res.getDrawable(R.drawable.icon1);
            }

            Bitmap btmMarkO = drawableMarkO.getBitmap();

            //caculate postion MarkO
            float unitRatio = (float) ((clientHeight - (2*outerRectMargin) - (horizontalCap) ) / (maxLevel - minLevel + 10));
            float markOPosLeft = outerRectMargin + verticalCap * (a+1);
            float markOPosTop = (float) ((outerRectMargin + dataO.get(a) * unitRatio ) - unitRatio * (minLevel));

            //draw MarkO
            RectF rectMarkO = new RectF(markOPosLeft-15,markOPosTop-15,markOPosLeft+15,markOPosTop+15);
            canvas.drawBitmap(btmMarkO,null,rectMarkO,pnt);

            //draw lineO
            pnt.setColor(Color.RED);
            pnt.setStrokeWidth(3);
            if(prevMarkOLeft != -1 && prevMarkOTop != -1) {
                canvas.drawLine(prevMarkOLeft, prevMarkOTop, markOPosLeft, markOPosTop, pnt);
            }
            prevMarkOLeft = markOPosLeft;
            prevMarkOTop = markOPosTop;


            //prepare image MarkX
            BitmapDrawable drawableMarkX;
            if(SDK_INT >= 21) {
                drawableMarkX = (BitmapDrawable)res.getDrawable(R.drawable.icon2, null);
            }
            else {
                drawableMarkX = (BitmapDrawable)res.getDrawable(R.drawable.icon2);
            }
//            BitmapDrawable drawableMarkX = (BitmapDrawable)res.getDrawable(R.drawable.icon2);
            Bitmap btmMarkX = drawableMarkX.getBitmap();

            //caculate position MarkX
            float markXPosLeft = outerRectMargin + verticalCap * (a+1);
            float markXPosTop = (float) ((outerRectMargin + dataX.get(a) * unitRatio) - unitRatio * (minLevel));

            //draw MarkX
            RectF rectMarkX = new RectF(markXPosLeft-15,markXPosTop-15,markXPosLeft+15,markXPosTop+15);
            canvas.drawBitmap(btmMarkX,null,rectMarkX,pnt);


            //draw lineX
            pnt.setStrokeWidth(3);
            pnt.setColor(Color.BLUE);
            if(prevMarkXLeft != -1 && prevMarkXTop != -1) {
                canvas.drawLine(prevMarkXLeft, prevMarkXTop, markXPosLeft, markXPosTop, pnt);
            }
            prevMarkXLeft = markXPosLeft;
            prevMarkXTop = markXPosTop;
        }

    }
}

