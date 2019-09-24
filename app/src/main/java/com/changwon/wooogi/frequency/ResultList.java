package com.changwon.wooogi.frequency;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultList extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    TextView listdate;
    String date_text;
    String time_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        listView = (ListView) findViewById(R.id.listview);
        listAdapter = new ListAdapter(getApplicationContext());
        listView.setAdapter(listAdapter);


    }

    public class ListAdapter extends BaseAdapter {

        private ArrayList<Address> arrayList;
        private ViewHolder viewHolder;
        Context context;

        public ListAdapter(Context context) {

            this.context = context;
            arrayList = new ArrayList<Address>();

            Address temp;

            try {
                List<FrequencyVO> list = MainActivity.dao.list();
                for (int i = 0; i < list.size(); i++) {
                    String num = list.get(i).num + "";
                    String date = list.get(i).date + "";
                    String time = list.get(i).time + "";
                    String l_sextant = list.get(i).l_sextant + "";
                    String r_sextant = list.get(i).r_sextant + "";
                    String loss_ratio = list.get(i).loss_ratio + "";
                    temp = new Address(num, date, time, l_sextant, r_sextant, loss_ratio);
                    arrayList.add(temp);


                }
            }
            catch (Exception e)
            {
                Toast.makeText(ResultList.this, "서버 오류입니다.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return arrayList.indexOf(getItem(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = lInflater.inflate(R.layout.listview, null);
            }
            viewHolder = new ViewHolder();
//            viewHolder.number = (TextView) convertView.findViewById(R.id.number);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.l_sextant = (TextView) convertView.findViewById(R.id.listlefttext);
            viewHolder.r_sextant = (TextView) convertView.findViewById(R.id.listrighttext);
            viewHolder.loss_ratio = (TextView) convertView.findViewById(R.id.loss_ratio);
            viewHolder.deletebutton = (Button) convertView.findViewById(R.id.deletebutton);

            final String number = arrayList.get(position).getNum();
            final String date = arrayList.get(position).getdate();
            final String time = arrayList.get(position).gettime();
            final String l_sextant = arrayList.get(position).getl_sextant();
            final String r_sextant = arrayList.get(position).getr_sextant();
            final String loss_ratio = arrayList.get(position).getloss_ratio();

//            viewHolder.number.setText(number);

            date_text = String.valueOf(date);
            String[] date_month_separated = date_text.split("월");
            String[] date_day_separated = date_month_separated[1].split("일");

            viewHolder.date.setText("19. "+date_month_separated[0] + "." + date_day_separated[0]);

            time_text = String.valueOf(time);
            String[] time_hours_separated = time_text.split("시");
            String[] time_minutes_separated = time_hours_separated[1].split("분");

            viewHolder.time.setText(time_hours_separated[0] + "." + time_minutes_separated[0]);
            viewHolder.l_sextant.setText("Lt. " + l_sextant);
            viewHolder.r_sextant.setText("Rt. " + r_sextant);
            viewHolder.loss_ratio.setText(loss_ratio+"%");
            viewHolder.deletebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainActivity.dao.delete(Integer.parseInt(number));
                    Intent intent=new Intent(ResultList.this,ResultList.class);
                    startActivity(intent);
                    finish();
                }
            });

            final String listnumbers = Integer.toString(position);
            viewHolder.date.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ListResult.class);
                    intent.putExtra("position", listnumbers);
                    startActivity(intent);

                }
            });

            viewHolder.time.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ListResult.class);
                    intent.putExtra("position", listnumbers);
                    startActivity(intent);

                }
            });

            viewHolder.l_sextant.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ListResult.class);
                    intent.putExtra("position", listnumbers);
                    startActivity(intent);

                }
            });

            viewHolder.r_sextant.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ListResult.class);
                    intent.putExtra("position", listnumbers);
                    startActivity(intent);

                }
            });

            viewHolder.loss_ratio.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ListResult.class);
                    intent.putExtra("position", listnumbers);
                    startActivity(intent);

                }
            });

            return convertView;
        }
    }


    class ViewHolder {
        public TextView number;
        public TextView date;
        public TextView time;
        public TextView l_sextant;
        public TextView r_sextant;
        public TextView loss_ratio;
        public Button deletebutton;

    }

    class Address {
        String num;
        String date;
        String time;
        String l_sextant;
        String r_sextant;
        String loss_ratio;

        public Address(String num, String date, String time, String l_sextant,
                       String r_sextant, String loss_ratio) {
            this.num = num;
            this.date = date;
            this.time = time;
            this.l_sextant = l_sextant;
            this.r_sextant = r_sextant;
            this.loss_ratio = loss_ratio;
        }

        public String getNum() {
            return num;
        }

        public String getdate() {
            return date;
        }

        public String gettime() {
            return time;
        }

        public String getl_sextant() {
            return l_sextant;
        }

        public String getr_sextant() {
            return r_sextant;
        }

        public String getloss_ratio() {
            return loss_ratio;
        }
    }



//    textView.setOnClickListener(new View.OnClickListener() {
//        public void onClick(View v) {
//
//            Intent intent = new Intent(getApplicationContext(), Contents.class);
//            intent.putExtra("content", content);
//            startActivity(intent);
//            finish();
//
//        }
//    });
}