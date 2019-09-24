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

import java.util.ArrayList;
import java.util.List;

public class ListResult extends AppCompatActivity {

    public static int[] ListRealLeftValue = new int[100];
    public static int[] ListRealRightValue = new int[100];
    float HH;
    float textRight;
    float textLeft;
    int l_sextant;
    int r_sextant;
    boolean list_left_decibel = false;
    boolean list_right_decibel = false;
    boolean list_left_ratio = false;
    boolean list_right_ratio = false;
    int listposition;

    String l_sextant_text;
    String r_sextant_text;

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
        setContentView(R.layout.activity_list_result);

        Intent intent = getIntent();
        listposition = Integer.parseInt(intent.getStringExtra("position"));

//        Toast.makeText(this, "listposition = " + listposition, Toast.LENGTH_SHORT).show();

        List<FrequencyVO> list = MainActivity.dao.list();
        ListRealLeftValue[0] = list.get(listposition).l_a125;
        ListRealLeftValue[1] = list.get(listposition).l_a250;
        ListRealLeftValue[2] = list.get(listposition).l_a500;
        ListRealLeftValue[3] = list.get(listposition).l_a1000;
        ListRealLeftValue[4] = list.get(listposition).l_a2000;
        ListRealLeftValue[5] = list.get(listposition).l_a3000;
        ListRealLeftValue[6] = list.get(listposition).l_a4000;
        ListRealLeftValue[7] = list.get(listposition).l_a6000;
        ListRealLeftValue[8] = list.get(listposition).l_a8000;

        ListRealRightValue[0] = list.get(listposition).r_a125;
        ListRealRightValue[1] = list.get(listposition).r_a250;
        ListRealRightValue[2] = list.get(listposition).r_a500;
        ListRealRightValue[3] = list.get(listposition).r_a1000;
        ListRealRightValue[4] = list.get(listposition).r_a2000;
        ListRealRightValue[5] = list.get(listposition).r_a3000;
        ListRealRightValue[6] = list.get(listposition).r_a4000;
        ListRealRightValue[7] = list.get(listposition).r_a6000;
        ListRealRightValue[8] = list.get(listposition).r_a8000;

//        ListRealLeftValue[0] = 60;
//        ListRealLeftValue[1] = 70;
//        ListRealLeftValue[2] = 65;
//        ListRealLeftValue[3] = 75;
//        ListRealLeftValue[4] = 30;
//        ListRealLeftValue[5] = 20;
//        ListRealLeftValue[6] = 15;
//        ListRealLeftValue[7] = 10;
//        ListRealLeftValue[8] = 20;
//
//        ListRealRightValue[0] = 85;
//        ListRealRightValue[1] = 90;
//        ListRealRightValue[2] = 85;
//        ListRealRightValue[3] = 0;
//        ListRealRightValue[4] = 0;
//        ListRealRightValue[5] = 0;
//        ListRealRightValue[6] = 0;
//        ListRealRightValue[7] = 0;
//        ListRealRightValue[8] = 0;

//        ListRealLeftValue[0] = 5;
//        ListRealLeftValue[1] = 0;
//        ListRealLeftValue[2] = 10;
//        ListRealLeftValue[3] = 5;
//        ListRealLeftValue[4] = 75;
//        ListRealLeftValue[5] = 90;
//        ListRealLeftValue[6] = 85;
//        ListRealLeftValue[7] = 95;
//        ListRealLeftValue[8] = 80;
//
//        ListRealRightValue[0] = 0;
//        ListRealRightValue[1] = 0;
//        ListRealRightValue[2] = 0;
//        ListRealRightValue[3] = 0;
//        ListRealRightValue[4] = 90;
//        ListRealRightValue[5] = 85;
//        ListRealRightValue[6] = 95;
//        ListRealRightValue[7] = 80;
//        ListRealRightValue[8] = 90;



        l_sextant = (int)list.get(listposition).l_sextant;
        r_sextant = (int)list.get(listposition).r_sextant;

        l_sextant_text = Function.Resultdgreefunction(l_sextant);
        r_sextant_text = Function.Resultdgreefunction(r_sextant);

        textLeft = list.get(listposition).l_loss_ratio;
        textRight = list.get(listposition).r_loss_ratio;
        HH = list.get(listposition).loss_ratio;


//
//        final String number = arrayList.get(position).getNum();
//        final String date = arrayList.get(position).getdate();
//        final String time = arrayList.get(position).gettime();
//        final String l_sextant = arrayList.get(position).getl_sextant();
//        final String r_sextant = arrayList.get(position).getr_sextant();
//        final String loss_ratio = arrayList.get(position).getloss_ratio();

//        Toast.makeText(this, "listposition = " + listposition, Toast.LENGTH_SHORT).show();


//        PTA_R = ( (ListRealRightValue[1] + ListRealRightValue[2] + ListRealRightValue[3] + ListRealRightValue[4]) / 4 );
//        PTA_L = ( (ListRealLeftValue[1] + ListRealLeftValue[2] + ListRealLeftValue[3] + ListRealLeftValue[4]) / 4 );
//        if( (PTA_R-25)*1.5 <= (PTA_L-25)*1.5 ) {
//            MIb = (PTA_R-25)*1.5f;
//            MIw = (PTA_L-25)*1.5f;
//            textRight = MIb;
//            textLeft = MIw;
//        }
//        else {
//            MIb = (PTA_L-25)*1.5f;
//            MIw = (PTA_R-25)*1.5f;
//            textRight = MIw;
//            textLeft = MIb;
//        }
//        HH = (5*MIb + MIw) / 6;



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
        for(int a = 0; 9 > a; a++){ //8은 데이터갯수
            charView.dataO.add(new Double(ListRealRightValue[a]));
        }
        charView.dataX = new ArrayList<Double>();
        for(int a = 0; 9 > a; a++){
            charView.dataX.add(new Double(ListRealLeftValue[a]));
        }

        setContentView(R.layout.activity_list_result);

        TextView ResultText = (TextView)findViewById(R.id.listresulttext);

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

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.list_wrap_char_view);

        RelativeLayout.LayoutParams chartViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        charView.setLayoutParams(chartViewLayoutParams);
        rootLayout.addView(charView);


        Button equalizer_button = (Button) findViewById(R.id.list_result_equalizer_btn);
        Button end_button = (Button)findViewById(R.id.list_result_end_btn);

        equalizer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ListResult.this, Equalizer.class);
//                startActivity(intent);

                final String listnumbers_equalizer = Integer.toString(listposition);
                Intent intent = new Intent(getApplicationContext(), Equalizer.class);
                intent.putExtra("Value_key", 2);
                intent.putExtra("position_equalizer", listnumbers_equalizer);
                startActivity(intent);
            }
        });

        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListResult.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }
}
