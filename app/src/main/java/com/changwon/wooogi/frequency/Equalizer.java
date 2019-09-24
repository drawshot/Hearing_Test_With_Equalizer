package com.changwon.wooogi.frequency;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;


public class Equalizer extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Context context;
    private static final int PERMISSION_REQUEST_CODE = 1;
    String p1 = android.Manifest.permission.BLUETOOTH, p2 = android.Manifest.permission.BLUETOOTH_ADMIN,
            p3 = android.Manifest.permission.ACCESS_COARSE_LOCATION, p4 = android.Manifest.permission.ACCESS_FINE_LOCATION;

    public static int[] equalizer_LeftValue = new int[100];
    public static int[] equalizer_RightValue = new int[100];
    int[] position_list_l = new int[20];
    int[] position_list_r = new int[20];
    private BluetoothManager managerA = null;
    private BluetoothAdapter adapterA = null;
    private BluetoothDevice bDevice;

    private BluetoothGattDescriptor descriptorA = null;
    private BluetoothGatt gattA = null;
    private BluetoothGattCharacteristic charact = null;

    Button btn = null;
    BluetoothGattCallback callbackA = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (BluetoothGatt.STATE_CONNECTED == newState) {
                Log.d("Safaud", "연결됨");
//                input_button.setEnabled(true);
//                Toast.makeText(context, "연결됨", Toast.LENGTH_SHORT).show();
                gatt.discoverServices();
            } else {
                Log.d("Safaud", "끊어짐");
                Handler han = new Handler(Looper.getMainLooper());
                han.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        input_button.setEnabled(false);
                    }
                }, 100);

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            gattA = gatt;
            Log.d("Safaud", "null");
            List<BluetoothGattService> services = gatt.getServices();
            for(BluetoothGattService sv: services) {
                Log.d("Safaud", "ServiceUUID : "+sv.getUuid().toString());
                if(sv.getUuid().toString().equalsIgnoreCase("00001523-1212-efde-1523-785feabcd123")) {
                    List<BluetoothGattCharacteristic> chras = sv.getCharacteristics();
                    for(BluetoothGattCharacteristic ch:chras) {
                        Log.d("Safaud", "CHARUUID : "+ch.getUuid());
                        if(ch.getUuid().toString().equalsIgnoreCase("00001524-1212-efde-1523-785feabcd123")) {
                            Log.d("Safaud", "DescSize : "+ ch.getDescriptors().size());
                            gatt.setCharacteristicNotification(ch, true);
//                            descriptorA = ch.getDescriptors().get(1);
                            charact = ch;


                            Handler han = new Handler(Looper.getMainLooper());
                            han.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Equalizer.this, "equalizer board conneted", Toast.LENGTH_SHORT).show();
                                    input_button.setEnabled(true);
                                }
                            }, 100);

