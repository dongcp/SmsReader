package self.yue.messagereader.data.local.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by dong on 14/10/2017.
 */
open class MessageHeader constructor() : RealmObject() {
  @PrimaryKey
  private var mHeader: String = ""
  var header: String
    get() = mHeader
    set(value) {
      mHeader = value
    }

  constructor(header: String) : this() {
    mHeader = header
  }
}