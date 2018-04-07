package self.yue.messagereader

import android.app.Application
import android.content.Intent
import io.realm.Realm
import io.realm.RealmConfiguration
import self.yue.messagereader.data.remote.CogiloServiceBuilder
import self.yue.messagereader.service.ReadMessageService
import self.yue.messagereader.utils.Constants

/**
 * Created by dongc on 10/16/2017.
 */
class MessageReaderApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    Realm.init(this)
    val realmConfiguration = RealmConfiguration.Builder()
            .name("MessageReader.realm")
            .schemaVersion(1)
            .build()
    Realm.setDefaultConfiguration(realmConfiguration)

    CogiloServiceBuilder.initRetrofit()

    startService(
            Intent(this, ReadMessageService::class.java).apply {
              action = Constants.Action.INIT
            })
  }
}