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

package cn.geekduxu.xmanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * ֧�����һ�����ҳ
 */
public abstract class BaseSetupActivity extends Activity {

	// ����ʶ����
	private GestureDetector detector;
	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		detector = new GestureDetector(BaseSetupActivity.this,
				new SimpleOnGestureListener() {

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						float x1 = e1.getRawX();
						float x2 = e2.getRawX();
						float y1 = e1.getRawY();
						float y2 = e2.getRawY();

						// ����б��
						if (Math.abs(x1-x2) < Math.abs(y1-y2) || Math.abs(y1-y2) > 150) {
						}else if (e2.getRawX() - e1.getRawX() > 150) {
							// ��ʾ��һ��ҳ��
							pre(null);
						} else if (e1.getRawX() - e2.getRawX() > 150) {
							// ��ʾ��һ��ҳ��
							next(null);
						}
						return true;
					}
				});
	}

	/** ��һ��ҳ�� */
	public abstract void next(View v);
	/** ��һ��ҳ�� */
	public abstract void pre(View v);

	// ʹ������ʶ����
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
