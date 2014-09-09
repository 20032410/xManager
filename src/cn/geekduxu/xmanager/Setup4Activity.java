package cn.geekduxu.xmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends BaseSetupActivity {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
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
