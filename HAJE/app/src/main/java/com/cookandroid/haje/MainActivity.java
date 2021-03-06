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
        Toast.makeText(this, "????????? ?????? ???????????????", Toast.LENGTH_LONG).show();
        if(NaviClient.getInstance().isKakaoNaviInstalled(this)){
            Log.i("???????????????", "??????????????? ????????? ????????? ??????");

//            Intent intent = getPackageManager().getLaunchIntentForPackage("com.locnall.KimGiSa");
//            startActivity(intent);

            // ????????? ?????? ??????
            startActivity(
                    NaviClient.getInstance().navigateIntent(
                    new Location("????????? ???????????????", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
                )
            );


            // ????????? ?????? ??????
//            ArrayList<Location> stopover = new ArrayList<Location>();
//            stopover.add(new Location("????????? 1?????????", "127.111492", "37.395225"));
//            NaviClient.getInstance().navigateIntent(
//                    new Location("????????? ???????????????", "127.108640", "37.402111"),
//                    new NaviOption(CoordType.WGS84),
//                    stopover
//            );
        }
        else {
            Log.i("???????????????", "??????????????? ????????? : ??? ????????? ?????? ??????");

            Uri uri = NaviClient.getInstance().navigateWebUrl(
                    new Location("????????? ???????????????", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
            );

            KakaoCustomTabsClient.INSTANCE.openWithDefault(this, uri);
        }
    }

    public void onNavi2ButtonClicked(View v){
        startActivity(
                NaviClient.getInstance().shareDestinationIntent(
                new Location("????????? ???????????????", "127.108640", "37.402111"),
                new NaviOption(CoordType.WGS84)
            )
        );
    }

    public void onLoginButtonClicked(View v){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void onGpsLoginButtonClicked(View v){
        Toast.makeText(this, "GPS ?????? ??????", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
        startActivity(intent);
    }

}