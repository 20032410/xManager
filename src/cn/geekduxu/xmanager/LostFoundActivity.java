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
		//�ж��Ƿ��Ѿ�����������
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
	 * ���½����������ҳ��
	 */
	public void reEnterSetup(View view){
		Intent intent = new Intent(LostFoundActivity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
	}

}
