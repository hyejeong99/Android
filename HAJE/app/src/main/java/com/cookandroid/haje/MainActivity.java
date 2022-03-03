package com.cookandroid.haje;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.common.util.KakaoCustomTabsClient;
import com.kakao.sdk.navi.NaviClient;
import com.kakao.sdk.navi.model.CoordType;
import com.kakao.sdk.navi.model.Location;
import com.kakao.sdk.navi.model.NaviOption;


import net.daum.mf.map.api.MapView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHashKey();

//        MapView mapView = new MapView(this);
//
//        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.mapView);
//        mapViewContainer.addView(mapView);

    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    public void onNaviButtonClicked(View v){
        Toast.makeText(this, "길안내 버튼 눌렸습니다", Toast.LENGTH_LONG).show();
        if(NaviClient.getInstance().isKakaoNaviInstalled(this)){
            Log.i("카카오내비", "카카오내비 앱으로 길안내 가능");

//            Intent intent = getPackageManager().getLaunchIntentForPackage("com.locnall.KimGiSa");
//            startActivity(intent);

            // 경유지 없는 경우
            startActivity(
                    NaviClient.getInstance().navigateIntent(
                    new Location("카카오 판교오피스", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
                )
            );


            // 경유지 있는 경우
//            ArrayList<Location> stopover = new ArrayList<Location>();
//            stopover.add(new Location("판교역 1번출구", "127.111492", "37.395225"));
//            NaviClient.getInstance().navigateIntent(
//                    new Location("카카오 판교오피스", "127.108640", "37.402111"),
//                    new NaviOption(CoordType.WGS84),
//                    stopover
//            );
        }
        else {
            Log.i("카카오내비", "카카오내비 미설치 : 웹 길안내 사용 권장");

            Uri uri = NaviClient.getInstance().navigateWebUrl(
                    new Location("카카오 판교오피스", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
            );

            KakaoCustomTabsClient.INSTANCE.openWithDefault(this, uri);
        }
    }

    public void onNavi2ButtonClicked(View v){
        startActivity(
                NaviClient.getInstance().shareDestinationIntent(
                new Location("카카오 판교오피스", "127.108640", "37.402111"),
                new NaviOption(CoordType.WGS84)
            )
        );
    }

    public void onLoginButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onGpsLoginButtonClicked(View v){
        Toast.makeText(this, "GPS 탐색 시작", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
        startActivity(intent);
    }

}