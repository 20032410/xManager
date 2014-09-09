package cn.geekduxu.xmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void next(View view) {
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void pre(View v) {
	}

}
