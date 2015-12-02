package com.lr.fragment;

import java.util.ArrayList;

import com.example.shuichang.R;

import com.lr.agriculture.video.VideoView;
import com.lr.javaBean.DataALL;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MonitiorContr extends Fragment implements OnClickListener {
	private LinearLayout moniti;
	private ArrayList<DataALL> alls;
	private VideoView videoView;
	private ArrayList<VideoView> videoViews;
	private ArrayList<ArrayList<VideoView>> manyArrayList = new ArrayList<ArrayList<VideoView>>();
	private ArrayList<VideoView> manyviews = new ArrayList<VideoView>();
	private MyBroadCast myBroadCast;

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		alls = (ArrayList<DataALL>) bundle.getSerializable("name");
		View view = inflater.inflate(R.layout.monit, null);
		videoViews = new ArrayList<VideoView>();
		moniti = (LinearLayout) view.findViewById(R.id.ll);
		for (int i = 0; i < alls.size(); i++) {
			View view2 = getActivity().getLayoutInflater().inflate(
					R.layout.monicontent, null);

			LinearLayout ll = (LinearLayout) view2.findViewById(R.id.num);
			ll.setTag(i + "");
			ll.setOnClickListener(this);

			LinearLayout view3 = (LinearLayout) view2
					.findViewById(R.id.shiping);
			TextView name = (TextView) view2.findViewById(R.id.name);
			TextView no = (TextView) view2.findViewById(R.id.no);
			name.setText(alls.get(i).getPondName());
			no.setText(alls.get(i).getDeviceID());
			String videoName = alls.get(i).getVedioName();
			ArrayList<VideoView> manyviews = new ArrayList<VideoView>();

			for (int j = 0; j < videoName.split(",").length; j++) {

				videoView = new VideoView(getActivity(),
						videoName.split(",")[j]);
				videoViews.add(videoView);
				view3.addView(videoView.getView());
				manyviews.add(videoView);
			}
			manyArrayList.add(manyviews);
			moniti.addView(view2);
		}
		return view;

	}

	@Override
	public void onClick(View v) {
		int i = Integer.parseInt(v.getTag().toString());
		View childAt = moniti.getChildAt(i);
		ImageView imag = (ImageView) childAt.findViewById(R.id.ima1);
		View view = childAt.findViewById(R.id.shiping);
		LinearLayout ll = (LinearLayout) view;
		if (view.getVisibility() == View.GONE) {
			if (ll.getChildCount() == 0) {
				ArrayList<VideoView> manyVideoView = manyArrayList.get(i);
				for (int j = 0; j < manyVideoView.size(); j++) {

					View view2 = manyVideoView.get(j).getView();
					ImageView imageView = (ImageView) view2
							.findViewById(R.id.ima);
					View view3 = view2.findViewById(R.id.ControlLayout);
					ProgressBar progressBar = (ProgressBar) view2
							.findViewById(R.id.liveProgressBar);
					if (progressBar.getVisibility() == View.VISIBLE) {
						progressBar.setVisibility(View.GONE);

					}
					if (imageView.getVisibility() == ImageView.GONE) {
						imageView.setVisibility(View.VISIBLE);
					}
					if (view3.getVisibility() == View.VISIBLE) {
						view3.setVisibility(View.GONE);
					}
					ll.addView(view2);

				}
				imag.setImageResource(R.drawable.top);
				view.setVisibility(View.VISIBLE);

			}

			//
			//
			//
			//
			// // if(i==0&&manyviews.size()>2){
			// // for (int j = 0; j <manyviews.size(); j++) {
			// // View view2 = manyviews.get(j).getView();
			// // ImageView imageView = (ImageView)
			// view2.findViewById(R.id.ima);
			// // View view3 = view2.findViewById(R.id.ControlLayout);
			// // ProgressBar
			// progressBar=(ProgressBar)view2.findViewById(R.id.liveProgressBar);
			// // if(progressBar.getVisibility()==View.VISIBLE){
			// // progressBar.setVisibility(View.GONE);
			// //
			// // }
			// // if (imageView.getVisibility() == ImageView.GONE) {
			// // imageView.setVisibility(View.VISIBLE);
			// // }
			// // if (view3.getVisibility() == View.VISIBLE) {
			// // view3.setVisibility(View.GONE);
			// // }
			// // ll.addView(view2);
			// //
			// // }
			// // view.setVisibility(View.VISIBLE);
			// // return;
			// // }
			//
			//
			// View view2 =
			// videoViews.get(i+(manyviews.size()==0?0:(manyviews.size()-1))).getView();
			// ImageView imageView = (ImageView) view2.findViewById(R.id.ima);
			// View view3 = view2.findViewById(R.id.ControlLayout);
			// ProgressBar
			// progressBar=(ProgressBar)view2.findViewById(R.id.liveProgressBar);
			// if(progressBar.getVisibility()==View.VISIBLE){
			// progressBar.setVisibility(View.GONE);
			//
			// }
			// if (imageView.getVisibility() == ImageView.GONE) {
			// imageView.setVisibility(View.VISIBLE);
			// }
			// if (view3.getVisibility() == View.VISIBLE) {
			// view3.setVisibility(View.GONE);
			// }
			// ll.addView(view2);
			// }
			// imag.setImageResource(R.drawable.top);
			// view.setVisibility(View.VISIBLE);
			// } else {
		} else {
			view.setVisibility(View.GONE);
			// if(i==0&&manyviews.size()>2){
			// for (int j = 0; j <manyviews.size(); j++) {
			// manyviews.get(j).stopBtnOnClick();
			// ll.removeView(manyviews.get(j).getView());
			// imag.setImageResource(R.drawable.blo);
			//
			// }
			// return;
			//
			// }
			// videoViews.get(i+(manyviews.size()==0?0:(manyviews.size()-1))).stopBtnOnClick();
			// ll.removeView(videoViews.get(i+(manyviews.size()==0?0:(manyviews.size()-1))).getView());
			// imag.setImageResource(R.drawable.blo);
			ArrayList<VideoView> arrayList = manyArrayList.get(i);
			for (int j = 0; j < arrayList.size(); j++) {
				arrayList.get(j).stopBtnOnClick();
				ll.removeView(arrayList.get(j).getView());
				imag.setImageResource(R.drawable.blo);

			}

		}

	}

	class MyBroadCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getStringExtra("flag").equals("min")) {
				for (int i = 0; i < videoViews.size(); i++) {
					View view = videoViews.get(i).getView();
					View cont = view.findViewById(R.id.ControlLayout);
					if (cont.getVisibility() == View.VISIBLE) {
						cont.setVisibility(View.GONE);
					}
					videoViews.get(i).NosendBromin();
				}
			} else if (intent.getStringExtra("flag").equals("max")) {
				for (int i = 0; i < videoViews.size(); i++) {
					VideoView videoView2 = videoViews.get(i);
					videoView2.NoSendBroMax();
				}

			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		myBroadCast = new MyBroadCast();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("flag");
		getActivity().registerReceiver(myBroadCast, intentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		getActivity().unregisterReceiver(myBroadCast);

	}

}
