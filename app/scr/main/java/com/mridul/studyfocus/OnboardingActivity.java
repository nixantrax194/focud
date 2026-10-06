package com.mridul.studyfocus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.net.Uri;
import android.view.Gravity;
import android.widget.*;
import android.graphics.Color;
import android.graphics.Typeface;

public class OnboardingActivity extends Activity {
    private LinearLayout root;
    private int green=Color.rgb(139,227,90), bg=Color.rgb(11,15,13), surface=Color.rgb(21,26,23);
    @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(bg); getWindow().setNavigationBarColor(bg); showWelcome();}
    TextView text(String s,int sp){TextView t=new TextView(this);t.setText(s);t.setTextColor(Color.rgb(244,247,243));t.setTextSize(sp);t.setPadding(0,8,0,8);return t;}
    Button btn(String s){Button b=new Button(this);b.setText(s);b.setTextColor(bg);b.setTextSize(16);b.setAllCaps(false);b.setBackgroundColor(green);LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,58);p.setMargins(0,10,0,0);b.setLayoutParams(p);return b;}
    void base(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,40,28,28);root.setGravity(Gravity.CENTER_HORIZONTAL);root.setBackgroundColor(bg);setContentView(root);}
    void showWelcome(){base(); TextView logo=text("◷",72);logo.setTextColor(green);root.addView(logo);TextView h=text("Study Focus",34);h.setTypeface(Typeface.DEFAULT,Typeface.BOLD);root.addView(h);TextView p=text("A focused study companion for your daily sessions, exams and goals.",18);p.setGravity(Gravity.CENTER);root.addView(p);Space sp=new Space(this);root.addView(sp,new LinearLayout.LayoutParams(1,0,1));Button next=btn("Get started");root.addView(next);next.setOnClickListener(v->showPermissions());}
    void showPermissions(){base();root.addView(text("Set up Focus Mode",28));root.addView(text("For the full experience, Android can show study notifications and place a focus shield over distracting apps while a session is running.",17));Button n=btn("Enable notifications");root.addView(n);n.setOnClickListener(v->{if(android.os.Build.VERSION.SDK_INT>=33)requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"},20);});Button usage=btn("Enable app usage access");root.addView(usage);usage.setOnClickListener(v->{try{startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));}catch(Exception ignored){}});Button overlay=btn("Enable focus shield");root.addView(overlay);overlay.setOnClickListener(v->{try{startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())));}catch(Exception ignored){}});Space sp=new Space(this);root.addView(sp,new LinearLayout.LayoutParams(1,0,1));Button done=btn("Continue to Study Focus");root.addView(done);done.setOnClickListener(v->{getSharedPreferences("sf",0).edit().putBoolean("onboarded",true).apply();startActivity(new Intent(this,MainActivity.class));finish();});}
}
