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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.utils.MD5Util;

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
	
	
	private EditText setupPwd;
	private EditText confirmPwd;
	private Button btnOk;
	private Button btnCancle;
	private AlertDialog dialog;
	/**
	 * ������������
	 */
	public void rePassword(View v){
		AlertDialog.Builder builder = new Builder(LostFoundActivity.this);
		View view = View.inflate(LostFoundActivity.this, R.layout.dialog_setup_pwd, null);
		setupPwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		confirmPwd = (EditText) view.findViewById(R.id.et_confirm_pwd);
		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//ȡ������
				String password = setupPwd.getText().toString().trim();
				String repasswd = confirmPwd.getText().toString().trim();
				if(TextUtils.isEmpty(password) || TextUtils.isEmpty(repasswd)){
					Toast.makeText(LostFoundActivity.this, "���벻����Ϊ��Ŷ������ ^_^", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!password.equals(repasswd)){
					Toast.makeText(LostFoundActivity.this, "�������벻һ��......", Toast.LENGTH_SHORT).show();
					setupPwd.setText("");
					confirmPwd.setText("");
					return;
				}
				Editor edit = sp.edit();
				edit.putString("password", MD5Util.encodeMd5(password));
				edit.commit();
				Toast.makeText(LostFoundActivity.this, "�޸�����ɹ������μ��������롣", Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		});
		
		btnCancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		builder.setView(view);
		dialog = builder.show();
	}

}
