package com.mridul.studyfocus;

import android.app.*;
import android.content.*;
import android.os.*;

public class FocusTimerService extends Service {
    public static final String ACTION_START="START", ACTION_STOP="STOP", ACTION_PAUSE="PAUSE", ACTION_RESUME="RESUME", ACTION_ADD="ADD";
    static final String CH="study_focus_timer"; static final int ID=404;
    Handler h=new Handler(Looper.getMainLooper()); String tag="Focus"; long endAt; boolean paused;
    Runnable loop=()->{update();h.postDelayed(this.loop,1000);};
    @Override public void onCreate(){super.onCreate();createChannel();}
    void createChannel(){NotificationManager nm=getSystemService(NotificationManager.class);if(Build.VERSION.SDK_INT>=26)nm.createNotificationChannel(new NotificationChannel(CH,getString(com.mridul.studyfocus.R.string.channel_name),NotificationManager.IMPORTANCE_LOW));}
    Notification n(String title,String body){Intent i=new Intent(this,MainActivity.class);PendingIntent pi=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);return new Notification.Builder(this,CH).setSmallIcon(R.drawable.ic_launcher).setContentTitle(title).setContentText(body).setContentIntent(pi).setOngoing(!paused).setCategory(Notification.CATEGORY_PROGRESS).setOnlyAlertOnce(true).setProgress(100,0,false).build();}
    void startTimer(Intent i){tag=i.getStringExtra("tag");long sec=Math.max(1,i.getLongExtra("seconds",2700));endAt=System.currentTimeMillis()+sec*1000;paused=false;save();startForeground(ID,n("Focus session",tag+" • "+fmt(sec)));h.removeCallbacks(loop);h.post(loop);}
    void save(){getSharedPreferences("timer",0).edit().putString("tag",tag).putLong("end",endAt).putBoolean("paused",paused).apply();}
    void update(){long left=paused?getSharedPreferences("timer",0).getLong("left",0):(endAt-System.currentTimeMillis())/1000;if(left<=0&&!paused){notifyDone();stopSelf();return;}NotificationManager nm=getSystemService(NotificationManager.class);nm.notify(ID,n("Focus session",tag+" • "+fmt(left)+" remaining"));}
    void notifyDone(){NotificationManager nm=getSystemService(NotificationManager.class);nm.notify(405,new Notification.Builder(this,CH).setSmallIcon(R.drawable.ic_launcher).setContentTitle("Focus session complete").setContentText("Great work — your "+tag+" session is finished.").setAutoCancel(true).build());}
    String fmt(long s){s=Math.max(0,s);return String.format(java.util.Locale.US,"%02d:%02d",s/60,s%60);}
    @Override public int onStartCommand(Intent i,int flags,int id){if(i==null)return START_STICKY;String a=i.getAction();if(ACTION_START.equals(a))startTimer(i);else if(ACTION_STOP.equals(a)){h.removeCallbacks(loop);stopSelf();}else if(ACTION_PAUSE.equals(a)){long l=Math.max(0,(endAt-System.currentTimeMillis())/1000);getSharedPreferences("timer",0).edit().putLong("left",l).apply();paused=true;save();update();}else if(ACTION_RESUME.equals(a)){long l=getSharedPreferences("timer",0).getLong("left",0);endAt=System.currentTimeMillis()+l*1000;paused=false;save();update();}else if(ACTION_ADD.equals(a)){endAt+=300000;save();}return START_STICKY;}
    @Override public void onDestroy(){h.removeCallbacks(loop);super.onDestroy();}
    @Override public android.os.IBinder onBind(Intent i){return null;}
}
