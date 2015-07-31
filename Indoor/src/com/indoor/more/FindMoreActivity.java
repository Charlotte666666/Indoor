package com.indoor.more;

import com.indoor.im.R;
import com.indoor.im.ui.BaseActivity;
import com.indoor.more.view.LabelView;
import com.indoor.more.view.LabelView.OnItemClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

public class FindMoreActivity extends BaseActivity {
	
	private LabelView mLabelView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_more);
		initTopBarForLeft("����");
		mLabelView = (LabelView) findViewById(R.id.lv);
		mLabelView.setLabels(new String[] {"Ƭ��","ÿ��һ��","����", "ƽ������", "MONO", "һ��", "emo",
				"���ߴ�ʦ","how_old"});
		mLabelView.setColorSchema(new int[] {Color.DKGRAY, Color.CYAN, Color.GREEN, Color.LTGRAY, 
				Color.MAGENTA, Color.RED});
		mLabelView.setSpeeds(new int[][] {{1,2},{1,1},{2,1},{2,3},{3,1},{3,4},{4,1},{4,5}});
		mLabelView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(int index, String label) {
				if(label.equals("ÿ��һ��"))
					startActivity(new Intent(FindMoreActivity.this, DailyArticleActivity.class));
				if(label.equals("how_old"))
					startActivity(new Intent(FindMoreActivity.this,HowOldActivity.class));
				if(label.equals("���ߴ�ʦ"))
					startActivity(new Intent(FindMoreActivity.this,HypnotistActivity.class));
				if(label.equals("Ƭ��"))
					startActivity(new Intent(FindMoreActivity.this,AmomentActivity.class));
			}
		});
	}
}
