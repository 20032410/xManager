package cn.geekduxu.xmanager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;

public class Setup4Activity extends Activity {

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

	public void next(View view) {
		Editor edit = sp.edit();
		edit.putBoolean("configed", true);
		edit.commit();
		Intent intent = new Intent(this, LostFoundActivity.class);
		startActivity(intent);
		finish();
	}

	public void pre(View view) {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
	}

}
