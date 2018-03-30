package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Province 建表语句
	 */
	public static final String CREATE_PROVINCE="CREATE TABLE pROVINCE("+"id integer primary key autoincrement, "+
	"province_name text, "+"province_code text)";
	
	/**
	 * City 建表语句
	 */
	public static final String CREATE_CITY="CREATE TABLE City("+"id integer primary key autoincrement, "+
			"city_name text,"+"city_code text, "+"province_id integer)";
	/**
	 * COUNTY 建表语句
	 */
	public static final String CREATE_COUNTY="CREATE TABLE County("+"id integer primary key autoincrement, "+
			"county_name text,"+"county_code text, "+"city_id integer)";
	
	
	//重写OnCreate
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);//创建Province
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
