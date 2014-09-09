package cn.geekduxu.xmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LostFoundActivity extends Activity {

	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//判断是否已经做过设置向导
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_found);
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
