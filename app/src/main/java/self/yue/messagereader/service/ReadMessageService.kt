package self.yue.messagereader.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.util.Patterns
import com.google.gson.JsonObject
import io.realm.Realm
import io.realm.RealmResults
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import self.yue.messagereader.data.local.model.MessageHeader
import self.yue.messagereader.data.remote.CogiloServiceBuilder
import self.yue.messagereader.utils.Constants
import java.util.regex.Pattern

/**
 * Created by dongc on 10/16/2017.
 */
class ReadMessageService : Service() {
  private var mMessageHeaders: RealmResults<MessageHeader>? = null

  private val onMessageReceived = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      intent?.run {
        if (action == Constants.Action.MESSAGE_RECEIVED) {
          var from = getStringExtra(Constants.Extra.MESSAGE_FROM)
          val content = getStringExtra(Constants.Extra.MESSAGE_BODY)

          Log.e("From", "" + from);

          if (Pattern.matches(Patterns.PHONE.pattern(), from) && (from.length == 12 || from.length == 13)) {
            from = from.substring(3)
            from = "0" + from
          }

          mMessageHeaders?.let {
            if (it.size > 0) {
              if (it.where().equalTo("mHeader", from).findFirst() != null) {
                val data = JsonObject()
                data.addProperty("from", from)
                data.addProperty("sms_content", content)

                CogiloServiceBuilder.getService().sendSMS(data).enqueue(object : retrofit2.Callback<ResponseBody> {
                  override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                      Log.e("Check", "Success")
                    } else {
                      Log.e("Check", "Fail")
                    }
                  }

                  override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    Log.e("Check", "Fail")
                  }
                })
              }
            }
          }
        }
      }
    }
  }

  override fun onCreate() {
    super.onCreate()
    LocalBroadcastManager.getInstance(this).registerReceiver(onMessageReceived, IntentFilter(Constants.Action.MESSAGE_RECEIVED))
  }

  override fun onBind(intent: Intent?): IBinder {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    intent?.run {
      when (action) {
        Constants.Action.INIT -> {
          init()
        }
      }
    }
    return START_STICKY
  }

  override fun onDestroy() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(onMessageReceived)
    mMessageHeaders?.removeAllChangeListeners()
    super.onDestroy()
  }

  private fun init() {
    mMessageHeaders = Realm.getDefaultInstance().where(MessageHeader::class.java).findAllAsync()
    setEventListeners()
  }

  private fun setEventListeners() {
    mMessageHeaders?.addChangeListener { results -> mMessageHeaders = results }
  }
}