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

package cn.geekduxu.xmanager.ui;

import cn.geekduxu.xmanager.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义组合控件，实现类似系统设置栏的选择项
 */
public class SettingItemView extends RelativeLayout {
	
	private CheckBox cbStatus;
	private TextView tvDesc;
	private TextView tvTiile;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 初始化布局文件
	 */
	private void initView(Context context) {
		//加载布局文件
		View.inflate(context, R.layout.setting_item_view, this);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		tvTiile = (TextView) findViewById(R.id.tv_title);
		cbStatus.setClickable(false);
	}
	
	/** 校验组合控件是否选中 */
	public boolean isChecked(){
		return cbStatus.isChecked();
	}
	/** 设置组合控件的状态 */
	public void setStatus(boolean checked){
		cbStatus.setChecked(checked);
	}
	/** 设置组合控件的描述信息 */
	public void setDesc(String desc){
		tvDesc.setText(desc);
	}
}
