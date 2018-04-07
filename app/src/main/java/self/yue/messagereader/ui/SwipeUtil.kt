package self.yue.messagereader.ui

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by dongc on 10/17/2017.
 */
class SwipeUtil(dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {


  override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}