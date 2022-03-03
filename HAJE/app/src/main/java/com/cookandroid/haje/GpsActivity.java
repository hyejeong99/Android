package com.cookandroid.haje;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Handler;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.haje.R;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.sdk.common.util.KakaoCustomTabsClient;
import com.kakao.sdk.navi.NaviClient;
import com.kakao.sdk.navi.model.CoordType;
import com.kakao.sdk.navi.model.Location;
import com.kakao.sdk.navi.model.NaviOption;


import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GpsActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener{


    private static final String LOG_TAG = "GpsActivity";

    private static GpsActivity baseApplication;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    //kakao map 띄움
    MapView mapView;
    ViewGroup mapViewContainer;

    MapPOIItem marker;
    MapPoint mapPoint;

    //지도에 원 그리기
    MapCircle circle1;
    //지도에 이동 거리 그리기
    MapPolyline polyline;
    //로딩 다이얼로그
    AppCompatDialog progressDialog;

    //이미지 버튼
    ImageButton mapGpsButton;
    ImageButton callButton;
    ImageButton myPgButton;
    //매칭 수락&거절 버튼_추가
    ImageButton rideButton;
    ImageButton arriveButton;

    FirebaseFirestore db;
    Breakdown breakdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //지도 띄우기
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        //마커 생성하기
        marker = new MapPOIItem();

        //내 위치 보는 이미지 버튼 눌렸을 때
        mapGpsButton = findViewById(R.id.mapGpsButton);
        mapGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapView.setMapViewEventListener(GpsActivity.this);
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            }
        });
        //기사님 호출 이미지 버튼 눌렸을 때
        callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = getIntent();
                String email = emailIntent.getStringExtra("email");
                Log.d("인텐트 값 넘기기", email);
                //출발, 목적지 검색 인텐트로 넘어가기
                Intent callIntent = new Intent(getApplicationContext(), DriverCallActivity.class);
                callIntent.putExtra("email", email);
                startActivityForResult(callIntent, 101);
            }
        });

        //매칭 수락&거절 버튼 안보이게_추가

        rideButton = findViewById(R.id.rideButton);
        arriveButton = findViewById(R.id.arriveButton);
        if(callButton.getVisibility()==View.VISIBLE){
            rideButton.setVisibility(View.INVISIBLE);
            arriveButton.setVisibility(View.INVISIBLE);

        }
        //마이페이지 버튼 눌렸을 때
        myPgButton = findViewById(R.id.myPgButton);
        myPgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout) ;
                View myPageDrawer = (View) findViewById(R.id.drawer);

                drawer.openDrawer(myPageDrawer);
            }
        });

        // 탑승 버튼 눌림 > 카카오내비 실행 - onRideButtonClicked 함수 밑에 따로 구현


        // 도착 버튼 눌림 > 내역객체 수정, db입력, 내역페이지로 이동
