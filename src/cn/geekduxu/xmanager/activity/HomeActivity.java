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

import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.R.drawable;
import cn.geekduxu.xmanager.R.id;
import cn.geekduxu.xmanager.R.layout;
import cn.geekduxu.xmanager.utils.MD5Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	private static final String[] FUNCTION_NAMES = {
		"�ֻ�����", "ͨѶ��ʿ", "�������", 
		"���̹���", "����ͳ��", "�ֻ�ɱ��",
		"������", "�߼�����", "��������"
	};

	public static final int[] FUNCTION_IMGS = {
		R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
		R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
		R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
	};

	private GridView listHome;
	private MAdapter adapter;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);

		// ʵ������ҳ�Ĺ����б�
		listHome = (GridView) findViewById(R.id.list_home);
		adapter = new MAdapter();
		listHome.setAdapter(adapter);
		listHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0: // �����ֻ�����
					showLostFoundDialog();
					break;
				case 1: break;
				case 2: break;
				case 3: break;
				case 4: break;
				case 5: break;
				case 6: break;
				case 7: break;
				case 8: // ������������
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;
				}
			}

		});
	}
	/**
	 * ��ʾ�����ֻ�����ʱ�ĶԻ���
	 */
	private void showLostFoundDialog() {
		//�ж��Ƿ��Ѿ����ù�����
		if(isSetupPwd()){
			//�Ѿ��������룬������������Ի���
			showEnterPwdDialog();
		} else {
			//û���������룬������������Ի���
			showSetupPwdDialog();
		}
	}
	
	private EditText setupPwd;
	private EditText confirmPwd;
	private Button btnOk;
	private Button btnCancle;
	private AlertDialog dialog;
	/**
	 * ��ʾ��������Ի���
	 */
	private void showSetupPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_pwd, null);
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
					Toast.makeText(HomeActivity.this, "���벻����Ϊ��Ŷ������ ^_^", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!password.equals(password)){
					Toast.makeText(HomeActivity.this, "�������벻һ��......", Toast.LENGTH_SHORT).show();
					return;
				}
				Editor edit = sp.edit();
				edit.putString("password", MD5Util.encodeMd5(password));
				edit.commit();
				dialog.dismiss();
				Intent intent = new Intent(HomeActivity.this, LostFoundActivity.class);
				startActivity(intent);
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
	/**
	 * ��ʾ��������Ի���
	 */
	private void showEnterPwdDialog() {
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_pwd, null);
		setupPwd = (EditText) view.findViewById(R.id.et_setup_pwd);
		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancle = (Button) view.findViewById(R.id.btn_cancle);
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//ȡ������
				String password = setupPwd.getText().toString().trim();
				if(TextUtils.isEmpty(password)){
					Toast.makeText(HomeActivity.this, "���벻����Ϊ��Ŷ������ ^_^", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!sp.getString("password", "").equals(MD5Util.encodeMd5(password))){
					setupPwd.setText("");
					Toast.makeText(HomeActivity.this, "������� ^_^ ������", Toast.LENGTH_SHORT).show();
					return;
				}
				dialog.dismiss();
				Intent intent = new Intent(HomeActivity.this, LostFoundActivity.class);
				startActivity(intent);
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
	/**
	 * �ж��Ƿ��Ѿ������ֻ���������
	 * @return
	 */
	private boolean isSetupPwd(){
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
	}
	
	/**
	 * 
	 */
	private class MAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return FUNCTION_NAMES.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
//			
			ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv = (TextView) view.findViewById(R.id.tv_item);
			
			tv.setText(FUNCTION_NAMES[position]);
			iv.setImageResource(FUNCTION_IMGS[position]);
			
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

}
