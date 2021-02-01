package com.example.savemoney

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

private const val DB_NAME = "MemoDatabase"
private const val DB_VERSION = 1

class MemoDatabase(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?){
     //テーブル作成
     db?.execSQL("""
         CREATE TABLE Memo (
         _id INTEGER PRIMARY KEY AUTOINCREMENT,
         date TEXT NOT NULL ,
         productname TEXT NOT NULL,
         price INTEGER NOT NULL,
         swing NUMERIC default "未振り分け");
         """)
     db?.execSQL("""
         CREATE TABLE Gps(
         _id INTEGER PRIMARY KEY AUTOINCREMENT,
         latitude REAL NOT NULL,
         longitude REAL NOT NULL,
         time INTEGER NOT NULL
         );
     """)
    db?.execSQL("""
        CREATE TABLE MarkerLocation(
        _id INTEGER PRIMARY KEY AUTOINCREMENT,
        latitude REAL NOT NULL,
        longitude REAL NOT NULL,
        date INTEGER NOT NULL
        );
    """)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

//データベースのレコードを表現するクラス
class LocationRecord(val id :Long, val latitude : Double,
    val longitude : Double, val date :Long)

//指定した日の位置情報を検索する関数
fun selectInDay(context:Context, year: Int, month:Int, day:Int):
        List<LocationRecord>{
    //検索条件に使用する日時を計算
    val calendar = Calendar.getInstance()//
    calendar.set(year,month,day,0,0,0)//引数の日時に設定
    val from = calendar.time.time.toString()//日付文字列を取得
    calendar.add(Calendar.DATE,1)//日時を一日分進める
    val to = calendar.time.time.toString()//日付け文字列を取得

    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("MarkerLocation",null,"date >= ? AND date < ?", arrayOf(from, to),
    null,null,"date DESC")

    val locations = mutableListOf<LocationRecord>()
    cursor.use{
        while(cursor.moveToNext()){
            val place = LocationRecord(
                cursor.getLong(cursor.getColumnIndex("_id")),
                cursor.getDouble(cursor.getColumnIndex("latitude")),
                cursor.getDouble(cursor.getColumnIndex("longitude")),
                cursor.getLong(cursor.getColumnIndex("date")))
            locations.add(place)
        }
    }
    database.close()
    return locations
}

//マーカー位置情報をデータベースに保存する
fun insertMarkerLocations(context: Context, latitude: Double,longitude: Double, year: Int, month:Int, day:Int) {
    val database = MemoDatabase(context).writableDatabase

    val calendar = Calendar.getInstance()
    calendar.set(year,month,day,0,0,0)//引数の日時に設定
    val from = calendar.time.time.toString()//日付文字列を取得

    database.use { db ->
                val record = ContentValues().apply {
                    put("latitude", latitude)
                    put("longitude",longitude)
                    put("date",from)
                }

                db.insert("MarkerLocation", null, record)
    }
}

fun insertLocations(context: Context, locations : List<Location>){
    val database = MemoDatabase(context).writableDatabase

    database.use {db ->
        locations.filter {!it.isFromMockProvider}
                .forEach{location ->
                    val recored = ContentValues().apply{
                        put("latitude",location.latitude)
                        put("longitude",location.longitude)
                        put("time",location.time)
                    }
                    db.insert("Gps",null, recored)
                }
    }
}





//メモをＤＢに保存
@RequiresApi(Build.VERSION_CODES.O)
fun insertText(context: Context, text: String, price: Int, nowDateString: String, hantei: String) {
    val database = MemoDatabase(context).writableDatabase

    database.use { db->
        val record = ContentValues().apply {
            put("productname", text)
            put("price" , price)
            put("swing", hantei)
            put("date", nowDateString)
        }


        db.insert("Memo",null,record)
    }
}




//メモ検索-----------------------------------------------------------------

fun queryID(context: Context) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", arrayOf("_id"), "swing = ?", arrayOf("未振り分け"), null, null, null)


    val ids = mutableListOf<String>()

    cursor.use {
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("_id"))
            ids.add(id)
        }
    }


    database.close()
    return ids
}





fun querySwing(context: Context) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", arrayOf("swing"), "swing = ?", arrayOf("未振り分け"), null, null, null)


    val sorts = mutableListOf<String>()

    cursor.use {
        while (cursor.moveToNext()) {
            val sort = cursor.getString(cursor.getColumnIndex("swing"))
            sorts.add(sort)
        }
    }


    database.close()
    return sorts
}





fun queryTexts(context: Context) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", arrayOf("productname"), "swing = ?", arrayOf("未振り分け"), null, null, null)


    val texts = mutableListOf<String>()

    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("productname"))
            texts.add(text)
        }
    }


    database.close()
    return texts
}

//
fun queryunsort(context: Context, str: String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("productname","price","swing","date"), "swing = ? AND date = ?", arrayOf("未振り分け","$str"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("productname"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val en = "円"
            val yugo = ("$text:$text2$en")
            texts.add(yugo)
        }
    }


    database.close()
    return texts
}

fun queryconsumption(context: Context, str: String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("productname","price","swing","date"), "swing = ? AND date = ?", arrayOf("消費","$str"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("productname"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val en = "円"
            val yugo = ("$text:$text2$en")
            texts.add(yugo)
        }
    }


    database.close()
    return texts
}

fun queryextravagance(context: Context, str: String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("productname","price","swing","date"), "swing = ? AND date = ?", arrayOf("浪費","$str"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("productname"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val en = "円"
            val yugo = ("$text:$text2$en")
            texts.add(yugo)
        }
    }


    database.close()
    return texts
}


//振り分け用クエリーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーーー
fun querySortedTexts(context: Context) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", arrayOf("productname"), "swing = ?", arrayOf("未振り分け"), null, null, null)


    val texts = mutableListOf<String>()

    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("productname"))
            texts.add(text)
        }
    }


    database.close()
    return texts
}



fun querySortedPrices(context: Context) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", arrayOf("price"), "swing = ?", arrayOf("未振り分け"), null, null, null)


    val prices = mutableListOf<String>()

    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("price"))
            prices.add(text)
        }
    }


    database.close()
    return prices
}
//--------------------------------------------------------------------------------------------






fun queryPrice(context: Context) : MutableList<Int> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", arrayOf("price"), "swing = ?", arrayOf("未振り分け"), null, null, null)


    val prices = mutableListOf<Int>()

    cursor.use {
        while (cursor.moveToNext()) {
            val price = cursor.getInt(cursor.getColumnIndex("price"))
            prices.add(price)
        }
    }


    database.close()
    return prices
}











//-----------------------------------------------------------------------------------

//振り分けDB_update
fun update(context: Context, whereId: String, newSwing: String) {
    val database = MemoDatabase(context).writableDatabase
    val values = ContentValues()
    values.put("swing",newSwing)
    val whereClauses = "_id = ?"

//    val whereArgs = arrayOf(whereId)
    database.update("Memo", values, whereClauses, arrayOf(whereId))
}






//    val whereArgs = arrayOf(whereId)
//    database.update("Memo", values, whereClauses, whereArgs)
//}
