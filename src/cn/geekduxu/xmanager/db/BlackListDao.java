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

import java.util.ArrayList;
import java.util.List;

import cn.geekduxu.xmanager.domain.BlackListInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ���������ݿ���ɾ�Ĳ�DAO
 */
public class BlackListDao {
	
	private BlackListDatabaseOpenHelper helper;

	public BlackListDao(Context context) {
		helper = new BlackListDatabaseOpenHelper(context);
	}

	/**
	 * ��ѯ�����������Ƿ����
	 */
	public boolean exists(String number) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacklist where number=?",
				new String[] { number });
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}

	/**
	 * ��ѯȫ������������
	 */
	public List<BlackListInfo> queryAll() {
		
		List<BlackListInfo> infos = new ArrayList<BlackListInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number, mode from blacklist", null);
		while (cursor.moveToNext()) {
			infos.add(new BlackListInfo(cursor.getString(0), cursor.getString(1)));
		}
		cursor.close();
		db.close();
		return infos;
	}

	/**
	 * ��Ӻ���������
	 * 
	 * @param number
	 *            ����
	 * @param mode
	 *            ����ģʽ
	 */
	public void add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacklist", null, values);
		db.close();
	}

	/**
	 * �޸ĺ��������������ģʽ
	 * 
	 * @param number
	 *            ���޸ĵĺ���
	 * @param newMode
	 *            �µ�����ģʽ
	 */
	public void update(String number, String newMode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", newMode);
		db.update("blacklist", values, "number=?", new String[] { number });
		db.close();
	}

	/**
	 * ɾ������������
	 * 
	 * @param number
	 *            ����
	 */
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacklist", "number=?", new String[] { number });
		db.close();
	}
}
