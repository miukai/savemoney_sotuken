package com.example.savemoney

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.LocationResult

//位置情報を受け取るサービスクラス
class LocationService : IntentService("LocationService"){
    override fun onHandleIntent(intent: Intent?){
        //intentから位置情報を抽出してデータベースに保存する
        val result = LocationResult.extractResult(intent)
        if(result != null){
            insertLocations(this,result.locations)//データベースに保存する
        }
    }
}