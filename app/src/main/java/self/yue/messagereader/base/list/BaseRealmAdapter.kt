package self.yue.messagereader.base.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by dong on 14/10/2017.
 */
abstract class BaseRealmAdapter<E : RealmObject, VH : RecyclerView.ViewHolder>(context: Context) : RecyclerView.Adapter<VH>() {
  private val mContext = context
  val context: Context
    get() = mContext

  private var mItems: RealmList<E> = RealmList()
  var items: RealmList<E>
    get() = mItems
    set(value) {
      mItems = value
      notifyDataSetChanged()
    }

  override fun getItemCount(): Int = mItems.size

  fun addItems(items: List<E>) {
    val previousSize = mItems.size
    mItems.addAll(items)
    notifyItemRangeInserted(previousSize, items.size)
  }
}