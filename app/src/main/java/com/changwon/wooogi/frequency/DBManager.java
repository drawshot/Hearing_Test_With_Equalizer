package com.changwon.wooogi.frequency;

/**
 * Created by wooogi on 2017. 10. 10..
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;





public class DBManager extends SQLiteOpenHelper {
    Context context;
    static final String dbName = "FrequencyData.db";
    static final String tableName = "frequency";

    public DBManager(Context context, int version) {
        super(context, dbName, null, version);
        this.context = context;
    }

    /** 생성자의 2번째 파라미터로 전달된 이름의 DB가 존재하지 않는 경우에 안드로이드 시스템은
     *  해당 이름의 DB를 생성하고 이어서 onCreate() 를 호출한다
     *  이때 onCreate(SQLiteDatabase db) 메소드의 파라미터로 생성된 DB의 인스턴스 참조가 전달된다
     *  이 메소드는 주로 DB 생성 후 테이블이 없는 상태이므로 앞으로 사용할 테이블을 생성하는 역할을 한다
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // String dropSql = "DROP TABLE IF EXISTS emp";
        // db.execSQL(dropSql);

        String createSql = "CREATE TABLE frequency ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "time TEXT," +
                "l_a125 INTEGER," +
                "l_a250 INTEGER," +
                "l_a500 INTEGER," +
                "l_a1000 INTEGER," +
                "l_a2000 INTEGER," +
                "l_a3000 INTEGER," +
                "l_a4000 INTEGER," +
                "l_a6000 INTEGER," +
                "l_a8000 INTEGER," +
                "r_a125 INTEGER," +
                "r_a250 INTEGER," +
                "r_a500 INTEGER," +
                "r_a1000 INTEGER," +
                "r_a2000 INTEGER," +
                "r_a3000 INTEGER," +
                "r_a4000 INTEGER," +
                "r_a6000 INTEGER," +
                "r_a8000 INTEGER," +
                "l_sextant REAL," +
                "r_sextant REAL," +
                "l_sextant_s REAL," +
                "r_sextant_s REAL," +
                "l_loss_ratio REAL," +
                "r_loss_ratio REAL," +
                "loss_ratio REAL)";
        db.execSQL(createSql);
        Toast.makeText(context, "DB is created", Toast.LENGTH_LONG).show();
    }

    /**SQLiteOpenHelper 클래스의 생성자의 마지막 파라미터에 전달되는 버전정보가 이전 버전보다 높으면
     * 이 메소드가 자동으로 호출된다
     * 이 메소드는 일반적으로 기존 테이블을 삭제하고 새 버전에 맞춰서 테이블을 새로 생성하는 목적으로 사용된다
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.d("onUpgrade(), DB Version", "OLD:"+oldVer+", NEW:"+newVer);
        // String dropSql = "DROP TABLE IF EXISTS emp";
        // db.execSQL(dropSql);
        // onCreate(db);
    }
}

class FrequencyDAO
{
    public static final String tableName = "frequency";

    DBManager dbManager;
    SQLiteDatabase db;

    FrequencyDAO(Context context, int ver) {
        this.dbManager = new DBManager(context, ver);
        db = dbManager.getWritableDatabase();
    }

    // 데이터 추가
    public void insert(FrequencyVO frequencydate) {
        String sql = "INSERT INTO " + tableName + " VALUES ( NULL, '"
                + frequencydate.date+ "', '"+ frequencydate.time +"', '"+ frequencydate.l_a125 + "', '"+ frequencydate.l_a250 + "', '"+ frequencydate.l_a500 + "', '"+
                frequencydate.l_a1000 + "', '"+ frequencydate.l_a2000 + "', '"+ frequencydate.l_a3000 + "', '"+ frequencydate.l_a4000 +
                "', '"+ frequencydate.l_a6000 + "', '"+ frequencydate.l_a8000 + "', '"+ frequencydate.r_a125 + "', '"+
                frequencydate.r_a250 + "', '"+ frequencydate.r_a500 + "', '"+ frequencydate.r_a1000 + "', '"+
                frequencydate.r_a2000 + "', '"+ frequencydate.r_a3000 + "', '"+ frequencydate.r_a4000 +
                "', '"+ frequencydate.r_a6000 + "', '"+ frequencydate.r_a8000 + "', '"+ frequencydate.l_sextant + "', '"+ frequencydate.r_sextant + "', '"+
                frequencydate.l_sextant_s + "', '" + frequencydate.r_sextant_s + "', '" +
                frequencydate.l_loss_ratio + "', '"+ frequencydate.r_loss_ratio + "','"+frequencydate.loss_ratio+"');";
        db.execSQL(sql);
        Log.d("INSERT", "데이터 추가");
    }

    // 리스트 가져오기
    public List<FrequencyVO> list() {
        String sql = "SELECT * FROM " + tableName + ";";
        Cursor rs = db.rawQuery(sql, null);

        rs.moveToFirst();
        List<FrequencyVO> list = new ArrayList<FrequencyVO>();

        while (!rs.isAfterLast()) {
            FrequencyVO frequencydate = new FrequencyVO(rs.getInt(0), rs.getString(1), rs.getString(2), rs.getInt(3),
                    rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8)
                    , rs.getInt(9), rs.getInt(10), rs.getInt(11), rs.getInt(12), rs.getInt(13),
                    rs.getInt(14), rs.getInt(15), rs.getInt(16)
                    , rs.getInt(17), rs.getInt(18), rs.getInt(19),rs.getInt(20), rs.getFloat(21),
                    rs.getFloat(22), rs.getString(23), rs.getString(24), rs.getFloat(25), rs.getFloat(26),
                    rs.getFloat(27));
            list.add(frequencydate);
            rs.moveToNext();
        }
        rs.close();
        return list;
    }

    // 한개의 레코드 가져오기
    public FrequencyVO getFrequencyById(int id) {
        //String sql = "SELECT * FROM " + tableName + " WHERE id = " + id + ";";
        //Cursor rs = db.rawQuery(sql, null);

        // 위의 방법대신 아래처럼 ? 표를 사용하여 파라미터를 표현할 수도 있다
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?;";
        Cursor rs = db.rawQuery(sql, new String[]{""+id }); // 파라미터에 전달할 값을 문자열 배열로 전달한다

        if (rs.moveToFirst()) {
            FrequencyVO frequencydate = new FrequencyVO(rs.getInt(0), rs.getString(1), rs.getString(2), rs.getInt(3),
                    rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getInt(8)
                    , rs.getInt(9), rs.getInt(10), rs.getInt(11), rs.getInt(12), rs.getInt(13),
                    rs.getInt(14), rs.getInt(15), rs.getInt(16)
                    , rs.getInt(17), rs.getInt(18), rs.getInt(19),rs.getInt(20), rs.getFloat(21),
                    rs.getFloat(22), rs.getString(23), rs.getString(24), rs.getFloat(25), rs.getFloat(26),
                    rs.getFloat(27));
            rs.close();
            return frequencydate;
        }
        rs.close();
        return null;
    }

//    // 레코드 변경
//    public void update(EmpVO emp) {
//        String sql = "UPDATE " + tableName + " SET empno = " + emp.empno + " WHERE id="+emp.empno+";";
//        db.execSQL(sql);
//        Log.i("UPDATE", "업데이트 성공");
//    }

    // 레코드 삭제
    public void delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = " + id + ";";
        db.execSQL(sql);
        Log.i("DELETE", "삭제 성공");
    }
}

class FrequencyVO
{
    int num;
    String date;
    String time;
    int l_a125;
    int l_a250;
    int l_a500;
    int l_a1000;
    int l_a2000;
    int l_a3000;
    int l_a4000;
    int l_a6000;
    int l_a8000;
    int r_a125;
    int r_a250;
    int r_a500;
    int r_a1000;
    int r_a2000;
    int r_a3000;
    int r_a4000;
    int r_a6000;
    int r_a8000;
    float l_sextant;
    float r_sextant;
    String l_sextant_s;
    String r_sextant_s;
    float l_loss_ratio;
    float r_loss_ratio;
    float loss_ratio;




//    String ename;
//    int deptno;
//    String hiredate;

    public FrequencyVO(int num, String date, String time, int l_a125, int l_a250, int l_a500, int l_a1000, int l_a2000, int l_a3000, int l_a4000, int l_a6000, int l_a8000,
                       int r_a125, int r_a250, int r_a500, int r_a1000, int r_a2000, int r_a3000, int r_a4000, int r_a6000, int r_a8000, float l_sextant, float r_sextant,
                 String l_sextant_s, String r_sextant_s, float l_loss_ratio, float r_loss_ratio, float loss_ratio){
        this.num = num;
        this.date = date;
        this.time = time;
        this.l_a125 = l_a125;
        this.l_a250 = l_a250;
        this.l_a500 = l_a500;
        this.l_a1000 = l_a1000;
        this.l_a2000 = l_a2000;
        this.l_a3000 = l_a3000;
        this.l_a4000 = l_a4000;
        this.l_a6000 = l_a6000;
        this.l_a8000 = l_a8000;
        this.r_a125 = r_a125;
        this.r_a250 = r_a250;
        this.r_a500 = r_a500;
        this.r_a1000 = r_a1000;
        this.r_a2000 = r_a2000;
        this.r_a3000 = r_a3000;
        this.r_a4000 = r_a4000;
        this.r_a6000 = r_a6000;
        this.r_a8000 = r_a8000;
        this.l_sextant = l_sextant;
        this.r_sextant = r_sextant;
        this.l_sextant_s = l_sextant_s;
        this.r_sextant_s = r_sextant_s;
        this.l_loss_ratio = l_loss_ratio;
        this.r_loss_ratio = r_loss_ratio;
        this.loss_ratio = loss_ratio;
    }
}
