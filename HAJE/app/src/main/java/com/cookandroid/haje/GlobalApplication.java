package com.cookandroid.haje;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.navi.NaviClient;
import com.kakao.sdk.navi.model.CoordType;
import com.kakao.sdk.navi.model.Location;
import com.kakao.sdk.navi.model.NaviOption;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance=this;

        KakaoSdk.init(this, "49b0f46f5cab7ddf1154d8c0399dabe0");
    }
}
