package com.mridul.studyfocus;

import android.app.*;import android.app.usage.*;import android.content.*;import android.graphics.Color;import android.graphics.PixelFormat;import android.os.*;import android.provider.Settings;import android.view.*;import android.widget.*;import java.util.*;

public class FocusShieldService extends Service {
    Handler h=new Handler(Looper.getMainLooper()); View shield; WindowManager wm; String last="";
    Runnable check=()->{checkApp();h.postDelayed(this.check,700);};
    @Override public void onCreate(){super.onCreate();wm=(WindowManager)getSystemService(WINDOW_SERVICE);h.post(check);}
    String current(){try{UsageStatsManager u=(UsageStatsManager)getSystemService(USAGE_STATS_SERVICE);long now=System.currentTimeMillis();List<UsageStats> xs=u.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,now-5000,now);UsageStats best=null;for(UsageStats x:xs)if(best==null||x.getLastTimeUsed()>best.getLastTimeUsed())best=x;return best==null?"":best.getPackageName();}catch(Exception e){return "";}}
    void checkApp(){if(!Settings.canDrawOverlays(this)){remove();return;}String p=current();Set<String> blocked=getSharedPreferences("block",0).getStringSet("apps",Collections.emptySet());if(blocked.contains(p)&&!p.equals(getPackageName()))show();else remove();}
    void show(){if(shield!=null)return;LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setGravity(Gravity.CENTER);box.setPadding(36,36,36,36);box.setBackgroundColor(Color.rgb(11,15,13));TextView a=new TextView(this);a.setText("STAY FOCUSED");a.setTextColor(Color.rgb(139,227,90));a.setTextSize(28);a.setGravity(Gravity.CENTER);TextView b=new TextView(this);b.setText("This app is blocked during your Study Focus session.\n\nReturn to your study session when you are ready.");b.setTextColor(Color.WHITE);b.setTextSize(17);b.setGravity(Gravity.CENTER);b.setPadding(0,18,0,24);Button back=new Button(this);back.setText("Back to Study Focus");back.setTextColor(Color.rgb(11,15,13));back.setBackgroundColor(Color.rgb(139,227,90));back.setOnClickListener(v->{Intent i=new Intent(this,MainActivity.class);i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);startActivity(i);});box.addView(a);box.addView(b);box.addView(back);shield=box;int type=Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE;WindowManager.LayoutParams lp=new WindowManager.LayoutParams(-1,-1,type,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT);wm.addView(shield,lp);}
    void remove(){if(shield!=null){try{wm.removeView(shield);}catch(Exception ignored){}shield=null;}}
    @Override public int onStartCommand(Intent i,int f,int id){return START_STICKY;}
    @Override public void onDestroy(){h.removeCallbacks(check);remove();super.onDestroy();}
    @Override public android.os.IBinder onBind(Intent i){return null;}
}
