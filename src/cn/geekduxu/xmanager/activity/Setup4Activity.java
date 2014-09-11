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

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.receiver.DeviceAdmin;

public class Setup4Activity extends BaseSetupActivity {
	
	private DevicePolicyManager dpm;
	
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
			cbStatus.setText("���Ѿ�������������");
			ivStatus.setImageResource(R.drawable.lock);
		}else{
			cbStatus.setChecked(false);
			cbStatus.setText("���Ѿ��رշ�������");
			ivStatus.setImageResource(R.drawable.unlock);
		}
		cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cbStatus.setText("���Ѿ�������������");
					ivStatus.setImageResource(R.drawable.lock);
				}else{
					cbStatus.setText("���Ѿ��رշ�������");
					ivStatus.setImageResource(R.drawable.unlock);
				}
				Editor editor = sp.edit();
				editor.putBoolean("protecting",isChecked);
				editor.commit();
			}
		});
		
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, "Ϊ�˱�֤�ֻ���Ϣ��ȫ����������ɺ����˳���", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void next(View view) {
		Editor edit = sp.edit();
		edit.putBoolean("configed", true);
		edit.commit();
		
		ComponentName mDeviceAdminSample = new ComponentName(this, DeviceAdmin.class);
		if((cbStatus.isChecked()) && (!dpm.isAdminActive(mDeviceAdminSample))){
			//����һ��Intent 
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			//��Ҫ����˭
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
			//Ȱ˵�û���������ԱȨ��
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "����һ������");
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, LostFoundActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		}
	}

	@Override
	public void pre(View view) {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
