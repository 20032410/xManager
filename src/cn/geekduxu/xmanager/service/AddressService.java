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
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
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
	private SharedPreferences sp;
	/** "��͸��","������","��ʿ��","������","ƻ����" */
	private int[] IDS = { R.drawable.call_locate_white,
			R.drawable.call_locate_orange, R.drawable.call_locate_blue,
			R.drawable.call_locate_gray, R.drawable.call_locate_green };

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
		sp = getSharedPreferences("config", MODE_PRIVATE);
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

	private WindowManager.LayoutParams params;
	private long[] mHits = new long[2];
	/**
	 * �Զ�����˾
	 */
	public void showAddressToast(String address) {
		
		address = (TextUtils.isEmpty(address)) ? "δ֪������" : address;
		
		view = View.inflate(this, R.layout.address_show, null);
		TextView textview = (TextView) view.findViewById(R.id.tv_address);
		view.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// ˫����
					params.x = wm.getDefaultDisplay().getWidth()/2-view.getWidth()/2;
					params.y = wm.getDefaultDisplay().getHeight()/2-view.getHeight()/2;
					wm.updateViewLayout(view, params);
					Editor editor = sp.edit();
					editor.putInt("lastX", params.x);
					editor.putInt("lastY", params.y);
					editor.commit();
				}
			}
		});
		view.setOnTouchListener(touchListener);
		
		int id = sp.getInt("which", 5);
		id = (id == 5) ? ((int)(Math.random()*100))%5 : id;
		view.setBackgroundResource(IDS[id]);
		textview.setText(address);
		// ����Ĳ���
		params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		
		params.gravity = Gravity.TOP + Gravity.LEFT;
		params.x = sp.getInt("lastX", 100);
		params.y = sp.getInt("lastY", 100);

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
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
	
	private View.OnTouchListener touchListener =  new View.OnTouchListener() {
		int startX, startY;
		@SuppressWarnings("deprecation")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: //��ָ����
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE: //��ָ�ƶ�
				int newX = (int) event.getRawX();
				int newY = (int) event.getRawY();
				int dx = newX - startX;
				int dy = newY - startY;
				
				params.x += dx;
				params.y += dy;
				
				// ���Ǳ߽�����
				if (params.x < 0) {
					params.x = 0;
				}
				if (params.y < 0) {
					params.y = 0;
				}
				if (params.x > (wm.getDefaultDisplay().getWidth() - view.getWidth())) {
					params.x = (wm.getDefaultDisplay().getWidth() - view.getWidth());
				}
				if (params.y > (wm.getDefaultDisplay().getHeight() - view.getHeight())) {
					params.y = (wm.getDefaultDisplay().getHeight() - view.getHeight());
				}
				wm.updateViewLayout(view, params);
				
				startX = newX;
				startY = newY;
				break;
			case MotionEvent.ACTION_UP: //��ָ�뿪
				//��¼�ؼ�λ��
				Editor edit = sp.edit();
				edit.putInt("lastX", params.x);
				edit.putInt("lastY", params.y);
				edit.commit();
				break;
			}
			return false;
		}
	};
}
