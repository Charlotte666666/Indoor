package com.indoor.im.ui;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.indoor.im.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NavigatorActivity extends Activity implements AMapLocationListener,
OnPoiSearchListener, OnRouteSearchListener{
	
	private LocationManagerProxy mLocationManagerProxy;
    private PoiSearch.Query query;// Poi��ѯ������
	private PoiSearch poiSearch;
	private List<PoiItem> poiItems;// poi����
	private PoiResult poiResult; // poi���صĽ��
	private LatLonPoint lp;// ��¼�û�λ��
	private FromAndTo fromAndTo;//���Ե���
	private RouteSearch routeSearch;
	private WalkRouteResult walkRouteResult;//walkroute���صĽ��
	private WalkRouteQuery query2;//����·����ѯ������
	private Camera camera;   
    private boolean preview  = false ;  
    private RelativeLayout top;
    private ImageView choose;
    private ImageView back;
    private TextView head;
    private SurfaceView surfaceView ;
    private RelativeLayout nave;
    private TextView navepath;
    private TextView stop;
    private LinearLayout mark;
    private TextView busname;
    private TextView busdistance;
    private LinearLayout path;
    private TextView pathname;
    private TextView pathdistance;
    private TextView pathcontent;
    private TextView pathtime;
    private ImageView naveimg;
    private boolean isChoose;//���Լ�¼�Ƿ�������
    private int whatbus;//����ȷ�ϵ���ʱ�Ĺ���վ��
    private SensorManager sensorManager;  //��ȡ���������ݣ����Զ�̬�ı�naveimg
    private MySensorEventListener sensorEventListener; 
    private String Cloum;
    private boolean only;//���Խ��������ʱ����������ʾ��Ϣ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_navigator);
		Intent intent = getIntent();  
		Cloum = intent.getStringExtra("Cloum"); 
		//��ȡ��Ӧ��������    
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
        sensorEventListener = new MySensorEventListener();  
        surfaceView=(SurfaceView) findViewById(R.id.surfaceView); 
        top=(RelativeLayout) findViewById(R.id.top);
        nave=(RelativeLayout) findViewById(R.id.nave);
        mark=(LinearLayout) findViewById(R.id.mark);
        path=(LinearLayout) findViewById(R.id.path);
        naveimg=(ImageView) findViewById(R.id.naveimg);
        //��ȡ�����ݺ�����ʾ���տ�ʼ���ɼ�
        mark.setVisibility(View.INVISIBLE);
        path.setVisibility(View.INVISIBLE);
        naveimg.setVisibility(View.INVISIBLE);
        choose=(ImageView) top.findViewById(R.id.choose);
        back=(ImageView) top.findViewById(R.id.back);
        head=(TextView) top.findViewById(R.id.head);
        navepath=(TextView) nave.findViewById(R.id.navepath);
        stop=(TextView) nave.findViewById(R.id.stop);
        busname=(TextView) mark.findViewById(R.id.busname);
        busdistance=(TextView) mark.findViewById(R.id.busdistance);
        pathname=(TextView) path.findViewById(R.id.pathname);
        pathdistance=(TextView) path.findViewById(R.id.pathdistance);
        pathcontent=(TextView) path.findViewById(R.id.pathcontent);
        pathtime=(TextView) path.findViewById(R.id.pathtime);
        surfaceView.getHolder().addCallback(new SurfaceViewCallback());  
        head.setText(Cloum);
        only=true;
        init();
        choose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(only){
				Intent intent=new Intent(NavigatorActivity.this,ChooseActivity.class);
				//��poi���ݴ��ݸ�����activity
				intent.putExtra("poiItems", (Serializable)poiItems);  
				startActivityForResult(intent,0);
				}
				else
					Toast.makeText(NavigatorActivity.this, "����2000�׷�Χ��û���������", Toast.LENGTH_LONG).show();
			}
		});
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        mark.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				isChoose=!isChoose;
				if(isChoose){
					busdistance.setBackgroundColor(Color.parseColor("#ff0099"));
					Toast.makeText(NavigatorActivity.this, "��ʼ����", Toast.LENGTH_SHORT).show();
					top.setVisibility(View.INVISIBLE);
					naveimg.setVisibility(View.VISIBLE);
					startNave();
				}	
				else{
					busdistance.setBackgroundColor(Color.parseColor("#00ff99"));
					top.setVisibility(View.VISIBLE);
					naveimg.setVisibility(View.INVISIBLE);
					pathtime.setText("���ͼ�꿪������");
			        pathdistance.setText(poiItems.get(whatbus).getDistance()+"��");
			        busdistance.setText(poiItems.get(whatbus).getDistance()+"��");
				}
			}
		});
        stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				isChoose=false;
				busdistance.setBackgroundColor(Color.parseColor("#00ff99"));
				top.setVisibility(View.VISIBLE);
				naveimg.setVisibility(View.INVISIBLE);
				pathtime.setText("���ͼ�꿪������");
		        pathdistance.setText(poiItems.get(whatbus).getDistance()+"��");
		        busdistance.setText(poiItems.get(whatbus).getDistance()+"��");
			}
		});
	}
	
	/**
     * ��ʼ��AMap����
     */
    private void init() {
    	mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //�˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
        //ע�����ú��ʵĶ�λʱ��ļ���������ں���ʱ�����removeUpdates()������ȡ����λ����
        //�ڶ�λ�������ں��ʵ��������ڵ���destroy()����     
        //����������ʱ��Ϊ-1����λֻ��һ��
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 1000, 1, this);
        mLocationManagerProxy.setGpsEnable(true);
    }
 
    @Override    
    protected void onResume()     
    {    
        //��ȡ���򴫸���    
        @SuppressWarnings("deprecation")
		Sensor orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);  
        //����������
        sensorManager.registerListener(sensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);    
        //��ȡ���ٶȴ�����    
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);    
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);    
        super.onResume();    
    }    
    
    /**
     * �˷��������
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener); 
        stopLocation();
    }
 
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
            //��ȡλ����Ϣ
            Double geoLat = amapLocation.getLatitude();
            Double geoLng = amapLocation.getLongitude();  
            lp=new LatLonPoint(geoLat, geoLng);
            doSearchQuery();
        }
    }
    
    @SuppressWarnings("deprecation")
	private void stopLocation() {
        if (mLocationManagerProxy != null) {
        	mLocationManagerProxy.removeUpdates(this);
        	mLocationManagerProxy.destory();
        }
        mLocationManagerProxy = null;
    }

    /**
	 * ��ʼ����poi����
	 */
	protected void doSearchQuery() {
		// ��һ��������ʾ�����ַ������ڶ���������ʾpoi�������ͣ�������������ʾpoi�������򣨿��ַ�������ȫ����
		query = new PoiSearch.Query("", Cloum, "");
		query.setPageSize(30);// ����ÿҳ��෵�ض�����poiitem
		query.setPageNum(0);// ���ò��һҳ
		if (lp != null) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			// ������������Ϊ��lp��ΪԲ�ģ�����Χ2000�׷�Χ
			poiSearch.setBound(new SearchBound(lp, 2000, true));
			poiSearch.searchPOIAsyn();// �첽����
		}
	}
	
	/**
	 * POI�����ص�����
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(query)) {
					poiResult = result;
					// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
					poiItems = poiResult.getPois();
					if ( poiResult.getPois().size()== 0){
						if(only){
							Toast.makeText(this, "����2000�׷�Χ��û���������", Toast.LENGTH_LONG).show();
							only=false;
						}
					}	  
					else{
						busname.setText(poiItems.get(0).toString());
						//doSearchPoiDetail(poiItems.get(0).getPoiId());
						busdistance.setText(poiItems.get(0).getDistance()+"��");
						pathname.setText(poiItems.get(0).toString());
						pathdistance.setText(poiItems.get(0).getDistance()+"��");
						pathcontent.setText(poiItems.get(0).getSnippet());
						//��ȡ�����ݺ�����ʾ
				        mark.setVisibility(View.VISIBLE);
				        path.setVisibility(View.VISIBLE);
					}
						
				}
			} 
		} 
	}
	
	private final class SurfaceViewCallback implements Callback {  
        /** 
         * surfaceView �������ɹ�����ô˷��� 
         */  
        @Override  
        public void surfaceCreated(SurfaceHolder holder) {  
            /*  
             * ��SurfaceView������֮�� ������ͷ 
             * ע���� android.hardware.Camera 
             */  
            camera = Camera.open();  
            camera.setDisplayOrientation(90);
            /* 
             * This method must be called before startPreview(). otherwise surfaceviewû��ͼ�� 
             */  
            try {  
                camera.setPreviewDisplay(holder);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            camera.startPreview();  
            preview = true;  
        }  
        @Override  
        public void surfaceChanged(SurfaceHolder holder,int format,int width,int height) {  
        }  
        /** 
         * SurfaceView ������ʱ�ͷŵ� ����ͷ 
         */  
        @Override  
        public void surfaceDestroyed(SurfaceHolder holder) {  
            if(camera != null) {  
                /* ������ͷ���ڹ�������ֹͣ�� */  
                if(preview) {  
                    camera.stopPreview();  
                    preview = false;  
                }  
                //���ע���˴˻ص�����release֮ǰ���ã�����release֮�󻹻ص���crash  
                camera.setPreviewCallback(null);  
                camera.release();  
            }  
        }        
    }  
	
	/**
	 * �鵥��poi����
	 * 
	 * @param poiId
	 */
	/*public void doSearchPoiDetail(String poiId) {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}*/
	
	/**
	 * POI����ص�
	 */
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		if (rCode == 0) {
			if (result != null) {
				// ����poi�Ľ��
			    String sb = result.getSnippet();
			    Toast.makeText(this, sb, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { 
		//resultCodeΪ�ش��ı�ǣ�����ChooseActivity�лش�����RESULT_OK
		case RESULT_OK: 
			//dataΪB�лش���Intent
		    Bundle b=data.getExtras();
		    //str��Ϊ�ش���ֵ
		    String str=b.getString("listenB");
		    whatbus=Integer.parseInt(str);
		    busname.setText(poiItems.get(whatbus).toString());
			busdistance.setText(poiItems.get(whatbus).getDistance()+"��");
			busdistance.setBackgroundColor(Color.parseColor("#00ff99"));
			pathname.setText(poiItems.get(whatbus).toString());
			pathdistance.setText(poiItems.get(whatbus).getDistance()+"��");
			pathcontent.setText(poiItems.get(whatbus).getSnippet());
			break;
		default:
		break;
		}
	}
	
	//��ʼ·�߹滮�����е���
	public void startNave() {
		fromAndTo=new FromAndTo(lp, poiItems.get(whatbus).getLatLonPoint());
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
		query2 = new WalkRouteQuery(fromAndTo,0);
		routeSearch.calculateWalkRouteAsyn(query2);
	}
	
	private final class MySensorEventListener implements SensorEventListener    
    {    
        //���Եõ�������ʵʱ���������ı仯ֵ    
        @SuppressWarnings("deprecation")
		@SuppressLint("NewApi") @Override    
        public void onSensorChanged(SensorEvent event)     
        {       
            //�õ������ֵ    
            if(event.sensor.getType()==Sensor.TYPE_ORIENTATION)    
            {    
                //float x = event.values[SensorManager.DATA_X];          
               // float y = event.values[SensorManager.DATA_Y];          
                float z = event.values[SensorManager.DATA_Z];      
                naveimg.setRotation(-z);
            }    
            //�õ����ٶȵ�ֵ    
            else if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)    
            {    
                //float x = event.values[SensorManager.DATA_X];          
                //float y = event.values[SensorManager.DATA_Y];          
                //float z = event.values[SensorManager.DATA_Z];     
            }    
            
        }    
        //��д�仯    
        @Override    
        public void onAccuracyChanged(Sensor sensor, int accuracy)     
        {    
        }    
    }    

	@Override
	public void onLocationChanged(Location location) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {
		
	}

	@Override
	// ·���滮�в���ģʽ�ص�����
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
        if(rCode == 0 && result != null && result.getPaths() != null && 
        		result.getPaths().size() > 0){
            walkRouteResult = result;
            WalkPath walkPath = walkRouteResult.getPaths().get(0);
            navepath.setText(walkPath.getSteps().get(0).getInstruction());
            pathtime.setText("��Լ"+(int)(walkPath.getDuration()/60)+"����");
            pathdistance.setText(walkPath.getDistance()+"��");
            busdistance.setText(walkPath.getDistance()+"��");
	    }
	}
	
}
