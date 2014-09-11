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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.Toast;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.service.GPSService;

public class SmsReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
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
			
			if("#*location*#".equals(body)){ //GPS追踪
				Intent i = new Intent(context, GPSService.class);
				context.startService(i);
				
				String lastlocation = sp.getString("lastlocation", "");
				if (!TextUtils.isEmpty(lastlocation)) {
					//得到位置
					SmsManager.getDefault().sendTextMessage(safenumber, null, lastlocation, null, null);
				}
				abortBroadcast();
			}else if("#*alarm*#".equals(body)){ //报警音乐
				Toast.makeText(context, "报警音乐", 0).show();
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
//				player.setLooping(true);
				player.setVolume(1.0f, 1.0f);
				player.start();
				
				abortBroadcast();
			}else if("#*location*#".equals(body)){ //删除数据
				Toast.makeText(context, "删除数据", 0).show();
				abortBroadcast();
			} else if("#*lockscrn*#".equals(body)){ //远程锁屏
				//
				abortBroadcast();
			}
			
		}
	}

}
