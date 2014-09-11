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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.service.GPSService;

/**
 * ���������������������¿����ղ������Ź㲥<br/>
 * ��������SmsReceiver2��ͨ�������������ݿ�
 * @see SmsReceiver2
 */
@Deprecated
public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private DevicePolicyManager dpm;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		if(!sp.getBoolean("protecting", false)){
			return;
		}
		String safenumber = sp.getString("safenumber", "safenumber");
		if(TextUtils.isEmpty(safenumber)){
			return;
		}
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
			String sender = sms.getOriginatingAddress();
			if(!sender.contains(safenumber)){
				continue;
			}
			String body = sms.getMessageBody();
			
			if("#*location*#".equals(body)){ //GPS׷��
				Intent i = new Intent(context, GPSService.class);
				context.startService(i);
				String lastlocation = sp.getString("lastlocation", "");
				Log.i("geekduxu", lastlocation);
				if (!TextUtils.isEmpty(lastlocation)) {
					//�õ�λ��
					SmsManager.getDefault().sendTextMessage(safenumber, null, lastlocation, null, null);
				}
				abortBroadcast();
			}else if("#*alarm*#".equals(body)){ //��������
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
//				player.setLooping(true);
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			}else if("#*wipedata*#".equals(body)){ //ɾ������
				//���Sdcard�ϵ�����
//				dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
				//�ָ���������
//				dpm.wipeData(0);
				abortBroadcast();
			} else if("#*lockscrn*#".equals(body)){ //Զ������
				dpm.lockNow();
				dpm.resetPassword("0000", 0);
				abortBroadcast();
			}
		}
	}

}
