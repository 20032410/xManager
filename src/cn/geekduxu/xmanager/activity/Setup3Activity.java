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

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.geekduxu.xmanager.R;

public class Setup3Activity extends BaseSetupActivity {
	
	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);

		etPhone = (EditText) findViewById(R.id.et_setup3_phone);
		etPhone.setText(sp.getString("safenumber", ""));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null || requestCode != 100){
			return;
		}
		String phone = data.getStringExtra("phone").replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
		etPhone.setText(phone);
	}

	public void selectContact(View v) {
		Intent intent = new Intent(Setup3Activity.this, SelectContactActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void next(View view) {
		//保存安全号码
		String phone = etPhone.getText().toString();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(Setup3Activity.this, "您还没有设置安全号码...", Toast.LENGTH_SHORT).show();
			return;
		}
		Editor edit = sp.edit();
		edit.putString("safenumber", phone);
		edit.commit();
		
		Intent intent = new Intent(this, Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void pre(View view) {
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
