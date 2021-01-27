package com.example.savemoney

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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
         swing NUMERIC default "未振り分け",
         latiude REAL NOT NULL,
         longitube REAL NOT NULL);
         """)
     db?.execSQL("""
         CREATE TABLE Gps(
         _id INTEGER PRIMARY KEY AUTOINCREMENT,
         latiude REAL NOT NULL,
         longitude REAL NOT NULL,
         date TEXT NOT NULL
         );
     """)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}

//データベースのレコードを表現するクラス
class LocationRecord(val id :Long, val latitude : Double,
    val longitude : Double, val date :String)

//指定した日のマーカー位置情報を検索する関数
fun selectInDay(context:Context, year: Int, month:Int, day:Int):MutableList<LocationRecord>{
    //検索条件に使用する日時を計算
    val calendar = Calendar.getInstance()//
    calendar.set(year,month,day,0,0,0)//引数の日時に設定
    val from = calendar.time.time.toString()//日付文字列を取得
    calendar.add(Calendar.DATE,1)//日時を一日分進める
    val to = calendar.time.time.toString()//日付け文字列を取得

    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Gps",null,"date = ?", arrayOf(to),
    null,null,"id")

    val locations = mutableListOf<LocationRecord>()
    cursor.use{
        while(cursor.moveToNext()){
            val place = LocationRecord(
                cursor.getLong(cursor.getColumnIndex("_id")),
                cursor.getDouble(cursor.getColumnIndex("latitude")),
                cursor.getDouble(cursor.getColumnIndex("longitude")),
                cursor.getString(cursor.getColumnIndex("date")))
            locations.add(place)
        }
    }
    database.close()
    return locations
}



fun insertLocations(context: Context, lat:Double, lon:Double, nowDate: String){
    val database = MemoDatabase(context).writableDatabase

    database.use { db->
        val record = ContentValues().apply {
            put("latiude",lat)
            put("longitude",lon)
            put("date",nowDate)
        }
        db.insert("Gps",null,record)
    }
}




//メモをＤＢに保存
@RequiresApi(Build.VERSION_CODES.O)
fun insertText(context: Context, text: String, price: Int, nowDateString: String, ido: Int, kedo: Int, hantei: String) {
    val database = MemoDatabase(context).writableDatabase

    database.use { db->
        val record = ContentValues().apply {
            put("productname", text)
            put("price" , price)
            put("swing", hantei)
            put("date", nowDateString)
            put("latiude", ido)
            put("longitube", kedo)
        }


        db.insert("Memo",null,record)
    }
}




//メモ検索-----------------------------------------------------------------

fun queryID(context: Context) : MutableList<String> {
    val database = MemoDatabase(context).readableDatabase

    val cursor = database.query("Memo", null, null, null, null, null, null)


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

    val cursor = database.query("Memo", null, null, null, null, null, null)


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

    val cursor = database.query("Memo", null, null, null, null, null, null)


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

    val cursor = database.query("Memo", null, null, null, null, null, null)


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