//                            sendData(byteArray);
                        }
                    }
                }
            }
        }



        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] axisData = characteristic.getValue();
            Log.d("Safaud", "Data Recieved");
            Log.d("Safaud", axisData.toString());
        }

    };

    public void setNotification(View view) {
        Log.d("Safaud", "SetNoti");
        if(descriptorA != null){
            Log.d("Safaud", "descriptor not null");
            descriptorA.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            gattA.writeDescriptor(descriptorA);
        }
    }

    public void scanDevice() {
        Log.d("Safaud", "Scan Start");

        // 롤리팝 미만 버젼.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            final BluetoothAdapter.LeScanCallback leCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    if(device.getName() != null) {
                        Log.d("Safaud", "RSSI : " + rssi +"  onLeScan : " + device.getName());
                    }
                }
            };
            this.adapterA.startLeScan(leCallback);

            Handler han = new Handler();
            han.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapterA.stopLeScan(leCallback);
                }
            }, 2000);


            // 롤리팝 이상 버젼.
        } else {
            final ScanCallback scanCallback = new ScanCallback() {
                @Override

                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    Log.d("Safaud", "MacAddr :" + result.getDevice().getAddress());
                    Log.d("Safaud", "");
                    Log.d("Safaud", "========================================================");
                    Log.d("Safaud", "MacAddr :" + result.getDevice().getAddress());
                    Log.d("Safaud", "RSSI : " + result.getRssi());
                    Log.d("Safaud", "========================================================");
                    Log.d("Safaud", "");

                    if (result.getDevice().getAddress() != null) {
                        //1, 2, 6, 4, 5
                        if (result.getDevice().getAddress().equals("D9:26:EA:7E:60:48") || result.getDevice().getAddress().equals("E9:BA:AC:BF:03:B7")
                                || result.getDevice().getAddress().equals("DC:B3:6F:9E:A7:87") || result.getDevice().getAddress().equals("F8:9C:AC:0B:0E:4D")
                                || result.getDevice().getAddress().equals("E1:A5:E8:9C:5A:25") || result.getDevice().getAddress().equals("E2:0B:3A:01:A6:7C")
                                || result.getDevice().getAddress().equals("F9:24:71:34:56:71"))
                        { //우남
                            bDevice = result.getDevice();
                            adapterA.getBluetoothLeScanner().stopScan(this);
                            connect1(this);
                        }
                    }
                }
            };
            ScanSettings st = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
            this.adapterA.getBluetoothLeScanner().startScan(null, st, scanCallback);
            Handler han = new Handler();
            han.postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapterA.getBluetoothLeScanner().stopScan(scanCallback);
                }
            }, 5000);
        }

    }



    public void connect1(ScanCallback scanCallback) {
//        adapterA.getBluetoothLeScanner().stopScan(scanCallback);
        if(bDevice != null) {
            bDevice.connectGatt(Equalizer.this, false, callbackA);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void connect(View view) {
        if(bDevice != null) {
            bDevice.connectGatt(Equalizer.this, false, callbackA);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void sendData(byte[] byteArray) {
        Log.d("Safaud", "sendData");
        if (charact == null) {

            Log.d("Safaud", "charact is null");
            return;
        }
//        Log.d("safaud array : ", ""+byteArray);
        charact.setValue(byteArray);
//        Log.d("Safaud", String.valueOf(charact.getValue()));

        Log.d("Safaud", "byteArray.length is " + byteArray.length);

//        for (int i = 0 ; i < 12 ; i++){
//            Log.d("Safaud ", "hello");
//        }

        for (int i = 0 ; i < byteArray.length ; i++) {
            Log.d("Safaud11", String.valueOf((char)charact.getValue()[i]));
        }
        boolean status1 = gattA.writeCharacteristic(charact);
        if(status1){
            Log.d("Safaud", "Success");
        }else{
            Log.d("Safaud", "Fail");
        }
    }

//    public void send(View view){
//        sendData(byteArray);
//    }


    private Spinner spinner_l_125 = null;
    private Spinner spinner_l_250 = null;
    private Spinner spinner_l_500 = null;
    private Spinner spinner_l_1000 = null;
    private Spinner spinner_l_2000 = null;
    private Spinner spinner_l_3000 = null;
    private Spinner spinner_l_4000 = null;
    private Spinner spinner_l_6000 = null;
    private Spinner spinner_l_8000 = null;

    private Spinner spinner_r_125 = null;
    private Spinner spinner_r_250 = null;
    private Spinner spinner_r_500 = null;
    private Spinner spinner_r_1000 = null;
    private Spinner spinner_r_2000 = null;
    private Spinner spinner_r_3000 = null;
    private Spinner spinner_r_4000 = null;
    private Spinner spinner_r_6000 = null;
    private Spinner spinner_r_8000 = null;

    Button save_button = null;
    Button input_button = null;
    Button reset_button = null;


    int[] decibel_list = {-10, -5, 0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110};
    int[] left_value = new int[20];
    int[] right_value = new int[20];
    int[] save_position = {8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};   //초기화
    int[] load_position = {8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};   //초기화
    int l_average = 0; // 왼쪽 6분법 평균
    int r_average = 0; // 오른쪽 6분법 평균
    int[] left_frequency_band = new int[12];    // 주파수 밴드에 맞게 값 다시 저장 변수(12개 밴드)
    int[] right_frequency_band = new int[12];   // 주파수 밴드에 맞게 값 다시 저장 변수(12개 밴드)
    int[] left_gain = new int[12];  //왼쪽 gain 값
    int[] right_gain = new int[12]; //오른쪽 gain 값

    byte[] left_gain_byte = new byte[12];   // 블루투스로 전송할 왼쪽 주파수 byte 값 저장 변수
    byte[] right_gain_byte = new byte[12];  // 블루투스로 전송할 오른쪽 주파수 byte 값 저장 변수

    private ArrayAdapter<String> SpinnerAdapter = null;

    int[] result_left_value = new int[100];
    int[] result_right_value = new int[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        context = this;

        permissionAccess();

        this.managerA = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        this.adapterA = managerA.getAdapter();

//        // 이전 데이터 로드
//        SharedPreferences sharedPref = Equalizer.this.getPreferences(Context.MODE_PRIVATE);
//
//        // 왼쪽 주파수 로드
//        load_position[0] = sharedPref.getInt("a_l_125", save_position[0]);
//        load_position[1] = sharedPref.getInt("a_l_250", save_position[1]);
//        load_position[2] = sharedPref.getInt("a_l_500", save_position[2]);
//        load_position[3] = sharedPref.getInt("a_l_1000", save_position[3]);
//        load_position[4] = sharedPref.getInt("a_l_2000", save_position[4]);
//        load_position[5] = sharedPref.getInt("a_l_3000", save_position[5]);
//        load_position[6] = sharedPref.getInt("a_l_4000", save_position[6]);
//        load_position[7] = sharedPref.getInt("a_l_6000", save_position[7]);
//        load_position[8] = sharedPref.getInt("a_l_8000", save_position[8]);
//
//        // 오른쪽 주파수 로드
//        load_position[9] = sharedPref.getInt("a_r_125", save_position[9]);
//        load_position[10] = sharedPref.getInt("a_r_250", save_position[10]);
//        load_position[11] = sharedPref.getInt("a_r_500", save_position[11]);
//        load_position[12] = sharedPref.getInt("a_r_1000", save_position[12]);
//        load_position[13] = sharedPref.getInt("a_r_2000", save_position[13]);
//        load_position[14] = sharedPref.getInt("a_r_3000", save_position[14]);
//        load_position[15] = sharedPref.getInt("a_r_4000", save_position[15]);
//        load_position[16] = sharedPref.getInt("a_r_6000", save_position[16]);
//        load_position[17] = sharedPref.getInt("a_r_8000", save_position[17]);

        Intent intent = getIntent();
        int value_key = intent.getIntExtra("Value_key", -1);

        if(value_key == 0) {
            equalizer_LeftValue = MainTest.RealLeftValue;
            equalizer_RightValue = MainTest.RealRightValue;
        }

        if(value_key == 1) {
//            result_left_value = intent.getIntArrayExtra("MenualRealLeftValue");
//            result_right_value = intent.getIntArrayExtra("MenualRealRightValue");

//            equalizer_LeftValue = intent.getIntArrayExtra("MenualRealLeftValue");
//            equalizer_RightValue = intent.getIntArrayExtra("MenualRealRightValue");
            equalizer_LeftValue = MenualTest.menualrealleftvalue;
            equalizer_RightValue = MenualTest.menualrealrightvalue;

        }

        if(value_key == 2) {
            int listposition = Integer.parseInt(intent.getStringExtra("position_equalizer"));

            List<FrequencyVO> list = MainActivity.dao.list();
            equalizer_LeftValue[0] = list.get(listposition).l_a125;
            equalizer_LeftValue[1] = list.get(listposition).l_a250;
            equalizer_LeftValue[2] = list.get(listposition).l_a500;
            equalizer_LeftValue[3] = list.get(listposition).l_a1000;
            equalizer_LeftValue[4] = list.get(listposition).l_a2000;
            equalizer_LeftValue[5] = list.get(listposition).l_a3000;
            equalizer_LeftValue[6] = list.get(listposition).l_a4000;
            equalizer_LeftValue[7] = list.get(listposition).l_a6000;
            equalizer_LeftValue[8] = list.get(listposition).l_a8000;

            equalizer_RightValue[0] = list.get(listposition).r_a125;
            equalizer_RightValue[1] = list.get(listposition).r_a250;
            equalizer_RightValue[2] = list.get(listposition).r_a500;
            equalizer_RightValue[3] = list.get(listposition).r_a1000;
            equalizer_RightValue[4] = list.get(listposition).r_a2000;
            equalizer_RightValue[5] = list.get(listposition).r_a3000;
            equalizer_RightValue[6] = list.get(listposition).r_a4000;
            equalizer_RightValue[7] = list.get(listposition).r_a6000;
            equalizer_RightValue[8] = list.get(listposition).r_a8000;
        }





//        equalizer_LeftValue[0] = 60;
//        equalizer_LeftValue[1] = 70;
//        equalizer_LeftValue[2] = 65;
//        equalizer_LeftValue[3] = 75;
//        equalizer_LeftValue[4] = 30;
//        equalizer_LeftValue[5] = 20;
//        equalizer_LeftValue[6] = 15;
//        equalizer_LeftValue[7] = 10;
//        equalizer_LeftValue[8] = 20;
//
//        equalizer_RightValue[0] = 85;
//        equalizer_RightValue[1] = 90;
//        equalizer_RightValue[2] = 85;
//        equalizer_RightValue[3] = 0;
//        equalizer_RightValue[4] = 0;
//        equalizer_RightValue[5] = 0;
//        equalizer_RightValue[6] = 0;
//        equalizer_RightValue[7] = 0;
//        equalizer_RightValue[8] = 0;

//        equalizer_LeftValue[1] = 0;
//        equalizer_LeftValue[2] = 10;
//        equalizer_LeftValue[3] = 5;
//        equalizer_LeftValue[4] = 75;
//        equalizer_LeftValue[5] = 90;
//        equalizer_LeftValue[6] = 85;
//        equalizer_LeftValue[7] = 95;
//        equalizer_LeftValue[8] = 80;
//
//        equalizer_RightValue[0] = 0;
//        equalizer_RightValue[1] = 0;
//        equalizer_RightValue[2] = 0;
//        equalizer_RightValue[3] = 0;
//        equalizer_RightValue[4] = 90;
//        equalizer_RightValue[5] = 85;
//        equalizer_RightValue[6] = 95;
//        equalizer_RightValue[7] = 80;
//        equalizer_RightValue[8] = 90;


        position_list_l[0] = Function.SpinnerConverter(equalizer_LeftValue[0]);
        position_list_l[1] = Function.SpinnerConverter(equalizer_LeftValue[1]);
        position_list_l[2] = Function.SpinnerConverter(equalizer_LeftValue[2]);
        position_list_l[3] = Function.SpinnerConverter(equalizer_LeftValue[3]);
        position_list_l[4] = Function.SpinnerConverter(equalizer_LeftValue[4]);
        position_list_l[5] = Function.SpinnerConverter(equalizer_LeftValue[5]);
        position_list_l[6] = Function.SpinnerConverter(equalizer_LeftValue[6]);
        position_list_l[7] = Function.SpinnerConverter(equalizer_LeftValue[7]);
        position_list_l[8] = Function.SpinnerConverter(equalizer_LeftValue[8]);

        position_list_r[0] = Function.SpinnerConverter(equalizer_RightValue[0]);
        position_list_r[1] = Function.SpinnerConverter(equalizer_RightValue[1]);
        position_list_r[2] = Function.SpinnerConverter(equalizer_RightValue[2]);
        position_list_r[3] = Function.SpinnerConverter(equalizer_RightValue[3]);
        position_list_r[4] = Function.SpinnerConverter(equalizer_RightValue[4]);
        position_list_r[5] = Function.SpinnerConverter(equalizer_RightValue[5]);
        position_list_r[6] = Function.SpinnerConverter(equalizer_RightValue[6]);
        position_list_r[7] = Function.SpinnerConverter(equalizer_RightValue[7]);
        position_list_r[8] = Function.SpinnerConverter(equalizer_RightValue[8]);

//        Toast.makeText(this, "Value = " + equalizer_RightValue[8], Toast.LENGTH_SHORT).show();


        spinner_l_125 = (Spinner)findViewById(R.id.l_125);
        spinner_l_250 = (Spinner)findViewById(R.id.l_250);
        spinner_l_500 = (Spinner)findViewById(R.id.l_500);
        spinner_l_1000 = (Spinner)findViewById(R.id.l_1000);
        spinner_l_2000 = (Spinner)findViewById(R.id.l_2000);
        spinner_l_3000 = (Spinner)findViewById(R.id.l_3000);
        spinner_l_4000 = (Spinner)findViewById(R.id.l_4000);
        spinner_l_6000 = (Spinner)findViewById(R.id.l_6000);
        spinner_l_8000 = (Spinner)findViewById(R.id.l_8000);

        spinner_r_125 = (Spinner)findViewById(R.id.r_125);
        spinner_r_250 = (Spinner)findViewById(R.id.r_250);
        spinner_r_500 = (Spinner)findViewById(R.id.r_500);
        spinner_r_1000 = (Spinner)findViewById(R.id.r_1000);
        spinner_r_2000 = (Spinner)findViewById(R.id.r_2000);
        spinner_r_3000 = (Spinner)findViewById(R.id.r_3000);
        spinner_r_4000 = (Spinner)findViewById(R.id.r_4000);
        spinner_r_6000 = (Spinner)findViewById(R.id.r_6000);
        spinner_r_8000 = (Spinner)findViewById(R.id.r_8000);

        save_button = (Button)findViewById(R.id.save_button);
        input_button = (Button)findViewById(R.id.input_button);
        input_button.setEnabled(false);

        reset_button = (Button)findViewById(R.id.reset_button);

        SpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, (String[])getResources().getStringArray(R.array.array_list));
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_l_125.setAdapter(SpinnerAdapter);
        spinner_l_250.setAdapter(SpinnerAdapter);
        spinner_l_500.setAdapter(SpinnerAdapter);
        spinner_l_1000.setAdapter(SpinnerAdapter);
        spinner_l_2000.setAdapter(SpinnerAdapter);
        spinner_l_3000.setAdapter(SpinnerAdapter);
        spinner_l_4000.setAdapter(SpinnerAdapter);
        spinner_l_6000.setAdapter(SpinnerAdapter);
        spinner_l_8000.setAdapter(SpinnerAdapter);

        spinner_r_125.setAdapter(SpinnerAdapter);
        spinner_r_250.setAdapter(SpinnerAdapter);
        spinner_r_500.setAdapter(SpinnerAdapter);
        spinner_r_1000.setAdapter(SpinnerAdapter);
        spinner_r_2000.setAdapter(SpinnerAdapter);
        spinner_r_3000.setAdapter(SpinnerAdapter);
        spinner_r_4000.setAdapter(SpinnerAdapter);
        spinner_r_6000.setAdapter(SpinnerAdapter);
        spinner_r_8000.setAdapter(SpinnerAdapter);

        spinner_l_125.setSelection(position_list_l[0]);
        spinner_l_250.setSelection(position_list_l[1]);
        spinner_l_500.setSelection(position_list_l[2]);
        spinner_l_1000.setSelection(position_list_l[3]);
        spinner_l_2000.setSelection(position_list_l[4]);
        spinner_l_3000.setSelection(position_list_l[5]);
        spinner_l_4000.setSelection(position_list_l[6]);
        spinner_l_6000.setSelection(position_list_l[7]);
        spinner_l_8000.setSelection(position_list_l[8]);

        spinner_r_125.setSelection(position_list_r[0]);
        spinner_r_250.setSelection(position_list_r[1]);
        spinner_r_500.setSelection(position_list_r[2]);
        spinner_r_1000.setSelection(position_list_r[3]);
        spinner_r_2000.setSelection(position_list_r[4]);
        spinner_r_3000.setSelection(position_list_r[5]);
        spinner_r_4000.setSelection(position_list_r[6]);
        spinner_r_6000.setSelection(position_list_r[7]);
        spinner_r_8000.setSelection(position_list_r[8]);

        //왼쪽, 오른쪽 데시벨 배열에 저장(초기화)
        for( int i = 0 ; i< 9 ; i++) {
            left_value[i] = decibel_list[load_position[i]];
            right_value[i] = decibel_list[load_position[i+9]];
        }


//        //오른쪽 데시벨 배열에 저장(초기화)
//        for( int i = 0 ; i< 9 ; i++) {
//            right_value[i] = decibel_list[load_position[i+9]];
//        }

        spinner_l_125.setOnItemSelectedListener(this);
        spinner_l_250.setOnItemSelectedListener(this);
        spinner_l_500.setOnItemSelectedListener(this);
        spinner_l_1000.setOnItemSelectedListener(this);
        spinner_l_2000.setOnItemSelectedListener(this);
        spinner_l_3000.setOnItemSelectedListener(this);
        spinner_l_4000.setOnItemSelectedListener(this);
        spinner_l_6000.setOnItemSelectedListener(this);
        spinner_l_8000.setOnItemSelectedListener(this);
        spinner_r_125.setOnItemSelectedListener(this);
        spinner_r_250.setOnItemSelectedListener(this);
        spinner_r_500.setOnItemSelectedListener(this);
        spinner_r_1000.setOnItemSelectedListener(this);
        spinner_r_2000.setOnItemSelectedListener(this);
        spinner_r_3000.setOnItemSelectedListener(this);
        spinner_r_4000.setOnItemSelectedListener(this);
        spinner_r_6000.setOnItemSelectedListener(this);
        spinner_r_8000.setOnItemSelectedListener(this);


        // 리셋 버튼 클릭 이벤트
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                scanDevice();

                for(int i = 0 ; i < 9 ; i++) {
                    left_value[i] = 30;
                    right_value[i] = 30;
                }

                spinner_l_125.setSelection(8);
                spinner_l_250.setSelection(8);
                spinner_l_500.setSelection(8);
                spinner_l_1000.setSelection(8);
                spinner_l_2000.setSelection(8);
                spinner_l_3000.setSelection(8);
                spinner_l_4000.setSelection(8);
                spinner_l_6000.setSelection(8);
                spinner_l_8000.setSelection(8);

                spinner_r_125.setSelection(8);
                spinner_r_250.setSelection(8);
                spinner_r_500.setSelection(8);
                spinner_r_1000.setSelection(8);
                spinner_r_2000.setSelection(8);
                spinner_r_3000.setSelection(8);
                spinner_r_4000.setSelection(8);
                spinner_r_6000.setSelection(8);
                spinner_r_8000.setSelection(8);
            }
        });

        // 저장 버튼 클릭 이벤트
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanDevice();

//                for(int i = 0 ; i < 12 ; i++) {
//                    right_gain_byte[i] = (byte)109;
//                }
//                sendData(right_gain_byte);



                SharedPreferences sharedPref = Equalizer.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                // 왼쪽 주파수
                editor.putInt("a_l_125", save_position[0]);
                editor.putInt("a_l_250", save_position[1]);
                editor.putInt("a_l_500", save_position[2]);
                editor.putInt("a_l_1000", save_position[3]);
                editor.putInt("a_l_2000", save_position[4]);
                editor.putInt("a_l_3000", save_position[5]);
                editor.putInt("a_l_4000", save_position[6]);
                editor.putInt("a_l_6000", save_position[7]);
                editor.putInt("a_l_8000", save_position[8]);

                // 오른쪽 주파수
                editor.putInt("a_r_125", save_position[9]);
                editor.putInt("a_r_250", save_position[10]);
                editor.putInt("a_r_500", save_position[11]);
                editor.putInt("a_r_1000", save_position[12]);
                editor.putInt("a_r_2000", save_position[13]);
                editor.putInt("a_r_3000", save_position[14]);
                editor.putInt("a_r_4000", save_position[15]);
                editor.putInt("a_r_6000", save_position[16]);
                editor.putInt("a_r_8000", save_position[17]);
                editor.commit();
            }
        });

        // 입력 버튼 클릭 이벤트
        input_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("Safaud", "인풋");
                Average_Function();

            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {

            // 왼쪽 주파수
            case R.id.l_125 :
                left_value[0] = decibel_list[spinner_l_125.getSelectedItemPosition()];
                save_position[0] = spinner_l_125.getSelectedItemPosition();
                break;

            case R.id.l_250 :
                left_value[1] = decibel_list[spinner_l_250.getSelectedItemPosition()];
                save_position[1] = spinner_l_250.getSelectedItemPosition();
                break;

            case R.id.l_500 :
                left_value[2] = decibel_list[spinner_l_500.getSelectedItemPosition()];
                save_position[2] = spinner_l_500.getSelectedItemPosition();
                break;

            case R.id.l_1000 :
                left_value[3] = decibel_list[spinner_l_1000.getSelectedItemPosition()];
                save_position[3] = spinner_l_1000.getSelectedItemPosition();
                break;

            case R.id.l_2000 :
                left_value[4] = decibel_list[spinner_l_2000.getSelectedItemPosition()];
                save_position[4] = spinner_l_2000.getSelectedItemPosition();
                break;

            case R.id.l_3000 :
                left_value[5] = decibel_list[spinner_l_3000.getSelectedItemPosition()];
                save_position[5] = spinner_l_3000.getSelectedItemPosition();
                break;

            case R.id.l_4000 :
                left_value[6] = decibel_list[spinner_l_4000.getSelectedItemPosition()];
                save_position[6] = spinner_l_4000.getSelectedItemPosition();
                break;

            case R.id.l_6000 :
                left_value[7] = decibel_list[spinner_l_6000.getSelectedItemPosition()];
                save_position[7] = spinner_l_6000.getSelectedItemPosition();
                break;

            case R.id.l_8000 :
                left_value[8] = decibel_list[spinner_l_8000.getSelectedItemPosition()];
                save_position[8] = spinner_l_8000.getSelectedItemPosition();
                break;

            // 오른쪽 주파수
            case R.id.r_125 :
                right_value[0] = decibel_list[spinner_r_125.getSelectedItemPosition()];
                save_position[9] = spinner_r_125.getSelectedItemPosition();
                break;

            case R.id.r_250 :
                right_value[1] = decibel_list[spinner_r_250.getSelectedItemPosition()];
                save_position[10] = spinner_r_250.getSelectedItemPosition();
                break;

            case R.id.r_500 :
                right_value[2] = decibel_list[spinner_r_500.getSelectedItemPosition()];
                save_position[11] = spinner_r_500.getSelectedItemPosition();
                break;

            case R.id.r_1000 :
                right_value[3] = decibel_list[spinner_r_1000.getSelectedItemPosition()];
                save_position[12] = spinner_r_1000.getSelectedItemPosition();
                break;

            case R.id.r_2000 :
                right_value[4] = decibel_list[spinner_r_2000.getSelectedItemPosition()];
                save_position[13] = spinner_r_2000.getSelectedItemPosition();
                break;

            case R.id.r_3000 :
                right_value[5] = decibel_list[spinner_r_3000.getSelectedItemPosition()];
                save_position[14] = spinner_r_3000.getSelectedItemPosition();
                break;

            case R.id.r_4000 :
                right_value[6] = decibel_list[spinner_r_4000.getSelectedItemPosition()];
                save_position[15] = spinner_r_4000.getSelectedItemPosition();
                break;

            case R.id.r_6000 :
                right_value[7] = decibel_list[spinner_r_6000.getSelectedItemPosition()];
                save_position[16] = spinner_r_6000.getSelectedItemPosition();
                break;

            case R.id.r_8000 :
                right_value[8] = decibel_list[spinner_r_8000.getSelectedItemPosition()];
                save_position[17] = spinner_r_8000.getSelectedItemPosition();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // 6분법 계산 및 모든 계산 함수
    public void Average_Function() {
        l_average = (left_value[2] + left_value[3] + left_value[3] + left_value[4] + left_value[4] + left_value[6]) / 6;
        r_average = (right_value[2] + right_value[3] + right_value[3] + right_value[4] + right_value[4] + right_value[6]) / 6;
        Log.d("Safaud", String.valueOf(l_average));

        // 왼쪽 주파수 밴드 배열 ( 16Hz, 31.5Hz, 63Hz, 125Hz, 250Hz, 500Hz, 1KHz, 2KHz, 4KHz, 8KHz, 16KHz, 32KHz )
        left_frequency_band[0] = left_value[0];
        left_frequency_band[1] = left_value[0];
        left_frequency_band[2] = left_value[0];
        left_frequency_band[3] = left_value[0];
        left_frequency_band[4] = left_value[1];
        left_frequency_band[5] = left_value[2];
        left_frequency_band[6] = left_value[3];
        left_frequency_band[7] = left_value[4];
        left_frequency_band[8] = (left_value[5] + left_value[6]) / 2;
        left_frequency_band[9] = (left_value[7] + left_value[8]) / 2;
        left_frequency_band[10] = (left_value[7] + left_value[8]) / 2;
        left_frequency_band[11] = (left_value[7] + left_value[8]) / 2;

        // 오른쪽 주파수 밴드 배열 ( 16Hz, 31.5Hz, 63Hz, 125Hz, 250Hz, 500Hz, 1KHz, 2KHz, 4KHz, 8KHz, 16KHz, 32KHz )
        right_frequency_band[0] = right_value[0];
        right_frequency_band[1] = right_value[0];
        right_frequency_band[2] = right_value[0];
        right_frequency_band[3] = right_value[0];
        right_frequency_band[4] = right_value[1];
        right_frequency_band[5] = right_value[2];
        right_frequency_band[6] = right_value[3];
        right_frequency_band[7] = right_value[4];
        right_frequency_band[8] = (right_value[5] + right_value[6]) / 2;
        right_frequency_band[9] = (right_value[7] + right_value[8]) / 2;
        right_frequency_band[10] = (right_value[7] + right_value[8]) / 2;
        right_frequency_band[11] = (right_value[7] + right_value[8]) / 2;



        // 주파수별 데시벨 값과 6분법 평균값의 차이 저장
        for(int i = 0 ; i < 12 ; i++) {
            left_gain[i] = left_frequency_band[i] - l_average;
            right_gain[i] = right_frequency_band[i] - r_average;

            // 최대, 최소 범위 지정
            if(left_gain[i] < -12) {
                left_gain[i] = -12;
            }
            if(left_gain[i] > 12) {
                left_gain[i] = 12;
            }

            if(right_gain[i] < -12) {
                right_gain[i] = -12;
            }
            if(right_gain[i] > 12) {
                right_gain[i] = 12;
            }
        }


        // 왼쪽, 오른쪽(-12dB ~ +12dB) 값을 아스키코드 97 ~ 121 값으로 변환
        for(int i = 0 ; i < 12 ; i++) {
            left_gain[i] += 109;
            right_gain[i] += 109;
//            Log.d("Safaud", String.valueOf(left_gain[i]));

            left_gain_byte[i] = (byte) left_gain[i];
            right_gain_byte[i] = (byte)right_gain[i];

//            right_gain_byte[i] = (byte)122;
//            Log.d("Safaud", Cha.valueOf(left_gain_byte[i]));
        }
        Log.d("Safaud", "AverageFunction");
//        sendData(left_gain_byte);
        sendData(right_gain_byte);
//        Log.d("Safaud data : " , ""+right_gain_byte);

    }

    private void permissionAccess() {


        if (!checkPermission(p1)) {
            Log.e("TAG",p1);
            requestPermission(p1);
        } else if (!checkPermission(p2)) {
            Log.e("TAG",p2);
            requestPermission(p2);
        } else if (!checkPermission(p3)) {
            Log.e("TAG",p3);
            requestPermission(p3);
        } else if (!checkPermission(p4)) {
            Log.e("TAG",p4);
            requestPermission(p4);
        } else {
            Toast.makeText(context, "All permission granted", Toast.LENGTH_LONG).show();
        }


    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission) {

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Equalizer.this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            //Do the stuff that requires permission...
            Log.e("TAG","Not say request");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Log.e("TAG", "val " + grantResults[0]);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAccess();
                } else {
//                    Toast.makeText(context, "Bye bye", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}


