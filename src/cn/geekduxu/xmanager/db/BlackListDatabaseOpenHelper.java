/*///////////////////////////////////////////////////////////////// 

                          _ooOoo_                               
                         o8888888o                              
                         88" . "88                              
                         (| ^_^ |)                              
                         O\  =  /O                              
                      ____/`---'\____                            
                    .'  \\|     |//  `.                          
                   /  \\|||  :  |||//  \                        
                  /  _||||| -:- |||||-  \                       
                  |   | \\\  -  /// |   |                       
                  | \_|  ''\---/''  |   |                       
                  \  .-\__  `-`  ___/-. /                        
                ___`. .'  /--.--\  `. . ___                      
              ."" '<  `.___\_<|>_/___.'  >'"".                
            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                  
            \  \ `-.   \_ __\ /__ _/   .-` /  /                 
      ========`-.____`-.___\_____/___.-`____.-'========          
                           `=---='                               
      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        
                     ���汣��    ����BUG                         
                   Code by duxu0711@163.com                      
////////////////////////////////////////////////////////////////*/ 

package cn.geekduxu.xmanager.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���������ݿ������
 * @author Administrator
 *
 */
public class BlackListDatabaseOpenHelper extends SQLiteOpenHelper {

	public BlackListDatabaseOpenHelper(Context context) {
		super(context, "blacklist.db", null, 1);
	}

	/**
	 * ��ʼ�����ݿ��ṹ
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE blacklist (_id integer primary key autoincrement,number varchar(20), mode varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
