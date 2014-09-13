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

package cn.geekduxu.xmanager.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.R.id;
import cn.geekduxu.xmanager.R.layout;
import cn.geekduxu.xmanager.service.AddressService;
import cn.geekduxu.xmanager.ui.SettingClickView;
import cn.geekduxu.xmanager.ui.SettingItemView;

public class SettingActivity extends Activity {
	
	private static final String[] NAMES = new String[]{
			"��͸��", "������", "��ʿ��", "������", "ƻ����", "���ѡ��"
	};
	private SettingItemView sivUpdate; 
	private SettingItemView sivShowAddress; 
	private SettingClickView scvChangeBg; 
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		initUpdate();
		initShowAddress();
		initBg();
	}
	
	private void initUpdate() {
		//�����Ƿ��Զ�����
		sivUpdate = (SettingItemView) findViewById(R.id.siv_setting_update);
		sivUpdate.setStatus(sp.getBoolean("update", true));
		sivUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if(sivUpdate.isChecked()){ // ���Ŀǰ��ѡ��״̬
					sivUpdate.setStatus(false);
					editor.putBoolean("update", false);
				}else{
					sivUpdate.setStatus(true);
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
	}

	private void initShowAddress() {
		sivShowAddress = (SettingItemView) findViewById(R.id.siv_setting_showaddress);
		sivShowAddress.setStatus(sp.getBoolean("showaddress", true));
		sivShowAddress.setOnClickListener(new View.OnClickListener() {
			Intent intent = new Intent(SettingActivity.this, AddressService.class);
			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if(sivShowAddress.isChecked()){ // ���Ŀǰ��ѡ��״̬
					sivShowAddress.setStatus(false);
					editor.putBoolean("showaddress", false);
					scvChangeBg.setClickable(false);
					stopService(intent);
				}else{
					sivShowAddress.setStatus(true);
					editor.putBoolean("showaddress", true);
					scvChangeBg.setClickable(true);
					startService(intent);
				}
				editor.commit();
			}
		});
	}
	
	private void initBg() {
		scvChangeBg = (SettingClickView) findViewById(R.id.scv_change_bg);
		scvChangeBg.setDesc(NAMES[sp.getInt("which", 5)]);
		scvChangeBg.setClickable(sivShowAddress.isChecked());
		scvChangeBg.setOnClickListener(new View.OnClickListener() {
 			@Override
			public void onClick(View v) {
				//����һ���Ի���
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("ѡ���������ʾ����");
				builder.setSingleChoiceItems(NAMES, sp.getInt("which", 5), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor edit = sp.edit();
						edit.putInt("which", which);
						edit.commit();
						scvChangeBg.setDesc(NAMES[which]);
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("ȡ��", null);
				builder.show();
			}
		});
	}
	
}
