package map;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.huoyuyunshu.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class GpsMapAcitivty extends FragmentActivity {
	MapView mMapView = null;
	private float latitude;
	private float longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.gpsmap);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		BaiduMap mBaiduMap = mMapView.getMap();
		String lgtLatStr = getIntent().getStringExtra("gpsData");
		if (lgtLatStr != null) {
			String[] lgtLatArr = lgtLatStr.split(",");
			if (lgtLatArr != null && lgtLatArr.length == 2) {
				longitude = Float.parseFloat(lgtLatArr[0]);
				latitude = Float.parseFloat(lgtLatArr[1]);

				// 定义Maker坐标点
				LatLng point = new LatLng(latitude, longitude);
				// 构建Marker图标
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.marke);
				// 构建MarkerOption，用于在地图上添加Marker
				OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);

				// 在地图上添加Marker，并显示
				mBaiduMap.addOverlay(option);
				

			    MapStatus ms = new MapStatus.Builder().target(point).zoom(15).build();
		       
		        // 定义点聚合管理类ClusterManager
		        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));

			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

}