//        arriveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent breakdownIntent = getIntent();
//                Breakdown breakdown = (Breakdown) breakdownIntent.getSerializableExtra("breakdown");
//                Log.d("내역객체 접근", breakdown.getUser_email());
//
//                // 객체 도착시간 수정
//                Date now = new Date();
//                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
//                String endTime = timeFormat.format(now);
//
//                breakdown.setEndTime(endTime);
//
//                // db에 입력
//                db = FirebaseFirestore.getInstance();
//                String id = breakdown.getId();
//
//                db.collection("breakdown").document(id).set(breakdown)
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        Log.d("db접근 내역 넣기 성공", breakdown.getId());
//                    }
//                    else{
//                        Log.d("db접근 내역 넣기 실패", task.getException().getMessage());
//                    }
//                });
//
//                // 내역페이지로 이동
//                Intent intent = new Intent(getApplicationContext(), ShowRideActivity.class);
//                String uuid = id;
//                intent.putExtra("uuid", id);
//                startActivity(intent);
//            }
//        });

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }
    }

    // 탑승 버튼 눌림 > 내역객체 수정, 카카오내비 실행
    public void onRideButtonClicked(View v){
        Toast.makeText(this, "카카오내비로 안내합니다", Toast.LENGTH_LONG).show();

        // 객체 탑승시간 수정
        Date now = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
        String startTime = timeFormat.format(now);

        breakdown.setStartTime(startTime);
        String destination = breakdown.getDestination();

        startActivity(
                NaviClient.getInstance().shareDestinationIntent(
                        new Location(destination, "127.09039242435075", "37.62826552802066"),
                        new NaviOption(CoordType.WGS84)
                )
        );


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {
    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
    }


    private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(GpsActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(GpsActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(GpsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음

        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(GpsActivity.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(GpsActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(GpsActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(GpsActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GpsActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CallDriverActivity에서 호출하기 버튼 눌렀을 때
        if (requestCode == 101) {
            String name = data.getStringExtra("name");
            breakdown = (Breakdown) data.getSerializableExtra("breakdown");
            markDriverPosition();
        }

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    //기사님 위치 표시 함수
    private void markDriverPosition() {
        //call button 안보이게 하기
        callButton.setVisibility(View.INVISIBLE);
        //매칭 수락&거절 버튼 보이게 하기
        rideButton.setVisibility(View.VISIBLE);
        arriveButton.setVisibility(View.VISIBLE);
        //로딩 화면 띄우기
        startProgress();
        mapView.setMapViewEventListener(GpsActivity.this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        //로딩 화면 띄운 후 구현
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mapView.setMapViewEventListener(GpsActivity.this);
//                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                //토스트 메시지 띄우기
                Toast.makeText(GpsActivity.this, "가까운 위치의 기사님과 매칭 완료", Toast.LENGTH_LONG).show();
                //원 그리기
                circle1 = new MapCircle(
                        MapPoint.mapPointWithGeoCoord(37.57843368281801, 127.04861222228894), //기사 위치
                        200, // radius
                        Color.argb(128, 91, 83, 215), // strokeColor
                        Color.argb(128, 91, 83, 215) // fillColor
                );
                circle1.setTag(1234);
                mapView.addCircle(circle1);

                //이동 거리 그리기
                polyline = new MapPolyline();
                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(128, 255, 0, 0)); // Polyline 컬러 지정.

                // Polyline 좌표 지정.
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57843368281801, 127.04861222228894));//기사 위치
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57776660423137, 127.04851050890508));
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.577660318091816, 127.04885383167067));
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.5771203821599, 127.04848368681401));
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.576801125608455, 127.04906283355939));
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57578075917584, 127.04810796711756));
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.575721237369024, 127.04806505177184));
                //polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.57622291967998, 127.04910574866106));//승객 위치

                // Polyline 지도에 올리기.
                mapView.addPolyline(polyline);

                //고정 위치 핀 찍기
                mapPoint = MapPoint.mapPointWithGeoCoord(37.57843368281801, 127.04861222228894);//기사 위치
                marker.setItemName("기사님 위치");
                marker.setTag(1);
                marker.setMapPoint(mapPoint);
                //커스텀 마커 만들기
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setCustomImageResourceId(R.drawable.redpin);
                marker.setCustomImageAutoscale(false);
                marker.setCustomImageAnchor(0.5f, 1.0f);

                mapView.addPOIItem(marker);


                Log.d("내역객체 접근1", breakdown.getUser_email());

                arriveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("내역객체 접근2", breakdown.getUser_email());

                        // 객체 도착시간 수정
                        Date now = new Date();
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                        String endTime = timeFormat.format(now);

                        breakdown.setEndTime(endTime);

                        // db에 입력
                        db = FirebaseFirestore.getInstance();
                        String id = breakdown.getId();

                        db.collection("breakdown").document(id).set(breakdown)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        Log.d("db접근 내역 넣기 성공", breakdown.getId());
                                    }
                                    else{
                                        Log.d("db접근 내역 넣기 실패", task.getException().getMessage());
                                    }
                                });

                        // 내역페이지로 이동
                        Intent intent = new Intent(getApplicationContext(), ShowRideActivity.class);
                        String uuid = id;
                        intent.putExtra("uuid", id);
                        startActivity(intent);
                    }
                });
            }
        }, 3500);


    }

    //로딩 다이어로그 위한 함수

    private void startProgress() {
        //progressON(GpsActivity.this,"기사님과 매칭중입니다.");
        //로딩 다이얼로그 띄우기
        Activity activity = GpsActivity.this;
        String message = "기사님과 매칭 중입니다.";
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.activity_gps);
            progressDialog.show();

        }

        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.loadingImage);
        img_loading_frame.setVisibility(View.VISIBLE);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 3500);
    }

    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.activity_gps);
            progressDialog.show();
        }

        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.loadingImage);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

    }

    public void progressSET(String message) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}