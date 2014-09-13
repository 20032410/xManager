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

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import cn.geekduxu.xmanager.R;
import cn.geekduxu.xmanager.db.BlackListDao;
import cn.geekduxu.xmanager.domain.BlackListInfo;

public class CallAndSmsSafeActivity extends Activity {
	
	private ListView lvCallSmsSafe;
	private List<BlackListInfo> infos;
	private BlackListDao dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callandsmssafe);
		dao = new BlackListDao(CallAndSmsSafeActivity.this);
		infos = dao.queryAll();
		lvCallSmsSafe = (ListView) findViewById(R.id.lv_callsms_safe);
		lvCallSmsSafe.setAdapter(adapter);
	}
	
	private BaseAdapter adapter = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView != null){
				view = convertView;
			}else{
				view = View.inflate(CallAndSmsSafeActivity.this, R.layout.list_item_callandsms, null);
			}
			TextView tvBlackNumber = (TextView) view.findViewById(R.id.tv_black_number);
			TextView tvBlackMode = (TextView) view.findViewById(R.id.tv_black_mode);
			tvBlackNumber.setText(infos.get(position).getNumber());
			tvBlackMode.setText(getModeName(infos.get(position).getMode()));
			return view;
		}
		private String getModeName(String mode) {
			if("1".equals(mode)){
				return "拦截电话"; 
			}
			if("2".equals(mode)){
				return "拦截短信"; 
			}
			if("3".equals(mode)){
				return "拦截电话和短信"; 
			}
			return "数据出错";
		}
		@Override
		public int getCount() {
			return infos.size();
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
	}; 

}
