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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;
import cn.geekduxu.xmanager.activity.HomeActivity;
import cn.geekduxu.xmanager.receiver.SmsReceiver2;
import cn.geekduxu.xmanager.utils.StreamTools;

public class SplashActivity extends Activity {
	
	/** ������ҳ��Ϣ */
	private static final int ENTER_HOME_PAGE = 0;
	/** ��ʾ�����Ի�����Ϣ */
	private static final int SHOW_UPDATE_DIALOG = 1;
	/** URL������Ϣ */
	private static final int URL_ERROR = 2;
	/** ���������Ϣ */
	private static final int NETWORK_ERROR = 3;
	/** JSON����������Ϣ */
	private static final int JSON_ERROR = 4;

	private SharedPreferences sp;
	private TextView tvSplashVersion;
	private TextView tvUpdateInfo;
	
	/** �°汾��������Ϣ */
	private String description;
	/** �°汾�汾�� */
	private String version;
	/** �°汾�����ص�ַ */
	private String apkurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        //***************************************************
//        ContentObserver observer = new SmsReceiver2(new Handler(), getApplicationContext()); 
//        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, observer);
        //***************************************************
        
        //���Ӷ���Ч��
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(1000);
        findViewById(R.id.rl_splash).startAnimation(aa);
        
        //�õ�ʵ������������
        tvSplashVersion = (TextView) findViewById(R.id.tv_splash_version);
        tvSplashVersion.setText("�汾�ţ�" + getVersionName());
        
        tvUpdateInfo = (TextView) findViewById(R.id.tv_splash_updateinfo);
        
        //�õ�SharedPreferences���Ƿ��Զ������ֶ�
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if(sp.getBoolean("update", false)) {
        	//������� 
        	checkUpdate();
        } else {
        	//�ӳٽ�����ҳ
        	handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					enterHomePage();
				}
			}, 2500);
        }
    }
    
    
    private Handler handler = new Handler(){
    	@Override
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case SHOW_UPDATE_DIALOG: //��ʾ�����Ի���
				showUpdateDialog();
				break;
			case ENTER_HOME_PAGE: //������ҳ��
				enterHomePage();
				break;
			case URL_ERROR: //URL����
				Toast.makeText(SplashActivity.this, "URL����", 0).show();
				enterHomePage();
				break;
			case NETWORK_ERROR: //�����쳣
				Toast.makeText(SplashActivity.this, "�������", 0).show();
				enterHomePage();
				break;
			case JSON_ERROR: //json��������
				Toast.makeText(SplashActivity.this, "JSON����", 0).show();
				enterHomePage();
				break;
			}
    	}

    };
    
    /**
     * ������ҳ
     */
    private void enterHomePage() {
    	Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
    	startActivity(intent);
    	//�رյ�ǰҳ��
    	finish();	
    	overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in); 
    }
    
    /**
     * ��ʾ�����Ի���
     */
    private void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("������ʾ");
		builder.setMessage(description);
		builder.setPositiveButton("��������", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//����apk�����滻��װ
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//����sd��
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkurl, 
						Environment.getExternalStorageDirectory().getAbsolutePath()+"xManager-"+version+".apk",
						new AjaxCallBack<File>() {
							@Override
							public void onFailure(Throwable t, int errorNo, String strMsg) {
								Toast.makeText(SplashActivity.this, "���ظ���ʧ�ܣ����Ժ����ԡ�", Toast.LENGTH_LONG).show();
								super.onFailure(t, errorNo, strMsg);
								enterHomePage();
							}
							@Override
							public void onLoading(long count, long current) {
								super.onLoading(count, current);
								//��ʾ��ǰ���صİٷֱ�
								tvUpdateInfo.setText("���ؽ��ȣ�"+ (current*100/count) +"%");
							}
							@Override
							public void onSuccess(File file) {
								super.onSuccess(file);
								//���سɹ�����װapk
								installApk(file);
							}
							/**
							 * ��װapk
							 * @param file
							 */
							private void installApk(File file) {
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								intent.addCategory("android.intent.category.DEFAULT");
								intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
								startActivity(intent);
							}
						});
				} else {
					//������sd��
					Toast.makeText(SplashActivity.this, "û���ҵ�SD�������Ȱ�װSD����", Toast.LENGTH_LONG).show();
					return;
				}
			}
		});
//		builder.setCancelable(false);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				enterHomePage();
			}
		});
		builder.setNegativeButton("�ݲ�����", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHomePage();
			}
		});
		builder.show();
	}

	/**
     * �쳵�Ƿ�����µİ汾
     */
    private void checkUpdate() {
    	new Thread(){
    		public void run() {
    			Message msg = Message.obtain();
    			long time = System.currentTimeMillis();
    			try {
					URL url = new URL(getString(R.string.server_url));
					//����
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					try {
						conn.setRequestMethod("GET");
					} catch (ProtocolException e) {
						e.printStackTrace();
					}
					conn.setReadTimeout(4000);
					int code = conn.getResponseCode();
					//�������ɹ�
					if(code / 100 == 2){
						InputStream is = conn.getInputStream();
						//�ѽ��ת��Ϊ�ַ���
						String result = StreamTools.readFromStream(is);
						//����json
						JSONObject obj = new JSONObject(result);
						version = obj.getString("version");
						description = obj.getString("description");
						apkurl = obj.getString("apkurl");
						
						
						//�����Ƿ�����°汾
						if(getVersionName().equals(version)){
							//û���°汾
							msg.what = ENTER_HOME_PAGE;
						}else{
							//�����µİ汾
							msg.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					msg.what = URL_ERROR;
				} catch (IOException e) {
					msg.what = NETWORK_ERROR;
				} catch (JSONException e) {
					msg.what = JSON_ERROR;
				}finally{
					time = System.currentTimeMillis() - time;
					if(time < 2500){
						SystemClock.sleep(2500 - time);
					}
					handler.sendMessage(msg);
				}
    		}
    	}.start();
	}

	/**
     * �õ���ǰ�İ汾��
     */
    private String getVersionName(){
    	try {
    		//PackageManager������������APK
    		PackageManager pm = getPackageManager();
			//�õ���������嵥�ļ���Ϣ
    		PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
    }
}
