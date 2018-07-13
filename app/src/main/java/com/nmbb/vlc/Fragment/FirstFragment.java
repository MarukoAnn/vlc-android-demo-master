package com.nmbb.vlc.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.nmbb.vlc.R;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class FirstFragment extends Fragment {
    private View view;
    private ProgressDialog dialog;
    WebView webView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg1, container, false);


        final WebView webView = view.findViewById(R.id.web_item);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;// 返回false
//            }
//        });
        WebSettings webSettings = webView.getSettings();
  //       让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
//        webSettings.setDefaultTextEncodingName("UTF-8");
////         让JavaScript可以自动打开windows
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
////         设置缓存
//        webSettings.setAppCacheEnabled(true);
////         设置缓存模式,一共有四种模式
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.loadUrl("http://120.78.138.104:8080/ColorAlum/#/mobielogin/zhangfan" +
                "/123456/main");
        return view;
    }

}