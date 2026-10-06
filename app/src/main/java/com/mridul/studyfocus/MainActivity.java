package com.mridul.studyfocus;

import android.Manifest;import android.app.*;import android.content.*;import android.content.pm.PackageManager;import android.net.Uri;import android.os.*;import android.provider.Settings;import android.webkit.*;import android.graphics.Color;

public class MainActivity extends Activity {
    WebView webView;
    @Override protected void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.rgb(11,15,13));getWindow().setNavigationBarColor(Color.rgb(11,15,13));if(!getSharedPreferences("sf",0).getBoolean("onboarded",false)){startActivity(new Intent(this,OnboardingActivity.class));finish();return;}setup();}
    void setup(){webView=new WebView(this);WebSettings s=webView.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setDatabaseEnabled(true);s.setAllowFileAccess(true);s.setAllowContentAccess(true);s.setBuiltInZoomControls(false);s.setDisplayZoomControls(false);webView.setWebViewClient(new WebViewClient());webView.setWebChromeClient(new WebChromeClient());webView.addJavascriptInterface(new NativeBridge(this),"Android");webView.loadUrl("file:///android_asset/index.html");setContentView(webView);if(Build.VERSION.SDK_INT>=33&&checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED)requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},100);}
    public class NativeBridge {Context c;NativeBridge(Context c){this.c=c;}
        @JavascriptInterface public void startFocus(String tag,int seconds){Intent i=new Intent(c,FocusTimerService.class).setAction(FocusTimerService.ACTION_START).putExtra("tag",tag).putExtra("seconds",seconds);startForegroundService(i);startShield();}
        @JavascriptInterface public void stopFocus(){startService(new Intent(c,FocusTimerService.class).setAction(FocusTimerService.ACTION_STOP));stopService(new Intent(c,FocusShieldService.class));}
        @JavascriptInterface public void pauseFocus(){startService(new Intent(c,FocusTimerService.class).setAction(FocusTimerService.ACTION_PAUSE));}
        @JavascriptInterface public void resumeFocus(){startService(new Intent(c,FocusTimerService.class).setAction(FocusTimerService.ACTION_RESUME));}
        @JavascriptInterface public void addFive(){startService(new Intent(c,FocusTimerService.class).setAction(FocusTimerService.ACTION_ADD));}
        @JavascriptInterface public void startShield(){if(Settings.canDrawOverlays(c))startForegroundService(new Intent(c,FocusShieldService.class));}
        @JavascriptInterface public void openAppPicker(){startActivity(new Intent(c,AppPickerActivity.class));}
        @JavascriptInterface public void openUsageAccess(){try{startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));}catch(Exception ignored){}}
        @JavascriptInterface public void openOverlayAccess(){try{startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())));}catch(Exception ignored){}}
        @JavascriptInterface public boolean hasOverlay(){return Settings.canDrawOverlays(c);}
    }
    @Override public void onBackPressed(){if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed();}
}
