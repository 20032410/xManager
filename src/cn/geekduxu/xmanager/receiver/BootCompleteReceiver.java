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
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * 接收开机事件
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		if (!sp.getBoolean("protecting", false)) {
			return;
		}
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//读取之前保存的SIM卡信息
		String savedSim = sp.getString("sim", "");
		//读取当前的SIM卡信息
		String nowSim = tm.getSimSerialNumber();
		//比较
		if (savedSim.equals(nowSim)) { // sim没有改变
			return;
		}
		Toast.makeText(context, "SIM卡变更", Toast.LENGTH_LONG).show();
		String safenumber = sp.getString("safenumber","");
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(safenumber,null,"手机SIM卡发生改变，这是新的手机号码。",null,null);
	} 

}
