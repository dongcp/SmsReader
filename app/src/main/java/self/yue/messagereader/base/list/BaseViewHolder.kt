package self.yue.messagereader.base.list

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by dong on 14/10/2017.
 */
abstract class BaseViewHolder<E>(rootView: View) : RecyclerView.ViewHolder(rootView) {
  abstract fun bindView(item: E, position: Int);
}