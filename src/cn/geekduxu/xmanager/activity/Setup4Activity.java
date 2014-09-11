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
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import cn.geekduxu.xmanager.R;

public class Setup4Activity extends BaseSetupActivity {
	
	private CheckBox cbStatus;
	private ImageView ivStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cbStatus = (CheckBox) findViewById(R.id.cb_setup4_status);
		ivStatus = (ImageView) findViewById(R.id.iv_setup4_status);
		
		boolean status = sp.getBoolean("protecting", false);
		if (status) {
			cbStatus.setChecked(true);
			cbStatus.setText("您已经开启防盗保护");
			ivStatus.setImageResource(R.drawable.lock);
		}else{
			cbStatus.setChecked(false);
			cbStatus.setText("您已经关闭防盗保护");
			ivStatus.setImageResource(R.drawable.unlock);
		}
		cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cbStatus.setText("您已经开启防盗保护");
					ivStatus.setImageResource(R.drawable.lock);
				}else{
					cbStatus.setText("您已经关闭防盗保护");
					ivStatus.setImageResource(R.drawable.unlock);
				}
				Editor editor = sp.edit();
				editor.putBoolean("protecting",isChecked);
				editor.commit();
			}
		});
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void next(View view) {
		Editor edit = sp.edit();
		edit.putBoolean("configed", true);
		edit.commit();
		Intent intent = new Intent(this, LostFoundActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void pre(View view) {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
