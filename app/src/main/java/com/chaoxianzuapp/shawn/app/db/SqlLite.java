package com.chaoxianzuapp.shawn.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.chaoxianzuapp.shawn.app.BasicInfo;
/**
 * 메모 데이터베이스
 */
public class SqlLite {

	/**
	 * 싱글톤 인스턴스
	 */
	private static SqlLite database;
	/**
	 * Helper class defined
	 */
	private DatabaseHelper dbHelper;
	/**
	 * SQLiteDatabase 인스턴스
	 */
	private SQLiteDatabase db;
	/**
	 * 컨텍스트 객체
	 */
	private Context context;
	//table version
	public static int DB_VERSION = 2;
	//sql lite database
	public static String DB_SQLLITE = "chaoxianzuapp/chaoxianzuapp_DB.db";
	//sql lite table
	public static String DB_USER_LOGIN_INFO = "login_set";
	/**
	 * 생성자
	 */
	private SqlLite(Context context) {
		this.context = context;
	}
	/**
	 * 인스턴스 가져오기
	 */
	public static SqlLite getInstance(Context context) {

		if (database == null) {
			database = new SqlLite(context);
		}

		return database;
	}
	/**
	 * 데이터베이스 열기
	 */
	public boolean open() {println("opening database [" + DB_SQLLITE + "].");

		dbHelper = new DatabaseHelper(context);

		db = dbHelper.getWritableDatabase();

		return true;
	}
	/**
	 * 데이터베이스 닫기
	 */
	public void close() {println("closing database [" + DB_SQLLITE + "].");

		db.close();

		database = null;
	}
	/**
	 * execute raw query using the input SQL
	 * close the cursor after fetching any result
	 *
	 * @param SQL
	 * @return
	 */
	public Cursor rawQuery(String SQL) {println("\nexecuteQuery called.\n");

		Cursor c1 = null;
		try {
			c1 = db.rawQuery(SQL, null);
			println("cursor count : " + c1.getCount());
		} catch(Exception ex) {
			Log.e(BasicInfo.SQLLIST_TAG, "Exception in executeQuery", ex);
		}
		return c1;
	}
	public boolean execSQL(String SQL) {println("\nexecute called.\n");

		try {
			Log.d(BasicInfo.SQLLIST_TAG, "SQL : " + SQL);
			db.execSQL(SQL);
		} catch(Exception ex) {
			Log.e(BasicInfo.SQLLIST_TAG, "Exception in executeQuery", ex);
			return false;
		}
		return true;
	}
	/**
	 * Database Helper inner class
	 */
	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_SQLLITE, null, DB_VERSION);

			println("DatabaseHelper DB_VERSION[" + DB_VERSION + "].");
		}
		public void onCreate(SQLiteDatabase db) {
			println("creating database [" + DB_SQLLITE + "].");

			// DB_USER_LOGIN_INFO
			println("creating table [" + DB_USER_LOGIN_INFO + "].");

			// drop existing table
			String DROP_SQL = "drop table if exists " + DB_USER_LOGIN_INFO;
			try {
				db.execSQL(DROP_SQL);
			} catch(Exception ex) {
				Log.e(BasicInfo.SQLLIST_TAG, "Exception in DROP_SQL", ex);
			}

			// create table 회원 셋팅 정보
			String CREATE_SQL = "create table " + DB_USER_LOGIN_INFO + "("
					//+ "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ "  auto_login INTEGER, "
					+ "  mb_id text, "
					+ "  mb_password text, "
					+ "	 appUrl text "
					+ ")";
			try {
				db.execSQL(CREATE_SQL);
			} catch(Exception ex) {
				Log.e(BasicInfo.SQLLIST_TAG, "Exception in CREATE_SQL", ex);
			}
		}
		public void onOpen(SQLiteDatabase db)
		{
			println("opened database [" + DB_SQLLITE + "].");
		}
		public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion)
		{
			println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
			if(oldVersion < 2){//DB버전5 이하면 SQLLITE에 앱 다운 ANDROID PATH 필드 추가

				db.execSQL("DROP TABLE IF EXISTS " + DB_USER_LOGIN_INFO);
				onCreate(db);
			}
		}
	}
	private void println(String msg) {
		Log.d(BasicInfo.SQLLIST_TAG, msg);
	}
}