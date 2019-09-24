package com.changwon.wooogi.frequency;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainResult extends AppCompatActivity {


    public static int[] RealLeftValue = new int[100];
    public static int[] RealRightValue = new int[100];
    float PTA_R;
    float PTA_L;
    float MIb;
    float MIw;
    float HH;
    float textRight;
    float textLeft;
    int l_sextant;
    int r_sextant;
    String l_sextant_text;
    String r_sextant_text;

    public static int listnumber;   // 현재 리스트 넘버
    String date_month;    // 현재 월
    String date_day;    // 현재 일
    String date_hour;   // 현재 시간
    String date_minute;     // 현재 분

    Button test_end_btn;
    Button equalizer_btn;

    int r_sextant_length = 0;
    int r_sextant_text_length = 0;
    int l_sextant_length = 0;
    int l_sextant_text_length = 0;
    int textRight_length = 0;
    int textLeft_length = 0;
    int HH_length = 0;
    int now_text_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_result);



        RealLeftValue = MainTest.RealLeftValue;
        RealRightValue = MainTest.RealRightValue;


        l_sextant = Function.sextantfunction(RealLeftValue[2], RealLeftValue[3], RealLeftValue[3], RealLeftValue[4], RealLeftValue[4], RealLeftValue[6]);
        r_sextant = Function.sextantfunction(RealRightValue[2], RealRightValue[3], RealRightValue[3], RealRightValue[4], RealRightValue[4], RealRightValue[6]);

        l_sextant_text = Function.Resultdgreefunction(l_sextant);
        r_sextant_text = Function.Resultdgreefunction(r_sextant);

        PTA_R = Function.PTAfunction(RealRightValue[2], RealRightValue[3], RealRightValue[4],RealRightValue[5]);
        PTA_L = Function.PTAfunction(RealLeftValue[2],RealLeftValue[3], RealLeftValue[4], RealLeftValue[5]);
        if( Function.MIfunction(PTA_R) <= Function.MIfunction(PTA_L) ) {
            MIb = Function.MIfunction(PTA_R);
            MIw = Function.MIfunction(PTA_L);
            textRight = MIb;
            textLeft = MIw;

        }
        else {
            MIb = Function.MIfunction(PTA_L);
            MIw = Function.MIfunction(PTA_R);
            textRight = MIw;
            textLeft = MIb;
        }
        HH = Function.HHfunction(MIb, MIw);

        List<FrequencyVO> list = MainActivity.dao.list();
        listnumber = list.size();
        date_month = new SimpleDateFormat("MM").format(new Date());
        date_day = new SimpleDateFormat("dd").format(new Date());
        date_hour = new SimpleDateFormat("HH").format(new Date());
        date_minute = new SimpleDateFormat("mm").format(new Date());
        FrequencyVO frequencydate = new FrequencyVO(0,date_month + "월 " + date_day + "일",date_hour + "시 " + date_minute + "분", RealLeftValue[0], RealLeftValue[1],
                 RealLeftValue[2], RealLeftValue[3], RealLeftValue[4], RealLeftValue[5], RealLeftValue[6], RealLeftValue[7], RealLeftValue[8], RealRightValue[0], RealRightValue[1],
                RealRightValue[2], RealRightValue[3], RealRightValue[4], RealRightValue[5], RealRightValue[6], RealRightValue[7], RealRightValue[8],
                l_sextant, r_sextant, l_sextant_text, r_sextant_text, textLeft, textRight, HH);
        MainActivity.dao.insert(frequencydate);

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
            charView.dataO.add(new Double(RealRightValue[a]));
        }
        charView.dataX = new ArrayList<Double>();
        for(int a = 0; 9 > a; a++){
            charView.dataX.add(new Double(RealLeftValue[a]));
        }

        setContentView(R.layout.activity_main_result);

        TextView ResultText = (TextView)findViewById(R.id.resulttext);
        ResultText.setText("Calculating the average of the thresholds of 6 frequencies shows the following results: The hearing threshold of the right ear is "
                + r_sextant + "dB, a "+ r_sextant_text +" hearing threshold. The hearing threshold of the left ear is " + l_sextant + "dB, indicating a " + l_sextant_text + " hearing loss.\n" +
                "Calculating the percentage of the functional loss of hearing shows the following results: There is a " + textRight + "%, functional loss on the right hearing and a " +
                textLeft + "% functional loss on the left hearing. Overall percentage of the functional loss on both hearing is " + HH + "%.");

        if(r_sextant == -10) {
            r_sextant_length = 3;
        }
        else if(r_sextant < 0) {
            r_sextant_length = 2;
        }
        else if(r_sextant < 10) {
            r_sextant_length = 1;
        }
        else if(r_sextant < 100) {
            r_sextant_length = 2;
        }
        else {
            r_sextant_length = 3;
        }


        if(l_sextant == -10) {
            l_sextant_length = 3;
        }
        else if(l_sextant < 0) {
            l_sextant_length = 2;
        }
        else if(l_sextant < 10) {
            l_sextant_length = 1;
        }
        else if(l_sextant < 100) {
            l_sextant_length = 2;
        }
        else {
            l_sextant_length = 3;
        }

        if(r_sextant_text == "Normal") {
            r_sextant_text_length = 6;
        }
        else if(r_sextant_text == "Mild") {
            r_sextant_text_length = 4;
        }
        else if(r_sextant_text == "Moderate") {
            r_sextant_text_length = 8;
        }
        else if(r_sextant_text == "Moderate Severe") {
            r_sextant_text_length = 15;
        }
        else if(r_sextant_text == "Severe") {
            r_sextant_text_length = 6;
        }
        else {
            r_sextant_text_length = 4;
        }

        if(l_sextant_text == "Normal") {
            l_sextant_text_length = 6;
        }
        else if(l_sextant_text == "Mild") {
            l_sextant_text_length = 4;
        }
        else if(l_sextant_text == "Moderate") {
            l_sextant_text_length = 8;
        }
        else if(l_sextant_text == "Moderate Severe") {
            l_sextant_text_length = 15;
        }
        else if(l_sextant_text == "Severe") {
            l_sextant_text_length = 6;
        }
        else {
            l_sextant_text_length = 4;
        }

        if(textRight == -10.0) {
            textRight_length = 5;
        }
        else if(textRight < 0.0) {
            textRight_length = 4;
        }
        else if(textRight < 10.0) {
            textRight_length = 3;
        }
        else if(textRight < 100.0) {
            textRight_length = 4;
        }
        else {
            textRight_length = 5;
        }

        if(textLeft == -10.0) {
            textLeft_length = 5;
        }
        else if(textLeft < 0.0) {
            textLeft_length = 4;
        }
        else if(textLeft < 10.0) {
            textLeft_length = 3;
        }
        else if(textLeft < 100.0) {
            textLeft_length = 4;
        }
        else {
            textLeft_length = 5;
        }

        if(HH == -10.0) {
            HH_length = 5;
        }
        else if(HH < 0.0) {
            HH_length = 4;
        }
        else if(HH < 10.0) {
            HH_length = 3;
        }
        else if(HH < 100.0) {
            HH_length = 4;
        }
        else {
            HH_length = 5;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(ResultText.getText());
        builder.setSpan(new ForegroundColorSpan(Color.RED), 130, 130+r_sextant_length+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        now_text_position = 130 + r_sextant_length + 2 + 4;

        builder.setSpan(new ForegroundColorSpan(Color.RED), now_text_position, now_text_position+r_sextant_text_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        now_text_position = now_text_position + r_sextant_text_length + 61;

        builder.setSpan(new ForegroundColorSpan(Color.BLUE), now_text_position, now_text_position+l_sextant_length+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        now_text_position = now_text_position + l_sextant_length + 2 + 15;

        builder.setSpan(new ForegroundColorSpan(Color.BLUE), now_text_position, now_text_position+l_sextant_text_length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        now_text_position = now_text_position + l_sextant_text_length + 116;

        builder.setSpan(new ForegroundColorSpan(Color.RED), now_text_position, now_text_position+textRight_length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        now_text_position = now_text_position + textRight_length + 1 + 45;

        builder.setSpan(new ForegroundColorSpan(Color.BLUE), now_text_position, now_text_position+textLeft_length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        now_text_position = now_text_position + textLeft_length + 1 + 99;

        builder.setSpan(new StyleSpan(Typeface.BOLD),now_text_position,now_text_position + HH_length+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        builder.setSpan(new ForegroundColorSpan(Color.RED),22,28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.RED),30,34, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.BLUE),44,49, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.BLUE),51,55, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new StyleSpan(Typeface.BOLD),57,64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        builder.setSpan(new ForegroundColorSpan(Color.RED),73,83, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.RED),85,90, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.BLUE),92,100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.BLUE),102,107, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new StyleSpan(Typeface.BOLD),111,120, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new StyleSpan(Typeface.BOLD),122,128, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ResultText.setText(builder);

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.wrap_char_view1);

        RelativeLayout.LayoutParams chartViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        charView.setLayoutParams(chartViewLayoutParams);
        rootLayout.addView(charView);

        test_end_btn = (Button)findViewById(R.id.main_result_end_btn);
        equalizer_btn = (Button)findViewById(R.id.main_result_equalizer_btn);

        test_end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainResult.this, "hello", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainResult.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        equalizer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainResult.this, Equalizer.class);
                intent.putExtra("Value_key", 0);
                startActivity(intent);
                finish();

            }
        });

    }
}
