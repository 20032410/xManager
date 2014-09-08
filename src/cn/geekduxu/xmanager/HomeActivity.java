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

package cn.geekduxu.xmanager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	private static final String[] FUNCTION_NAMES = {
		"手机防盗", "通讯卫士", "软件管理", 
		"进程管理", "流量统计", "手机杀毒",
		"清理缓存", "高级工具", "设置中心"
	};

	public static final int[] FUNCTION_IMGS = {
		R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
		R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
		R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
	};

	private GridView listHome;
	private MAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// 实例化首页的功能列表
		listHome = (GridView) findViewById(R.id.list_home);
		adapter = new MAdapter();
		listHome.setAdapter(adapter);
		listHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 8:
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					break;
				}
			}
		});
	}

	/**
	 * 
	 */
	private class MAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return FUNCTION_NAMES.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
//			
			ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv = (TextView) view.findViewById(R.id.tv_item);
			
			tv.setText(FUNCTION_NAMES[position]);
			iv.setImageResource(FUNCTION_IMGS[position]);
			
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

}
