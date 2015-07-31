package com.indoor.im.ui;

import android.os.Bundle;
import com.indoor.im.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.amap.api.services.core.PoiItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChooseActivity extends Activity{
	
	private ImageView back;
	private List<PoiItem> lists;
	private List<Map<String, Object>> list;
	private ListView listView;
	private SimpleAdapter simple_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose);
		back=(ImageView) findViewById(R.id.back);
		// ƥ�䲼���ļ��е�ListView�ؼ�
		listView = (ListView) findViewById(R.id.listview);    
        getData();
    	// ����SimpleAdapter������
        simple_adapter = new SimpleAdapter(this,
        		list, R.layout.list_item,
				new String[] { "busname", "buscontent","busdistance" }, new int[]
						{ R.id.busname,R.id.buscontent,R.id.busdistance});
		listView.setAdapter(simple_adapter);
		// ����ListView��Ԫ�ر�ѡ��ʱ���¼����������
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				final int what=position;
				//���öԻ������ 
				new AlertDialog.Builder(ChooseActivity.this).setTitle(lists
						.get(position).toString()) 
				//������ʾ������
			    .setMessage(lists.get(position).getSnippet()) 
			    //���ȷ����ť 
			    .setPositiveButton("ȷ��",new DialogInterface.OnClickListener() { 
			         
			    	@Override  
			    	//ȷ����ť����Ӧ�¼�
			        public void onClick(DialogInterface dialog, int which) { 
			    		 Intent aintent = new Intent(ChooseActivity.this, MainActivity.class);
			    		 aintent.putExtra("listenB",what+"");
			    		 setResult(RESULT_OK,aintent); 
			             finish();  
			         }  
			     //��ӷ��ذ�ť
			     }).setNegativeButton("����",new DialogInterface.OnClickListener() {  
			
			         @Override  
			         //��Ӧ�¼�  
			         public void onClick(DialogInterface dialog, int which) {
			         }  
			  
			     }).show();//�ڰ�����Ӧ�¼�����ʾ�˶Ի���  
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	// ����SimpleAdapter���ݼ�
	@SuppressWarnings("unchecked")
	private void getData() { 
		Intent intent = getIntent();  
        lists = (List<PoiItem>)intent.getSerializableExtra("poiItems"); 
		list = new ArrayList<Map<String, Object>>();
		for(int i=0;i<lists.size();i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("busname", lists.get(i).toString());
			map.put("buscontent", lists.get(i).getSnippet());
			map.put("busdistance", lists.get(i).getDistance()+"��");
			list.add(map);
		}
	}
}
