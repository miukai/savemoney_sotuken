package com.example.savemoney

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.os.Build
import android.text.Editable
import androidx.annotation.RequiresApi
import java.util.Date
import java.text.SimpleDateFormat


private const val DB_NAME = "MemoDatabase"
private const val DB_VERSION = 1

class MemoDatabase(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?){
     //テーブル作成
     db?.execSQL("""
         CREATE TABLE Memo (
         _id INTEGER PRIMARY KEY AUTOINCREMENT,
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
//  金額系の設定
    db?.execSQL("""
        CREATE TABLE Setting (
         _id INTEGER PRIMARY KEY AUTOINCREMENT,
         goalMoneydb INTEGER default 0,
         nowMoneydb INTEGER default 0,
         useMoneydb INTEGER default 0,
         counter INTEGER);
    """)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

//データベースのレコードを表現するクラス
class LocationRecord(val id :Long, val latitude : Double,
    val longitude : Double)

class MarkerRecord(val id :Long, val latitude: Double,val longitude: Double,val shopName:String,val price:Long,val swing:String)






//指定した日の位置情報を検索する関数
fun selectInDay(context:Context,nowDateString: String):
        List<MarkerRecord>{
    //検索条件に使用する日時を計算


    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo",null,"date2 = ?", arrayOf(nowDateString),
    null,null,null)

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

//fun selectOneDay(context: Context):List<LocationRecord>{
////
//    val database = MemoDatabase(context).readableDatabase
//
//    val query = "SELECT * FROM MarkerLocation WHERE _id = (SELECT MAX(_id) FROM MarkerLocation)"
//
//    val c = database.rawQuery(query,null)
//
//
//    val locations = mutableListOf<LocationRecord>()
//    c.use{
//        while(c.moveToNext()){
//            val place = LocationRecord(
//                    c.getLong(c.getColumnIndex("_id")),
//                    c.getDouble(c.getColumnIndex("latitude")),
//                    c.getDouble(c.getColumnIndex("longitude")))
//            locations.add(place)
//        }
//    }
//    database.close()
//    return locations
//}

//マーカー位置情報をデータベースに保存する
//fun insertMarkerLocations(context: Context, latitude: Double,longitude: Double) {
//    val database = MemoDatabase(context).writableDatabase
//
//
//
//    database.use { db ->
//                val record = ContentValues().apply {
//                    put("latitude", latitude)
//                    put("longitude",longitude)
//                }
//
//                db.insert("MarkerLocation", null, record)
//    }
//}

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
fun insertText(context: Context, text: String, price: Int, hantei: String,latitude: Double,longitude: Double,nowDateString : String) {
    val database = MemoDatabase(context).writableDatabase



    database.use { db->
        val record = ContentValues().apply {
            put("shopName", text)
            put("price" , price)
            put("swing", hantei)
            put("latitude",latitude)
            put("longitude",longitude)
            put("date2",nowDateString)
        }


        db.insert("Memo",null,record)
    }
}

//設定画面で入力した金額をDBに保存
@RequiresApi(Build.VERSION_CODES.O)
fun insertSettingMoney(context: Context, goalMoney: Editable, nowMoney: Editable, useMoneydb: Editable) {
    val database = MemoDatabase(context).writableDatabase
    database.use { db ->
        val update = ContentValues().apply {
            put("goalMoneydb", goalMoney.toString().toInt())
            put("nowMoneydb",nowMoney.toString().toInt())
            put("useMoneydb", useMoneydb.toString().toInt())
        }
        database.update("Setting",update,"_id = ?", arrayOf("1"))
    }
}
fun updatenowMoney(context: Context, toString: String) {
    val database = MemoDatabase(context).writableDatabase
    database.use { db ->
        val update = ContentValues().apply {
            put("nowMoneydb",toString)
        }
        database.update("Setting",update,"_id = ?", arrayOf("1"))
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

class EditRecord(val id :Long, val shopName: String,val price:Long)

fun queryEdit(context: Context,nowDateString: String):MutableList<EditRecord>{

    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", null,"date2 = ?", arrayOf(nowDateString),
        null,null,null)

    val edit = mutableListOf<EditRecord>()
    cursor.use{
        while(cursor.moveToNext()){
            val memo = EditRecord(
                cursor.getLong(cursor.getColumnIndex("_id")),
                cursor.getString(cursor.getColumnIndex("shopName")),
                cursor.getLong(cursor.getColumnIndex("price")))
            edit.add(memo)
        }
    }
    database.close()
    return edit
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
////貯金額を変える
//fun addT(context: Context) : List<String> {
//    val database = MemoDatabase(context).readableDatabase
//    val cursor = database.query("setting", null, null, null, null, null, null)
//    var money = 1000
//    cursor.use {
//        while (cursor.moveToNext()) {
//            val chooseMoney = cursor.getString(cursor.getColumnIndex("nowMoneydb"))
//            chooseMoney += money
//        }
//    }
//    database.close()
//    return money
//}

//その月に使った金額を計算できる　今はまだ未使用
fun monthtotal(context: Context) : Int {
    val database = MemoDatabase(context).readableDatabase

    //月だけの取得が雑になりました。すいません。
    val s = SimpleDateFormat("yyyy/M/dd").format(Date())
    val split_arr = s.split("/")
    var yearmonth = ("${split_arr[0]}/${split_arr[1]}")

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

//設定の目標金額、現在の貯金額、今月使える金額を取り出す
fun querygoalMoney(context: Context) : List<Int> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("setting", null, null, null, null, null, null)


    val Moneydb = mutableListOf<Int>()

    cursor.use {
        while (cursor.moveToNext()) {
            val chooseMoney = cursor.getInt(cursor.getColumnIndex("goalMoneydb"))
            Moneydb.add(chooseMoney)
        }
    }
    database.close()
    return Moneydb
}
fun querynowMoney(context: Context) : List<Int> {
    val database = MemoDatabase(context).readableDatabase
    val cursor = database.query("setting", null, null, null, null, null, null)
    val Moneydb = mutableListOf<Int>()

    cursor.use {
        while (cursor.moveToNext()) {
            val chooseMoney = cursor.getInt(cursor.getColumnIndex("nowMoneydb"))
            Moneydb.add(chooseMoney)
        }
    }
    database.close()
    return Moneydb
}
fun queryUsemoney(context: Context) : List<Int> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("setting", null, null, null, null, null, null)


    val useMoneydb = mutableListOf<Int>()

    cursor.use {
        while (cursor.moveToNext()) {
            val chooseuseMoney = cursor.getInt(cursor.getColumnIndex("useMoneydb"))
            useMoneydb.add(chooseuseMoney)
        }
    }
    database.close()
    return useMoneydb
}

fun <R> once(block: () -> R): () -> OnceResult<R>? = run {
    var isDone = false;

    // run の返値となるラムダ式
    {
        if (isDone) {
            null
        } else {
            isDone = true

            val result = block()
            OnceResult(result)
        }
    }
}

/**
 * [once]で生成した関数が返す、
 * 実行した処理の返値を保持するためのクラス。
 */
data class OnceResult<R>(val result: R)

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
//マーカー消す
//fun deleteMarker(context: Context,lat: String, lng: String) {
//    val database = MemoDatabase(context).writableDatabase
//
//
//    val cursor = database.query("MarkerLocation", arrayOf("_id"), "latitude = ? AND longitude = ?",
//            arrayOf(lat,lng), null, null, null)
//
//    val whereClauses = "_id = ?"
//
//
//    cursor.use {
//        while (cursor.moveToNext()) {
//            val ID = cursor.getInt(cursor.getColumnIndex("_id")).toString()
//            var arrayId = arrayOf(ID)
//            database.delete("MarkerLocation",whereClauses,arrayId)
//        }
//    }
//
//}

//編集画面で使う

//レコード
class EditUpRecord(val id:Long,val shopName: String,val price: Long,val swing :String,val date:String)

fun selectShop(context: Context,id:String?):List<EditUpRecord>{
    val database = MemoDatabase(context).readableDatabase
    val id = id.toString()

    val cursor = database.query("Memo", null, "shopName = ?",
            arrayOf(id), null, null, null)

    val marker = mutableListOf<EditUpRecord>()
    cursor.use{
        while(cursor.moveToNext()){
            val place = EditUpRecord(
                    cursor.getLong(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("shopName")),
                    cursor.getLong(cursor.getColumnIndex("price")),
                    cursor.getString(cursor.getColumnIndex("swing")),
                    cursor.getString(cursor.getColumnIndex("date2")))
            marker.add(place)
        }
    }
    database.close()
    return marker
}

fun memoUpdate(context: Context,shopName: String,price: Int,swing: String,ID :Int){
    val database = MemoDatabase(context).writableDatabase
    val values = ContentValues()

    values.put("price",price)
    values.put("swing",swing)
    val whereClauses = "_id = ?"
    val whereId = ID.toString()

//    val whereArgs = arrayOf(whereId)
    database.update("Memo", values, whereClauses, arrayOf(whereId))
}


