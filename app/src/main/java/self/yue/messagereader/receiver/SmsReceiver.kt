package self.yue.messagereader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.support.v4.content.LocalBroadcastManager
import android.telephony.SmsMessage
import self.yue.messagereader.utils.Constants

/**
 * Created by dong on 12/10/2017.
 */
class SmsReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    intent?.run {
      if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == action) {
        val bundle = extras
        val messageFormat = getStringExtra("format")
        if (bundle != null) {
          try {
            val pdus = bundle.get("pdus") as Array<ByteArray>
            pdus.forEach {
              val sms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SmsMessage.createFromPdu(it as ByteArray?, messageFormat)
              } else {
                SmsMessage.createFromPdu(it as ByteArray?)
              }

              LocalBroadcastManager.getInstance(context)
                      .sendBroadcast(Intent(Constants.Action.MESSAGE_RECEIVED).apply {
                        putExtra(Constants.Extra.MESSAGE_FROM, sms.originatingAddress)
                        putExtra(Constants.Extra.MESSAGE_BODY, sms.messageBody)
                      })
            }
          } catch (ex: Exception) {
            ex.printStackTrace()
          }
        }
      }
    }
  }
}