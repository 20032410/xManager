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
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import cn.geekduxu.xmanager.db.BlackListDao;

/**
 * 拦截电话短信Service
 */
public class CallAndSmsSafeService extends Service {
	
	private InnerSmsReceiver smsReceiver;
	private BlackListDao blackListDao;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		blackListDao = new BlackListDao(this);
		smsReceiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(smsReceiver, filter);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
		smsReceiver = null;
	}
	private class InnerSmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//检查发件人
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
				//得到发件人
				String sender = message.getOriginatingAddress();
				String mode = blackListDao.findMode(sender);
				if("2".equals(mode) || "3".equals(mode)){
					abortBroadcast();
				}
			}
		}
	}

}
