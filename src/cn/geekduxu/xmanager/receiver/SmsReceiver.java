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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		if(!sp.getBoolean("protecting", false)){
			return;
		}
		String safenumber = sp.getString("safenumber", "");
		if(TextUtils.isEmpty(safenumber)){
			return;
		}
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
			String sender = sms.getOriginatingAddress();
			if(!sender.endsWith(sender)){
				continue;
			}
			String body = sms.getMessageBody();
			
			if("#*location*#".equals(body)){ //GPS׷��
				Toast.makeText(context, "GPS׷��", 0).show();
				abortBroadcast();
			}else if("#*alarm*#".equals(body)){ //��������
				Toast.makeText(context, "��������", 0).show();
				abortBroadcast();
			}else if("#*location*#".equals(body)){ //ɾ������
				Toast.makeText(context, "ɾ������", 0).show();
				abortBroadcast();
			} else if("#*lockscrn*#".equals(body)){ //Զ������
				Toast.makeText(context, "Զ������", 0).show();
				abortBroadcast();
			}
			
		}
	}

}
