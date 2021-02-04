package com.example.savemoney

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.os.Build
import android.provider.BaseColumns._ID
import androidx.annotation.RequiresApi
import com.google.android.gms.common.util.ArrayUtils.contains
import java.lang.Integer.MAX_VALUE
import java.lang.Integer.max
import java.time.Instant.MAX
import java.util.*

private const val DB_NAME = "MemoDatabase"
private const val DB_VERSION = 1

class MemoDatabase(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?){
     //テーブル作成
     db?.execSQL("""
         CREATE TABLE Memo (
         _id INTEGER PRIMARY KEY AUTOINCREMENT,
         date INTEGER NOT NULL ,
         date2 TEXT NOT NULL,
         shopName TEXT NOT NULL,
         price INTEGER NOT NULL,
         swing NUMERIC default "未振り分け",
         latitude REAL NOT NULL,
        longitude REAL NOT NULL
         );
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

class MarkerRecord(val id :Long, val latitude: Double,val longitude: Double,val shopName:String,val price:Long,val swing:String)

//指定した日の位置情報を検索する関数
fun selectInDay(context:Context, year: Int, month:Int, day:Int):
        List<MarkerRecord>{
    //検索条件に使用する日時を計算
    val calendar = Calendar.getInstance()//
    calendar.set(year,month,day,0,0,0)//引数の日時に設定
    val from = calendar.time.time.toString()//日付文字列を取得
    calendar.add(Calendar.DATE,1)//日時を一日分進める
    val to = calendar.time.time.toString()//日付け文字列を取得

    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo",null,"date >= ? AND date < ?", arrayOf(from, to),
    null,null,"date DESC")

    val marker = mutableListOf<MarkerRecord>()
    cursor.use{
        while(cursor.moveToNext()){
            val place = MarkerRecord(
                cursor.getLong(cursor.getColumnIndex("_id")),
                cursor.getDouble(cursor.getColumnIndex("latitude")),
                cursor.getDouble(cursor.getColumnIndex("longitude")),
                cursor.getString(cursor.getColumnIndex("shopName")),
                cursor.getLong(cursor.getColumnIndex("price")),
                cursor.getString(cursor.getColumnIndex("swing")))
            marker.add(place)
        }
    }
    database.close()
    return marker
}

fun selectOneDay(context: Context,year: Int,month: Int,day: Int):List<LocationRecord>{
    //検索条件に使用する日時を計算
    val calendar = Calendar.getInstance()//
    calendar.set(year,month,day,0,0,0)//引数の日時に設定
    val from = calendar.time.time.toString()//日付文字列を取得
    calendar.add(Calendar.DATE,1)//日時を一日分進める
    val to = calendar.time.time.toString()//日付け文字列を取得

    val database = MemoDatabase(context).readableDatabase

    val query = "SELECT * FROM MarkerLocation WHERE _id = (SELECT MAX(_id) FROM MarkerLocation)"

    val c = database.rawQuery(query,null)

//    val cursor = database.query("MarkerLocation", null,"date >= ? AND date < ?", arrayOf(from, to),
//                    null,null,null)

    val locations = mutableListOf<LocationRecord>()
    c.use{
        while(c.moveToNext()){
            val place = LocationRecord(
                    c.getLong(c.getColumnIndex("_id")),
                    c.getDouble(c.getColumnIndex("latitude")),
                    c.getDouble(c.getColumnIndex("longitude")),
                    c.getLong(c.getColumnIndex("date")))
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
fun insertText(context: Context, text: String, price: Int, year: Int,month: Int,day: Int, hantei: String,latitude: Double,longitude: Double,nowDateString : String) {
    val database = MemoDatabase(context).writableDatabase

    val calendar = Calendar.getInstance()
    calendar.set(year,month,day,0,0,0)//引数の日時に設定
    val from = calendar.time.time.toString()//日付文字列を取得

    database.use { db->
        val record = ContentValues().apply {
            put("shopName", text)
            put("price" , price)
            put("swing", hantei)
            put("date", from)
            put("latitude",latitude)
            put("longitude",longitude)
            put("date2",nowDateString)
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

    val cursor = database.query("Memo", arrayOf("shopName"), "swing = ?", arrayOf("未振り分け"), null, null, null)


    val texts = mutableListOf<String>()

    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("shopName"))
            texts.add(text)
        }
    }


    database.close()
    return texts
}

//   ***詳細画面で使用するもの***

//日
//未振り分けを検索
fun queryunsort(context: Context, str: String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 = ?", arrayOf("未振り分け","$str"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("shopName"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val yugo = ("${text}:${text2}円")
            texts.add(yugo)
        }
    }
    database.close()
    return texts
}
//消費を検索
fun queryconsumption(context: Context, str: String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 = ?", arrayOf("消費","$str"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("shopName"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val yugo = ("${text}:${text2}円")
            texts.add(yugo)
        }
    }
    database.close()
    return texts
}
//消費の合計値
fun consumption(context: Context, str: String) : Int {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 = ?", arrayOf("消費","$str"), null, null, "swing DESC")
    var totalPrice = 0
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getInt(cursor.getColumnIndex("price"))
            totalPrice += text
        }
    }
    database.close()
    return totalPrice
}
//浪費を検索
fun queryextravagance(context: Context, str: String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 = ?", arrayOf("浪費","$str"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("shopName"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val yugo = ("${text}:${text2}円")
            texts.add(yugo)
        }
    }
    database.close()
    return texts
}
//浪費の合計値
fun extravagance(context: Context, str: String) : Int {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 = ?", arrayOf("浪費","$str"), null, null, "swing DESC")
    var totalPrice = 0
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getInt(cursor.getColumnIndex("price"))
            totalPrice += text
        }
    }
    database.close()
    return totalPrice
}
// ＊＊＊月＊＊＊

//月の未振り分けを検索
fun querymonthunsort(context: Context, yearmonth : String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 LIKE ?", arrayOf("未振り分け","$yearmonth%"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("shopName"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val yugo = ("${text}:${text2}円")
            texts.add(yugo)
        }
    }
    database.close()
    return texts
}
//月の消費を検索
fun querymonthconsumption(context: Context, yearmonth : String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 LIKE ?", arrayOf("消費","$yearmonth%"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("shopName"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val yugo = ("${text}:${text2}円")
            texts.add(yugo)
        }
    }
    database.close()
    return texts
}
//消費の合計値
fun monthconsumption(context: Context, yearmonth : String) : Int {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 LIKE ?", arrayOf("消費","$yearmonth%"), null, null, "swing DESC")
    var totalPrice = 0
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getInt(cursor.getColumnIndex("price"))
            totalPrice += text
        }
    }
    database.close()
    return totalPrice
}
//月の浪費を検索
fun querymonthextravagance(context: Context, yearmonth: String) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 LIKE ?", arrayOf("浪費","$yearmonth%"), null, null, "swing DESC")
    val texts = mutableListOf<String>()
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getString(cursor.getColumnIndex("shopName"))
            val text2 = cursor.getString(cursor.getColumnIndex("price"))
            val yugo = ("${text}:${text2}円")
            texts.add(yugo)
        }
    }
    database.close()
    return texts
}
//月の浪費の合計値
fun monthextravagance(context: Context, yearmonth : String) : Int {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","swing","date2"), "swing = ? AND date2 LIKE ?", arrayOf("浪費","$yearmonth%"), null, null, "swing DESC")
    var totalPrice = 0
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getInt(cursor.getColumnIndex("price"))
            totalPrice += text
        }
    }
    database.close()
    return totalPrice
}

//その月に使った金額を計算できる　今はまだ未使用
fun monthtotal(context: Context, yearmonth : String) : Int {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("Memo", arrayOf("shopName","price","date2"), "date2 LIKE ?", arrayOf("$yearmonth%"), null, null, null)
    var money = 0
    cursor.use {
        while (cursor.moveToNext()) {
            val text = cursor.getInt(cursor.getColumnIndex("price"))
            money += text
        }
    }
    database.close()
    return money
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

//MarkerLocationのデータを消す
fun selectDeleteMarker(context: Context):MutableList<Int>{
    val database = MemoDatabase(context).readableDatabase
    //テーブルの一番最後に入ったデータを検索
    val query = "SELECT _id FROM MarkerLocation WHERE _id = (SELECT MAX(_id) FROM MarkerLocation)"
    val c = database.rawQuery(query,null)
    val del = mutableListOf<Int>()
    c.use {
        while (c.moveToNext()){
            val non = (
                c.getInt(c.getColumnIndex("_id"))
            )
            del.add(non)
        }
    }
    database.close()
    return del
}



//マーカー消す
fun deleteMarker(context: Context,whereId: String) {
    val database = MemoDatabase(context).writableDatabase

    val whereClauses = "_id = ?"
    val whereArgs = arrayOf(whereId)
    database.delete("MarkerLocation", whereClauses, whereArgs)
}






//    val whereArgs = arrayOf(whereId)
//    database.update("Memo", values, whereClauses, whereArgs)
//}
