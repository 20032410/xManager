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
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * ���տ����¼�
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
		//��ȡ֮ǰ�����SIM����Ϣ
		String savedSim = sp.getString("sim", "");
		//��ȡ��ǰ��SIM����Ϣ
		String nowSim = tm.getSimSerialNumber();
		//�Ƚ�
		if (savedSim.equals(nowSim)) { // simû�иı�
			return;
		}
		Toast.makeText(context, "SIM�����", Toast.LENGTH_LONG).show();
		String safenumber = sp.getString("safenumber","");
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(safenumber,null,"�ֻ�SIM�������ı䣬�����µ��ֻ����롣",null,null);
	} 

}
