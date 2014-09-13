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

package cn.geekduxu.xmanager.utils;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class AnimationUtil {
	
	public static void startRotateAnimation(Context context,View view){
		RotateAnimation ra = new RotateAnimation(-1, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(20);
		ra.setRepeatCount(17);
		ra.setRepeatMode(Animation.REVERSE);
		ra.setFillAfter(false);//����ִ������Ƿ�ͣ����ִ�����״̬ 
		view.startAnimation(ra);
		
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{0, 100, 100, 100}, -1);
	}

}
