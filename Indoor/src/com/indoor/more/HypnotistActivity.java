package com.indoor.more;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.bean.Hypnotist;
import com.indoor.im.ui.BaseActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat") public class HypnotistActivity extends BaseActivity {

	private String musicAddress;
	private Uri musicUri;
	private int musicNum=1;
	private int oldmusicNum=1;
	private int musicTotal=0;
	private MediaPlayer mediaPlayer=null;
	private ImageView musicPhoto;
	private TextView musicName;
	private ImageView playlast;
	private ImageView play;
	private ImageView playnext;
	private TextView playtime;
	private TextView alltime;
	private SeekBar seekBar;
	private LinearLayout HypnotistLayout;
	private boolean isPaused=false,isDestory=false;
    private Handler handler;
    private String[] color={"#15ACA2","#F099DD","#AD1A8D","#419D2A","#3E025E","#D2795B","#4C3D47","#85051E","#85F01E",
    		"#EECF20","#B2BAF5","#E41607","#275D41"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hypnotist);
		initTopBarForLeft("���ߴ�ʦ");
		musicPhoto=(ImageView) findViewById(R.id.musicPhoto);
		musicName=(TextView) findViewById(R.id.musicName);
		playlast=(ImageView) findViewById(R.id.playback);
		play=(ImageView) findViewById(R.id.playstop);
		playnext=(ImageView) findViewById(R.id.playnext);
		playtime=(TextView) findViewById(R.id.playtime);
		alltime=(TextView) findViewById(R.id.alltime);
		seekBar=(SeekBar) findViewById(R.id.musicseekbar);
		HypnotistLayout=(LinearLayout) findViewById(R.id.hypnotist);
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			   if(isPaused){
                   mediaPlayer.start();
                   play.setImageResource(R.drawable.play);
                   isPaused=false;
               }else{
                   mediaPlayer.pause();
                   play.setImageResource(R.drawable.stop);
                   isPaused=true;
               }
			}
		});
		playlast.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				oldmusicNum=musicNum;
				musicNum--;
				searchMusic();
			}
		});
        playnext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				oldmusicNum=musicNum;
				musicNum++;
				searchMusic();
			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				 //��media��������Ϊ��ǰseekbar�Ľ���
                mediaPlayer.seekTo(arg0.getProgress());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				
			}
		});
		getCount();
	}

	private void searchMusic(){
		BmobQuery<Hypnotist> query = new BmobQuery<Hypnotist>();
		query.addWhereEqualTo("musicNum", musicNum);
		query.findObjects(this, new FindListener<Hypnotist>() {
	       
			@Override
	        public void onSuccess(List<Hypnotist> object) {
	        	if(object.size()>0 && object!=null){
	        		//����UIԪ��
	        		Random random = new Random();
	        		HypnotistLayout.setBackgroundColor(Color.parseColor(color[random.nextInt(color.length)]));
	        		musicName.setText(object.get(0).getMusicName());
	        		object.get(0).getMusicPhoto().loadImage(HypnotistActivity.this, musicPhoto);
	        		musicAddress=object.get(0).getMusic().getFileUrl(HypnotistActivity.this);
		        	musicUri=Uri.parse(musicAddress);
		        	playMusic();
	        	}	
	        	else
	        		//���ų�û������֮��õ��βŷ��ص�����
	        		musicNum=oldmusicNum;
	        }
	        
	        @Override
	        public void onError(int code, String msg) {
	           showTag("���������ȷ�������Ƿ���ͨ��", Effects.scale, R.id.hypnotist);
	        }
		});
	}
	
	//������Ӧ������
	private void playMusic() {
		stopCurrentMediaPlayer();
		mediaPlayer=new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(HypnotistActivity.this, musicUri);		
		}catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.prepareAsync();
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer arg0) {
				//����UIԪ��
				Date date = new Date(mediaPlayer.getDuration()); 
				SimpleDateFormat aFormat = new SimpleDateFormat("mm:ss"); 
				alltime.setText(aFormat.format(date));
				//������������󳤶�����Ϊý���ļ��Ĳ���ʱ�䣬�������ߵ�ͬ��
				seekBar.setMax(mediaPlayer.getDuration());
				mediaPlayer.start();	
				handler=new Handler(){
					public void handleMessage(Message msg){
						if(!isDestory){
							seekBar.setProgress(mediaPlayer.getCurrentPosition());
							Date date = new Date(mediaPlayer.getCurrentPosition()); 
							SimpleDateFormat aFormat = new SimpleDateFormat("mm:ss"); 
							playtime.setText(aFormat.format(date));
						}
					}
		        };//ʵ����Ϣ����
		        DelayThread delaythread=new DelayThread(100);
		        delaythread.start();
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				oldmusicNum=musicNum;
				musicNum++;
				searchMusic();
			}
		}); 
	}

	private class DelayThread extends Thread{
         int milliseconds;
         public DelayThread(int i){
             milliseconds=i;
         }
         public void run(){
        	 while(!isDestory){
                 try {
                     sleep(milliseconds);
                     //�������ֽ��ȶ�ȡƵ��
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 handler.sendEmptyMessage(0);
             }
         }      
     }
	 
	private void getCount(){
		String bql = "select count(*),* from Hypnotist";//��ѯHypnotist�����ܼ�¼�����������м�¼��Ϣ
		new BmobQuery<Hypnotist>().doSQLQuery(this,bql, new SQLQueryListener<Hypnotist>(){
			
			@Override
			public void done(BmobQueryResult<Hypnotist> result, BmobException e) {
				 if(e ==null){
			            musicTotal = result.getCount();//����õ����������ļ�¼��
			            Random random = new Random();
			    		//��Ϊ��Ŵ�1��ʼ��������Ҫ+1
			    		musicNum=random.nextInt(musicTotal)+1;
			    		searchMusic();
			         }else{
			        	  showTag("���������ȷ�������Ƿ���ͨ��", Effects.scale, R.id.hypnotist);
			        }
			}
		});
	}
	
	private void stopCurrentMediaPlayer() {
		if(mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
		}		
	}
	
	@Override
	protected void onDestroy() {
		isDestory=true;
		stopCurrentMediaPlayer();
		super.onDestroy();
	}
}
