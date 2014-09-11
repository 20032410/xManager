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
                     ·ð×æ±£ÓÓ    ÓÀÎÞBUG                         
                   Code by duxu0711@163.com                      
////////////////////////////////////////////////////////////////*/

package cn.geekduxu.xmanager.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PhoneAddressQueryUtil {

	private static final String PATH = "data/data/cn.geekduxu.xmanager/files/address.db";

	/**
	 * ¸ù¾ÝºÅÂë²éÑ¯¹éÊôµØ
	 * 
	 * @param phoneNumber
	 *            ´ý²éÑ¯µÄºÅÂë
	 */
	public static String queryAddress(String phoneNumber) {
		if(!phoneNumber.matches("^\\d+$")){
			return null;
		}
		String location = "";
		try {
			SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
			if(phoneNumber.matches("^1[34568]\\d{9}$")){
				Cursor cursor = database .rawQuery(
						"select location from data2 where id = (select outkey from data1 where id=?)",
						new String[] { phoneNumber.substring(0, 7) });
				if (cursor.moveToNext()) {
					location = cursor.getString(0);
					cursor.close();
				}
			} else if(phoneNumber.length() >= 10 && phoneNumber.startsWith("0")) {
				Cursor cursor = database .rawQuery(
						"select location from data2 where area = ?",
						new String[] { phoneNumber.substring(1, 3) });
				if (cursor.moveToNext()) {
					location = cursor.getString(0);
					cursor.close();
				}
				
				cursor = database .rawQuery(
						"select location from data2 where area = ?",
						new String[] { phoneNumber.substring(1, 4) });
				if (cursor.moveToNext()) {
					location = cursor.getString(0);
					cursor.close();
				}
			} else if(phoneNumber.length() == 3){
				
			}
		} catch (Exception e) {
			return null;
		}
		return location;
	}
}
