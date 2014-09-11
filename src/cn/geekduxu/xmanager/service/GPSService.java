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

package cn.geekduxu.xmanager.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 获得当前位置的服务
 */
public class GPSService extends Service {

	private LocationManager lm;
	private SharedPreferences sp;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyLocationListener();

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String proveder = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(proveder, 0, 0, listener);
	}

	@Override
	public void onDestroy() {
		lm.removeUpdates(listener);
		listener = null;
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			double longitude = location.getLongitude();// 经度
			double latitude = location.getLatitude();// 纬度
			double accuracy = location.getAccuracy();// 精度
			
//			转换火星坐标
			try {
				ModifyOffset offset = ModifyOffset.getInstance(getAssets().open("axisoffset.dat"));
				PointDouble point = offset.s2c(new PointDouble(longitude, latitude));
				longitude = point.x;
				latitude = point.y;
			} catch (Exception e) { }
			
			String loc = new StringBuilder().append("longitude:")
					.append(longitude).append("\nlatitude:")
					.append(latitude).append("\naccuracy:")
					.append(accuracy).toString();
			
			Editor edit = sp.edit();
			edit.putString("lastlocation", loc);
			edit.commit();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

	}

}
