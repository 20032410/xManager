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

package cn.geekduxu.xmanager.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.service.GPSService;

public class SmsReceiver2 extends ContentObserver {
	
	private long lastSmsTime;

	private Context context;
	private SharedPreferences sp;
	private DevicePolicyManager dpm;
	
	public SmsReceiver2(Handler handler, Context context) {
		super(handler);
		this.context = context;
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
	}

	@Override
	public synchronized void onChange(boolean selfChange, Uri uri) {
		
		if(sp == null){
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		if(!sp.getBoolean("protecting", false)){//没有开启保护
			return;
		}
		String safenumber = sp.getString("safenumber", "safenumber");
		if(TextUtils.isEmpty(safenumber)){//安全号码为空
			return;
		}
		
		Cursor cursor = context.getContentResolver().query(
				Uri.parse("content://sms/inbox"), 
				new String[]{"_id", "address", "body", "date"}, "type=1", null, "date desc limit 1");
		if(!cursor.moveToNext()) {
			return;
		}
		String id = cursor.getString(0);
		String sender = cursor.getString(1);
		String body = cursor.getString(2);
		long date = cursor.getLong(3);
		cursor.close();
		
		try {
			int i = context.getContentResolver().delete(
					Uri.parse("content://sms/"), "_id=?", new String[]{id});
			Log.i("geekduxu", "delete:"+i);
		} catch (Exception e) {
			Log.i("geekduxu", "delete error");
		}
		
		Log.i("geekduxu", "date:"+date);
		if(!sender.contains(safenumber)){//不是安全号码的短信
			return;
		}
		
		if(Math.abs(System.currentTimeMillis()-date) > 500){
			return;
		}
		
		if(date == lastSmsTime){
			return;
		}
		
		if(null == dpm){
			dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		}
		
		if("#*location*#".equals(body)){ //GPS追踪
			sendLocation(safenumber);
		}else if("#*alarm*#".equals(body)){ //报警音乐
			playMusic();
		}else if("#*wipedata*#".equals(body)){ //删除数据
			if(!isAdminActive()){
				return;
			}
			//清除Sdcard上的数据
			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
			//恢复出厂设置
			dpm.wipeData(0);
		} else if("#*lockscrn*#".equals(body)){ //远程锁屏
			if(!isAdminActive()){
				return;
			}
			dpm.lockNow();
			dpm.resetPassword("duxu", 0);
		}
		lastSmsTime = date;
	}

	/**
	 * 获得设备的激活状态
	 */
	private boolean isAdminActive() {
		return dpm.isAdminActive(new ComponentName(context, DeviceAdmin.class));
	}
	
	/**
	 * 发送位置信息
	 */
	private void sendLocation(String safenumber) {
		Intent i = new Intent(context, GPSService.class);
		context.startService(i);
		String lastlocation = sp.getString("lastlocation", "");
		if (!TextUtils.isEmpty(lastlocation)) {
			//得到位置
			SmsManager.getDefault().sendTextMessage(safenumber, null, lastlocation, null, null);
		}else {
			SmsManager.getDefault().sendTextMessage(safenumber, null, "正在获取，请稍后再发送命令获取。", null, null);
		}		
	}

	/**
	 * 播放报警声音
	 */
	private void playMusic() {
		MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
//		player.setLooping(true);
		player.setVolume(1.0f, 1.0f);
		player.start();
	}
	
	

}
