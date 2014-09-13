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
                     佛祖保佑    永无BUG                         
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
	 * 窗体管理者
	 */
	private WindowManager wm;
	private View view;
	private SharedPreferences sp;
	/** "半透明","活力橙","卫士蓝","金属灰","苹果绿" */
	private int[] IDS = { R.drawable.call_locate_white,
			R.drawable.call_locate_orange, R.drawable.call_locate_blue,
			R.drawable.call_locate_gray, R.drawable.call_locate_green };

	/**
	 * 电话服务
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
		// 监听来电
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
		// 用代码去注册广播接收者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		// 实例化窗体
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消监听来电
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;
		// 取消注册广播接收者
		unregisterReceiver(receiver);
		receiver = null;
	}

	private WindowManager.LayoutParams params;
	private long[] mHits = new long[2];
	/**
	 * 自定义土司
	 */
	public void showAddressToast(String address) {
		
		address = (TextUtils.isEmpty(address)) ? "未知归属地" : address;
		
		view = View.inflate(this, R.layout.address_show, null);
		TextView textview = (TextView) view.findViewById(R.id.tv_address);
		view.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 双击了
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
		// 窗体的参数
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
	 * 服务里面的广播接收者
	 */
	private class OutCallReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 播出的电话号码
			String phone = getResultData();
			// 查询数据库
			String address = PhoneAddressQueryUtil.queryAddress(phone);
			showAddressToast(address);
		}

	}

	private class MyListenerPhone extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state：状态，incomingNumber：来电号码
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
				// 查询数据库的操作
				String address = PhoneAddressQueryUtil
						.queryAddress(incomingNumber);
				showAddressToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 电话的空闲状态：挂电话、来电拒绝
				// 把这个View移除
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
			case MotionEvent.ACTION_DOWN: //手指按下
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE: //手指移动
				int newX = (int) event.getRawX();
				int newY = (int) event.getRawY();
				int dx = newX - startX;
				int dy = newY - startY;
				
				params.x += dx;
				params.y += dy;
				
				// 考虑边界问题
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
			case MotionEvent.ACTION_UP: //手指离开
				//记录控件位置
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
