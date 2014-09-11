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

import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFoundActivity extends Activity {

	private SharedPreferences sp;
	private TextView safeNumber;
	private TextView tvProtecting;
	private ImageView ivProtecting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//判断是否已经做过设置向导
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_found);
			safeNumber = (TextView) findViewById(R.id.tv_safenumber);
			safeNumber.setText(sp.getString("safenumber", "暂未设置安全号码"));
			
			tvProtecting = (TextView) findViewById(R.id.tv_isprotecting);
			ivProtecting = (ImageView) findViewById(R.id.iv_isprotecting);
			
			if(sp.getBoolean("protecting", false)){
				tvProtecting.setText("防盗保护已经开启");
				ivProtecting.setImageResource(R.drawable.lock);
			}else{
				tvProtecting.setText("防盗保护已经关闭");
				ivProtecting.setImageResource(R.drawable.unlock);
			}
		}else{
			Intent intent = new Intent(LostFoundActivity.this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}
	}
	
	/**
	 * 重新进入防盗设置页面
	 */
	public void reEnterSetup(View view){
		Intent intent = new Intent(LostFoundActivity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}

}
