package com.changwon.wooogi.frequency;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TestResult extends AppCompatActivity {

    public static int[] aRealLeftValue = new int[100];
    public static int[] aRealRightValue = new int[100];
    float PTA_R;
    float PTA_L;
    float MIb;
    float MIw;
    float HH;
    float textRight;
    float textLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);



        //        Button resultbutton1 = (Button)findViewById(R.id.resultbtn1);
//        Button resultbutton2 = (Button)findViewById(R.id.resultbtn2);




//        resultbutton1.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//
//                Intent intent = new Intent(ResultTest.this,ResultMasking.class);
//                startActivity(intent);
//
//            }
//        });
//
//        resultbutton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ResultTest.this, Result.class);
//                startActivity(intent);
//            }
//        });



        aRealLeftValue[0] = 70;
        aRealLeftValue[1] = 25;
        aRealLeftValue[2] = 50;
        aRealLeftValue[3] = 70;
        aRealLeftValue[4] = 50;
        aRealLeftValue[5] = 30;
        aRealLeftValue[6] = 0;
        aRealLeftValue[7] = 10;

        aRealRightValue[0] = 40;
        aRealRightValue[1] = 35;
        aRealRightValue[2] = 30;
        aRealRightValue[3] = 50;
        aRealRightValue[4] = 30;
        aRealRightValue[5] = 20;
        aRealRightValue[6] = 100;
        aRealRightValue[7] = 70;


        PTA_R = ( (aRealRightValue[1] + aRealRightValue[2] + aRealRightValue[3] + aRealRightValue[4]) / 4 );
        PTA_L = ( (aRealLeftValue[1] + aRealLeftValue[2] + aRealLeftValue[3] + aRealLeftValue[4]) / 4 );
        if( (PTA_R-25)*1.5 <= (PTA_L-25)*1.5 ) {
            MIb = (PTA_R-25)*1.5f;
            MIw = (PTA_L-25)*1.5f;
            textRight = MIb;
            textLeft = MIw;
        }
        else {
            MIb = (PTA_L-25)*1.5f;
            MIw = (PTA_R-25)*1.5f;
            textRight = MIw;
            textLeft = MIb;
        }
        HH = (5*MIb + MIw) / 6;



        ChartView charView = new ChartView(this);
        charView.outerRectMargin = 80;

        charView.innerVerticalTickCount = 8;
        charView.topLabeles = new ArrayList<String>();
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
        for(int a = 0; 8 > a; a++){ //6은 데이터갯수
            charView.dataO.add(new Double(aRealLeftValue[a]));
        }
        charView.dataX = new ArrayList<Double>();
        for(int a = 0; 8 > a; a++){
            charView.dataX.add(new Double(aRealRightValue[a]));
        }

        setContentView(R.layout.activity_test_result);

        TextView ResultText = (TextView)findViewById(R.id.aa);
        ResultText.setText("당신의 청력은 보건복지부 기준 6분법상 오른쪽 청력은 23dB로 정상 상태이며 왼쪽 청력은 29dB로 경도난청 상태입니다.\n\n" +
                "당신의 오른쪽 청력 손실율은 " + textRight + "%, 왼쪽 청력손실율은 " + textLeft + "% 이며 전체 청력 손실율은 " + HH + "% 입니다.");

        SpannableStringBuilder builder = new SpannableStringBuilder(ResultText.getText());
        builder.setSpan(new ForegroundColorSpan(Color.RED),22,28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.RED),30,34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLUE),44,49, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLUE),51,55, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD),57,64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setSpan(new ForegroundColorSpan(Color.RED),74,84, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.RED),86,91, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLUE),93,101, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.BLUE),103,108, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD),112,121, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD),122,128, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ResultText.setText(builder);

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.wrap_char_view2);

        RelativeLayout.LayoutParams chartViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        charView.setLayoutParams(chartViewLayoutParams);
        rootLayout.addView(charView);





    }
//    public void onClick(View v) {
//        switch(v.getId()) {
//            case R.id.resultbtn1:
//                Intent intent1 = new Intent(ResultTest.this, MainMasking.class);
//                startActivity(intent1);
//                break;
//            case R.id.resultbtn2:
//                Intent intent2 = new Intent(ResultTest.this, MainResult.class);
//                startActivity(intent2);
//                break;
//        }
//    }
}