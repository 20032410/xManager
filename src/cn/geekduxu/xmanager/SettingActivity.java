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

package cn.geekduxu.xmanager;

import cn.geekduxu.xmanager.ui.SettingItemView;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;

public class SettingActivity extends Activity {
	
	private SettingItemView sivUpdate; 
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//设置是否自动更新
		sivUpdate = (SettingItemView) findViewById(R.id.siv_setting_update);
		boolean autoUpdate = sp.getBoolean("update", false);
		if(autoUpdate){
			sivUpdate.setStatus(true);
			sivUpdate.setDesc("自动更新已经开启");
		}else{
			sivUpdate.setStatus(false);
			sivUpdate.setDesc("自动更新已经关闭");
		}
		sivUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if(sivUpdate.isChecked()){ // 如果目前是选中状态
					sivUpdate.setStatus(false);
					sivUpdate.setDesc("自动更新已经关闭");
					editor.putBoolean("update", false);
				}else{
					sivUpdate.setStatus(true);
					sivUpdate.setDesc("自动更新已经开启");
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
	}

}
