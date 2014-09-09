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

package cn.geekduxu.xmanager.ui;

import cn.geekduxu.xmanager.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * �Զ�����Ͽؼ���ʵ������ϵͳ��������ѡ����
 */
public class SettingItemView extends RelativeLayout {
	
	private CheckBox cbStatus;
	private TextView tvDesc;
	private TextView tvTiile;
	
	private String desc_on;
	private String desc_off;

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.geekduxu.xmanager", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.geekduxu.xmanager", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/cn.geekduxu.xmanager", "desc_off");
		tvTiile.setText(title);
		setStatus(false);
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * ��ʼ�������ļ�
	 */
	private void initView(Context context) {
		//���ز����ļ�
		View.inflate(context, R.layout.setting_item_view, this);
		cbStatus = (CheckBox) findViewById(R.id.cb_status);
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		tvTiile = (TextView) findViewById(R.id.tv_title);
		cbStatus.setClickable(false);
	}
	
	/** У����Ͽؼ��Ƿ�ѡ�� */
	public boolean isChecked(){
		return cbStatus.isChecked();
	}
	/** ������Ͽؼ���״̬ */
	public void setStatus(boolean checked){
		cbStatus.setChecked(checked);
		if(checked){
			tvDesc.setText(desc_on);
		}else{
			tvDesc.setText(desc_off);
		}
	}
}
