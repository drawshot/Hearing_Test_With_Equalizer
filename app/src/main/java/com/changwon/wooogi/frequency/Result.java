package com.changwon.wooogi.frequency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    Intent getMain;
    int[] RealLeftValue = new int[100];
    int[] RealRightValue = new int[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getMain = getIntent();
        RealLeftValue = getMain.getBundleExtra("RealLeftValue").getIntArray("LeftBun");
        RealRightValue =  getMain.getBundleExtra("RealRightValue").getIntArray("RightBun");

        ChartView charView = new ChartView(this);
        charView.outerRectMargin = 80;

        charView.innerVerticalTickCount = 9;
        charView.topLabeles = new ArrayList<String>();
        charView.topLabeles.add("125");
        charView.topLabeles.add("250");
        charView.topLabeles.add("500");
        charView.topLabeles.add("1000");
//        charView.topLabeles.add("1000");
        charView.topLabeles.add("2000");
        charView.topLabeles.add("3000");
        charView.topLabeles.add("4000");
        charView.topLabeles.add("6000");
        charView.topLabeles.add("8000");
        charView.strTopUnit = "Hz";

        charView.innerHorizontalTickCount = 13;
        charView.leftLabeles = new ArrayList<String>();
        charView.leftLabeles.add("-10");
        charView.leftLabeles.add("0");
        charView.leftLabeles.add("10");
        charView.leftLabeles.add("20");
        charView.leftLabeles.add("30");
        charView.leftLabeles.add("40");
        charView.leftLabeles.add("50");
        charView.leftLabeles.add("60");
        charView.leftLabeles.add("70");
        charView.leftLabeles.add("80");
        charView.leftLabeles.add("90");
        charView.leftLabeles.add("100");
        charView.leftLabeles.add("110");
        charView.strLeftUnit = "dB";

        charView.maxLevel = 110;
        charView.minLevel = -10;

        charView.dataO = new ArrayList<Double>();
        for(int a = 0; 9 > a; a++){ //6은 데이터갯수
            charView.dataO.add(new Double(RealLeftValue[a]));
        }
        charView.dataX = new ArrayList<Double>();
        for(int a = 0; 9 > a; a++){
            charView.dataX.add(new Double(RealRightValue[a]));
        }

        setContentView(R.layout.activity_result);
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.wrap_char_view);

        RelativeLayout.LayoutParams chartViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        charView.setLayoutParams(chartViewLayoutParams);
        rootLayout.addView(charView);


    }

}