package cn.geekduxu.xmanager.activity;

import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.R.anim;
import cn.geekduxu.xmanager.R.id;
import cn.geekduxu.xmanager.R.layout;
import cn.geekduxu.xmanager.ui.SettingItemView;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivBindSim;
	private TelephonyManager tm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sivBindSim = (SettingItemView) findViewById(R.id.siv_setup2_sim);
		sivBindSim.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();
				if(sivBindSim.isChecked()){ //取消保存Sim卡的序列号
					sivBindSim.setStatus(false);
					edit.putString("sim", null);
				}else { //保存Sim卡的序列号
					sivBindSim.setStatus(true);
					edit.putString("sim", tm.getSimSerialNumber());
				}
				edit.commit();
			}
		});
		sivBindSim.setStatus(!TextUtils.isEmpty(sp.getString("sim", null)));
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void next(View view) {
		
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(Setup2Activity.this, "SIM卡没有绑定，无法使用手机防盗功能！", Toast.LENGTH_LONG).show();
			return ;
		}
		
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void pre(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
