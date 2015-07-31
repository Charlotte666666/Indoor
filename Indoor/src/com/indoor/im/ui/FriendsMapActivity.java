package com.indoor.im.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.CustomApplcation;
import com.indoor.im.R;
import com.indoor.im.bean.User;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.Mark;
import android.content.Intent;
import android.os.Bundle;


public class FriendsMapActivity extends ActivityBase{
	
	private MapView mMapView; 
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	private BDLocationListener myListener;
	private MyLocationData locData;
	private List<User> allFriends;
	private int num;
	private LatLng point;
	//����Maker����� 
	private	Mark mark;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_map);
		initTopBarForLeft("���ѷֲ�");
		 //��ȡ��ͼ�ؼ�����  
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();  
        //��ͨ��ͼ  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);  
        initlocation();
	}
	
	private void initlocation() {
		myListener = new MyLocationListener();
		mLocationClient = new LocationClient(getApplicationContext());  //����LocationClient��
		mLocationClient.registerLocationListener( myListener );    //ע���������
		// ������λͼ�� 
        mBaiduMap.setMyLocationEnabled(true); 
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
		option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		//option.setScanSpan(1000);//���÷���λ����ļ��ʱ��Ϊ1000ms,��������һ�ζ�λ
		option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
		option.setNeedDeviceDirect(true);//���صĶ�λ��������ֻ���ͷ�ķ���
		mLocationClient.setLocOption(option);
		mLocationClient.start();//��ʼ��λ
		mLocationClient.requestLocation();
		//����BaiduMap�����setOnMarkerClickListener��������marker����ļ���
	  	mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker arg0) {
		
				//�Ƚ�����ѵ���ϸ����ҳ��
				Intent intent =new Intent(FriendsMapActivity.this,SetMyInfoActivity.class);
				intent.putExtra("from", "other");
				intent.putExtra("username", arg0.getTitle());
				startAnimActivity(intent);
				return false;
			}
		});
	}

	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
    	mLocationClient.stop();//ֹͣ��λ	
    	// ������Ҫ��λͼ��ʱ�رն�λͼ��  
    	mBaiduMap.setMyLocationEnabled(false);
        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onDestroy();  
    }  
	
    @Override  
    protected void onResume() {  
        super.onResume();  
        //��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onResume();  
    }  
    
    @Override  
    protected void onPause() {  
        super.onPause();  
        //��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���  
        mMapView.onPause();  
    }  
    
    //��λ�����Ľӿ�
  	public class MyLocationListener implements BDLocationListener{
  		@Override
  		public void onReceiveLocation(BDLocation location) {
  			if (location == null)
  		            return ;
  			//����ǰλ��������Ϣ�浽����
  	    	//String position=location.getAddrStr();
  			// ���춨λ����  
  			locData = new MyLocationData.Builder()  
  			    .accuracy(location.getRadius())  
  			    // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360  
  			    .direction(100).latitude(location.getLatitude())  
  			    .longitude(location.getLongitude()).build(); 
  			// ���ö�λ����  
  			mBaiduMap.setMyLocationData(locData);  
  			// ���ö�λͼ������ã���λģʽ���Ƿ���������Ϣ���û��Զ��嶨λͼ�꣩    
  			MyLocationConfiguration config = new MyLocationConfiguration(null, true, null);  
  			mBaiduMap.setMyLocationConfigeration(config);  
  			//�趨���ĵ����� 
  		    LatLng cenpt = new LatLng(location.getLatitude(),location.getLongitude()); 
  		    //�����ͼ״̬
  		    MapStatus mMapStatus = new MapStatus.Builder()
  		        .target(cenpt)
  		        .zoom(15)
  		        .build();
  		    //����MapStatusUpdate�����Ա�������ͼ״̬��Ҫ�����ı仯
  		    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
  		    //�ı��ͼ״̬
  		    mBaiduMap.setMapStatus(mMapStatusUpdate);
  		    setFriendMask();
  		}
  	}
  	
  	public void setFriendMask(){
  		Map<String,BmobChatUser> users = CustomApplcation.getInstance().getContactList();
		final List<BmobChatUser> friends=CollectionUtils.map2list(users);
		allFriends=new ArrayList<User>();
		num=0;
		for(int i=0;i<friends.size();i++){
			BmobQuery<User> query = new BmobQuery<User>();
			query.addWhereEqualTo("objectId", friends.get(i).getObjectId());
			query.findObjects(this, new FindListener<User>() {
			    @Override
			    public void onSuccess(List<User> object) {
			    	allFriends.add(object.get(0));
			    	num++;
			    	if(num==friends.size()){
			    		for(int j=0;j<allFriends.size();j++){ 
			    			point = new LatLng(allFriends.get(j).getLocation().getLatitude(), 
			    					allFriends.get(j).getLocation().getLongitude());  
			    			//����Maker����� 
			    	  		mark=new Mark(FriendsMapActivity.this);
			    	  	    //����Markerͼ��  
			    	  		mark.setUsername(allFriends.get(j).getUsername());
			    			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(mark); 
			    			//����MarkerOption�������ڵ�ͼ�����Marker  
			    			OverlayOptions option = new MarkerOptions()  
			    			    .position(point)
			    			    .zIndex(9)
			    			    .icon(bitmap);  
			    			//�ڵ�ͼ�����Marker������ʾ  
			    			Marker marker = (Marker) (mBaiduMap.addOverlay(option));
			    			//Title��Username��Ӧ
			    			marker.setTitle(allFriends.get(j).getUsername());
			    		}	
			    	}
			    }
			    @Override
			    public void onError(int code, String msg) {
			    	showTag(msg,Effects.slideIn,R.id.bmapView);
			    }
			});
		}
  	}
  	
}
