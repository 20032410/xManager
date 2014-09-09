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

package cn.geekduxu.xmanager.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * 支持左右滑动翻页
 */
public abstract class BaseSetupActivity extends Activity {

	// 手势识别器
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

						// 屏蔽斜滑
						if (Math.abs(x1-x2) < Math.abs(y1-y2) || Math.abs(y1-y2) > 150) {
						}else if (e2.getRawX() - e1.getRawX() > 150) {
							// 显示上一个页面
							pre(null);
						} else if (e1.getRawX() - e2.getRawX() > 150) {
							// 显示下一个页面
							next(null);
						}
						return true;
					}
				});
	}

	/** 下一个页面 */
	public abstract void next(View v);
	/** 上一个页面 */
	public abstract void pre(View v);

	// 使用手势识别器
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
