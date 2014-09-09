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
		//�ж��Ƿ��Ѿ�����������
		boolean configed = sp.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_found);
			safeNumber = (TextView) findViewById(R.id.tv_safenumber);
			safeNumber.setText(sp.getString("safenumber", "��δ���ð�ȫ����"));
			
			tvProtecting = (TextView) findViewById(R.id.tv_isprotecting);
			ivProtecting = (ImageView) findViewById(R.id.iv_isprotecting);
			
			if(sp.getBoolean("protecting", false)){
				tvProtecting.setText("���������Ѿ�����");
				ivProtecting.setImageResource(R.drawable.lock);
			}else{
				tvProtecting.setText("���������Ѿ��ر�");
				ivProtecting.setImageResource(R.drawable.unlock);
			}
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
