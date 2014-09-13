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

package cn.geekduxu.xmanager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.db.PhoneAddressQueryUtil;

public class AddressService extends Service {

	/**
	 * ���������
	 */
	private WindowManager wm;
	private View view;

	/**
	 * �绰����
	 */
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;

	private OutCallReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// ��������
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
		// �ô���ȥע��㲥������
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		// ʵ��������
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ȡ����������
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;
		// ȡ��ע��㲥������
		unregisterReceiver(receiver);
		receiver = null;
	}

	/**
	 * �Զ�����˾
	 */
	public void showAddressToast(String address) {
		view = View.inflate(this, R.layout.address_show, null);
		TextView textview = (TextView) view.findViewById(R.id.tv_address);

		// "��͸��","������","��ʿ��","������","ƻ����"
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		
		int id = sp.getInt("which", 5);
		id = (id == 5) ? ((int)(Math.random()*100))%5 : id;
		view.setBackgroundResource(ids[id]);
		textview.setText(address);
		// ����Ĳ��������ú���
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		wm.addView(view, params);
	}

	/**
	 * ��������Ĺ㲥������
	 */
	private class OutCallReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// �����ĵ绰����
			String phone = getResultData();
			// ��ѯ���ݿ�
			String address = PhoneAddressQueryUtil.queryAddress(phone);
			showAddressToast(address);
		}

	}

	private class MyListenerPhone extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state��״̬��incomingNumber���������
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ������������
				// ��ѯ���ݿ�Ĳ���
				String address = PhoneAddressQueryUtil
						.queryAddress(incomingNumber);
				showAddressToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// �绰�Ŀ���״̬���ҵ绰������ܾ�
				// �����View�Ƴ�
				if (view != null) {
					wm.removeView(view);
				}
				break;
			}
		}
	}
}
