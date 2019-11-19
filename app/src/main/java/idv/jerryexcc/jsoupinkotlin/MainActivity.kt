package idv.jerryexcc.jsoupinkotlin

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

//有網路行為別忘了在Manifest內加入uses-permission
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //匿名AsyncTask
        //Jsoup需在背景執行網路request
        object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                try {
                    //建立連線, 並取得連線後的response
                    val response: Connection.Response =
                        Jsoup.connect("https://www.google.com.tw").execute()
                    //取得的response(整份HTML)轉成Document物件, 方便直接取得HTML的各個節點
                    val doc: Document = response.parse()
                    Log.d("TAG", "document: $doc")

                    //取得HTML中的<title>字串
                    val title = doc.title()
                    //因doInBackGround是在不同執行序上, 故需runOnUiThread做畫面更新
                    runOnUiThread{
                        tvShow.text = title
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return ""
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
}

/*
 * execute()
 * 建立一個queue並執行非同步任務
 * 假設你開了兩個任務並同時都用execute()
 * 系統就會把兩個任務丟入同一個queue內
 * 並逐一執行
 *
 * executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
 * 同一時間並行執行
 * 一樣是建立兩個任務
 * 兩個任務會同時一起執行
 *
 * */