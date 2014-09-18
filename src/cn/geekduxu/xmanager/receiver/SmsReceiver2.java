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
		if(!sp.getBoolean("protecting", false)){//û�п�������
			return;
		}
		String safenumber = sp.getString("safenumber", "safenumber");
		if(TextUtils.isEmpty(safenumber)){//��ȫ����Ϊ��
			return;
		}
		
		Cursor cursor = context.getContentResolver().query(
				Uri.parse("content://sms/inbox"), 
				new String[]{"address", "body", "date"}, "type=1", null, "date desc limit 1");
		if(!cursor.moveToNext()) {
			return;
		}
		String sender = cursor.getString(0);
		String body = cursor.getString(1);
		long date = cursor.getLong(2);
		cursor.close();
		
		deleteSms(body, date);
		
		if(!sender.contains(safenumber)){//���ǰ�ȫ����Ķ���
			return;
		}
//		if(Math.abs(System.currentTimeMillis()-date) > 500){
//			return;
//		}
		if(date == lastSmsTime){
			return;
		}
		if(null == dpm){
			dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		}
		
		if("#*location*#".equals(body)){ //GPS׷��
			sendLocation(safenumber);
		}else if("#*alarm*#".equals(body)){ //��������
			playMusic();
		}else if("#*wipedata*#".equals(body)){ //ɾ������
			if(!isAdminActive()){
				return;
			}
			//���Sdcard�ϵ�����
			dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
			//�ָ���������
			dpm.wipeData(0);
		} else if("#*lockscrn*#".equals(body)){ //Զ������
			if(!isAdminActive()){
				return;
			}
			dpm.lockNow();
			dpm.resetPassword("duxu", 0);
		}
		lastSmsTime = date;

	}

	private void deleteSms(final String body, final long date) {
		new Thread(){
			public void run() {
				try {
					int i = context.getContentResolver().delete(
							Uri.parse("content://sms/"), "date=? and body=?", new String[]{""+date, body});
				} catch (Exception e) {
					Log.i("geekduxu", "delete error");
				}
			}
		}.start();
	}

	/**
	 * ����豸�ļ���״̬
	 */
	private boolean isAdminActive() {
		return dpm.isAdminActive(new ComponentName(context, DeviceAdmin.class));
	}
	
	/**
	 * ����λ����Ϣ
	 */
	private void sendLocation(String safenumber) {
		Intent i = new Intent(context, GPSService.class);
		context.startService(i);
		String lastlocation = sp.getString("lastlocation", "");
		if (!TextUtils.isEmpty(lastlocation)) {
			//�õ�λ��
			SmsManager.getDefault().sendTextMessage(safenumber, null, lastlocation, null, null);
		}else {
			SmsManager.getDefault().sendTextMessage(safenumber, null, "���ڻ�ȡ�����Ժ��ٷ��������ȡ��", null, null);
		}		
	}

	/**
	 * ���ű�������
	 */
	private void playMusic() {
		MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
//		player.setLooping(true);
		player.setVolume(1.0f, 1.0f);
		player.start();
	}
	
	/*		try {
			int i = context.getContentResolver().delete(
					Uri.parse("content://sms/"), "date=? and body=?", new String[]{""+date, body});
			Log.i("geekduxu", "delete:"+i);
		} catch (Exception e) {
			Log.i("geekduxu", "delete error");
		}*/

}
