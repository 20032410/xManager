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

package cn.geekduxu.xmanager.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.R.id;
import cn.geekduxu.xmanager.R.layout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {

	private ListView selectContact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		
		Log.i("geekduxu", "1");
		selectContact = (ListView) findViewById(R.id.list_select_contact);
		Log.i("geekduxu", "2");
		final List<Map<String,String>> contactInfo = getContactInfo();
		selectContact.setAdapter(new SimpleAdapter(SelectContactActivity.this, 
				contactInfo, R.layout.contact_item, new String[]{"name", "phone"}, 
				new int[]{R.id.tv_name,R.id.tv_phone}));
		selectContact.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String phone = contactInfo.get(position).get("phone");
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(0, intent);
				finish();
			}
		});
	}
	
	/**
	 * 获取联系人信息列表
	 */
	private List<Map<String, String>> getContactInfo(){
		List<Map<String, String>> list = null;
		//得到内容提供者
		ContentResolver resolver = getContentResolver();
		Uri contUri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		//查询
		Cursor cursor = resolver.query(contUri, new String[]{"contact_id"}, null, null, null);
		list = new ArrayList<Map<String,String>>(cursor.getColumnCount());
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			if(TextUtils.isEmpty(id)){
				continue;
			}
			Map<String, String> contact = new HashMap<String, String>(2);
			Cursor data = resolver.query(dataUri, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{id}, null);
			String mimetype = null;
			String data1 = null;
			while (data.moveToNext()) {
				mimetype = data.getString(1);
				data1 = data.getString(0);
				if("vnd.android.cursor.item/name".equals(mimetype)){//姓名
					contact.put("name", data1);
				}else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){//电话
					contact.put("phone", data1);
				}
				Log.i("geekduxu", mimetype+" : "+data1);
			}
			
			list.add(contact);
			data.close();
		}
		cursor.close();
		return list;
	}
	
//	@Override
//	public void onBackPressed() {}


}
