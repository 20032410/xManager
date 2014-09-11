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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.Toast;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.db.PhoneAddressQueryUtil;
import cn.geekduxu.xmanager.utils.AnimationUtil;

public class ToolsActivity extends Activity {

	private EditText etPhoneNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools);
		
		etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
	}
	
	/**
	 * 查询手机归属地
	 */
	public void query(View v){
		
		String phoneNumber = etPhoneNumber.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			AnimationUtil.startRotateAnimation(etPhoneNumber);
			etPhoneNumber.setText("");
			return;
		}
		String address = PhoneAddressQueryUtil.queryAddress(phoneNumber);
		if(TextUtils.isEmpty(address)){
			Toast.makeText(this, "电话号码错误", Toast.LENGTH_SHORT).show();
			AnimationUtil.startRotateAnimation(etPhoneNumber);
			etPhoneNumber.setText("");
		}else{
			Toast.makeText(this, address, Toast.LENGTH_LONG).show();
		}
	}
		


}
