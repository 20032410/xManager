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
                     佛祖保佑    永无BUG                         
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
 * 黑名单数据库增删改查DAO
 */
public class BlackListDao {
	
	private BlackListDatabaseOpenHelper helper;

	public BlackListDao(Context context) {
		helper = new BlackListDatabaseOpenHelper(context);
	}

	/**
	 * 查询黑名单号码是否存在
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
	 * 查询黑名单号码拦截模式
	 */
	public String findMode(String number) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select mode from blacklist where number=?",
				new String[] { number });
		String rst = null;
		if(cursor.moveToNext()){
			rst = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return rst;
	}

	/**
	 * 查询全部黑名单号码
	 */
	public List<BlackListInfo> queryAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number, mode from blacklist order by _id desc", null);
		List<BlackListInfo> infos = new ArrayList<BlackListInfo>(cursor.getColumnCount());
		while (cursor.moveToNext()) {
			infos.add(new BlackListInfo(cursor.getString(0), cursor.getString(1)));
		}
		cursor.close();
		db.close();
		return infos;
	}

	/**
	 * 添加黑名单号码
	 * 
	 * @param number
	 *            号码
	 * @param mode
	 *            拦截模式
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
	 * 修改黑名单号码的拦截模式
	 * 
	 * @param number
	 *            待修改的号码
	 * @param newMode
	 *            新的拦截模式
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
	 * 删除黑名单号码
	 * 
	 * @param number
	 *            号码
	 */
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacklist", "number=?", new String[] { number });
		db.close();
	}
}
