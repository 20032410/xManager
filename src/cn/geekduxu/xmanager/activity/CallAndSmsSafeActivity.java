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

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.db.BlackListDao;
import cn.geekduxu.xmanager.domain.BlackListInfo;
import cn.geekduxu.xmanager.utils.AnimationUtil;

public class CallAndSmsSafeActivity extends Activity {
	
	private BlacklistItemAdapter adapter;
	private ListView lvCallSmsSafe;
	private List<BlackListInfo> infos;
	private BlackListDao dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callandsmssafe);
		dao = new BlackListDao(CallAndSmsSafeActivity.this);
		infos = dao.queryAll();
		lvCallSmsSafe = (ListView) findViewById(R.id.lv_callsms_safe);
		adapter = new BlacklistItemAdapter();
		lvCallSmsSafe.setAdapter(adapter);
	}
	
	
	private EditText etBlackNumber;
	private CheckBox cbPhone;
	private CheckBox cbSms;
	private AlertDialog dialog;
	/**
	 * ����һ����������¼
	 */
	public void addBlackNumber(View v){
		AlertDialog.Builder builder = new Builder(CallAndSmsSafeActivity.this);
		dialog = builder.create();
		View view = View.inflate(CallAndSmsSafeActivity.this, R.layout.dialog_addblacknumber, null);
		etBlackNumber =  (EditText) view.findViewById(R.id.et_black_number);
		cbPhone = (CheckBox) view.findViewById(R.id.cb_phone);
		cbSms = (CheckBox) view.findViewById(R.id.cb_sms);
		
		view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});	
		view.findViewById(R.id.btn_contact).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CallAndSmsSafeActivity.this, SelectContactActivity.class);
				startActivityForResult(intent, 10101);
			}
		});
		view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = etBlackNumber.getText().toString().trim();
				if(TextUtils.isEmpty(phone)){
					AnimationUtil.startRotateAnimation(CallAndSmsSafeActivity.this, etBlackNumber);
					return;
				}
				if(!isValuablePhone(phone)){
					Toast.makeText(CallAndSmsSafeActivity.this, "�绰���벻��ȷ", Toast.LENGTH_SHORT).show();
					AnimationUtil.startRotateAnimation(CallAndSmsSafeActivity.this, etBlackNumber);
					return;
				}
				int mode = ((cbPhone.isChecked()) ? 1 : 0) + ((cbSms.isChecked()) ? 2 : 0);
				if(mode == 0){
					Toast.makeText(CallAndSmsSafeActivity.this, "��ѡ������ģʽ", Toast.LENGTH_SHORT).show();
					AnimationUtil.startRotateAnimation(CallAndSmsSafeActivity.this, cbPhone);
					AnimationUtil.startRotateAnimation(CallAndSmsSafeActivity.this, cbSms);
					return;
				}
				if(dao.exists(phone)){
					dao.update(phone, mode+"");	
					setMode(phone, mode+"");
					Toast.makeText(CallAndSmsSafeActivity.this, "�Ѹ�������ģʽ", Toast.LENGTH_SHORT).show();
				} else {
					dao.add(phone, mode+"");
					infos.add(0, new BlackListInfo(phone, mode + ""));
					Toast.makeText(CallAndSmsSafeActivity.this, "����Ӻ���������", Toast.LENGTH_SHORT).show();
				}
				//֪ͨListView����������������ʾ����
				adapter.notifyDataSetChanged();
				//�رնԻ���
				dialog.dismiss();
			}
		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null || requestCode != 10101){
			return;
		}
		String phone = data.getStringExtra("phone").replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
		if(etBlackNumber != null){
			etBlackNumber.setText(phone);
		}
	}
	
	private boolean isValuablePhone(String phone) {
		return phone.matches("^1[34568]\\d{9}$");
	}

	private class BlacklistItemAdapter extends BaseAdapter {
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View view = null;

			if(convertView == null){
				view = View.inflate(CallAndSmsSafeActivity.this, R.layout.list_item_callandsms, null);
				holder = new ViewHolder();
				holder.tvNumber = (TextView) view.findViewById(R.id.tv_black_number);
				holder.tvMode = (TextView) view.findViewById(R.id.tv_black_mode);
				holder.ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
				//�������TextView������
				view.setTag(holder);
			} else {
				System.out.println("2");
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			
			holder.tvNumber.setText(infos.get(position).getNumber());
			holder.tvMode.setText(getModeName(infos.get(position).getMode()));

			holder.ivDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					AlertDialog.Builder builder = new Builder(CallAndSmsSafeActivity.this);
					builder.setTitle("����")
						.setMessage("ȷ����"+infos.get(position).getNumber()+"�Ƴ���")
						.setNegativeButton("ȡ��", null)
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dao.delete(infos.remove(position).getNumber());
								adapter.notifyDataSetChanged();
							}
						}).show();
				}
			});
			
			view.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					
					AlertDialog.Builder builder = new Builder(CallAndSmsSafeActivity.this);
					dialog = builder.create();
					View view = View.inflate(CallAndSmsSafeActivity.this, R.layout.dialog_updateblacknumber, null);
					EditText etPhone =  (EditText) view.findViewById(R.id.et_black_number);
					etPhone.setText(infos.get(position).getNumber());
					
					cbPhone = (CheckBox) view.findViewById(R.id.cb_phone);
					cbSms = (CheckBox) view.findViewById(R.id.cb_sms);
					
					switch (Integer.parseInt(infos.get(position).getMode())) {
					case 1: cbPhone.setChecked(true); break;
					case 2: cbSms.setChecked(true); break;
					case 3: cbPhone.setChecked(true); cbSms.setChecked(true); break;
					}
					
					view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});	
					
					view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String phone = infos.get(position).getNumber();
							int mode = ((cbPhone.isChecked()) ? 1 : 0) + ((cbSms.isChecked()) ? 2 : 0);
							if(mode == 0){
								Toast.makeText(CallAndSmsSafeActivity.this, "��ѡ������ģʽ", Toast.LENGTH_SHORT).show();
								AnimationUtil.startRotateAnimation(CallAndSmsSafeActivity.this, cbPhone);
								AnimationUtil.startRotateAnimation(CallAndSmsSafeActivity.this, cbSms);
								return;
							}
							dao.update(phone, mode+"");	
							setMode(phone, mode+"");
							Toast.makeText(CallAndSmsSafeActivity.this, "�Ѹ�������ģʽ", Toast.LENGTH_SHORT).show();
							//֪ͨListView����������������ʾ����
							adapter.notifyDataSetChanged();
							//�رնԻ���
							dialog.dismiss();
						}
					});
					dialog.setView(view, 0, 0, 0, 0);
					dialog.show();
					return true;
				}
			});
			return view;
		}
		private String getModeName(String mode) {
			if("1".equals(mode)){
				return "�����ص绰"; 
			}
			if("2".equals(mode)){
				return "�����ض���"; 
			}
			if("3".equals(mode)){
				return "���ص绰�Ͷ���"; 
			}
			return "���ݳ���";
		}
		@Override
		public int getCount() {
			return infos.size();
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
	}; 
	
	/**
	 * ��¼ListView���ڴ��ַ
	 */
	private class ViewHolder {
		public TextView tvNumber;
		public TextView tvMode;
		private ImageView ivDelete;
	}

	private void setMode(String number, String mode){
		for (BlackListInfo info : infos) {
			if(info.getNumber().equals(number)){
				info.setMode(mode);
			}
		}
	}
	
	
}
