package self.yue.messagereader.ui

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.EditText
import com.malinskiy.superrecyclerview.SuperRecyclerView
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main_v2.*
import self.yue.messagereader.R
import self.yue.messagereader.data.local.model.MessageHeader

/**
 * Created by dongc on 10/21/2017.
 */
class MainActivity2 : AppCompatActivity() {
  private val mRvFromList: SuperRecyclerView by lazy {
    rv_message_headers
  }

  private val mMessageHeadersAdapter by lazy { MessageHeadersAdapter(this) }

  private val mMessageHeaders = RealmList<MessageHeader>()

  private val mSpareAnimator: SparseItemRemoveAnimator = SparseItemRemoveAnimator()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main_v2)

    setUpRecyclerView()

    setEventListeners()

    val messageHeaders = Realm.getDefaultInstance().where(MessageHeader::class.java).findAllAsync()
    messageHeaders.addChangeListener(object : RealmChangeListener<RealmResults<MessageHeader>> {
      override fun onChange(results: RealmResults<MessageHeader>?) {
        messageHeaders.removeChangeListener(this)

        results?.forEach {
          mMessageHeaders.add(it)
        }

        if (mMessageHeaders.size > 0)
          mMessageHeadersAdapter.notifyDataSetChanged()
      }
    })
  }

  private fun setUpRecyclerView() {
    mRvFromList.setLayoutManager(LinearLayoutManager(this))
    mRvFromList.recyclerView.itemAnimator = mSpareAnimator
    mRvFromList.adapter = mMessageHeadersAdapter
    mMessageHeadersAdapter.items = mMessageHeaders
  }

  private fun setEventListeners() {
    fab_add.setOnClickListener {
      val dialogView = layoutInflater.inflate(R.layout.dialog_add_message_header, null)

      val etMessageHeader = dialogView.findViewById<EditText>(R.id.et_message_header)

      AlertDialog.Builder(this)
              .setView(dialogView)
              .setPositiveButton("Ok") { dialog, _ ->
                val messageHeader = etMessageHeader.text.toString()
                if (!TextUtils.isEmpty(messageHeader)) {
                  val item = MessageHeader(messageHeader)
                  mMessageHeaders.add(item)
                  mMessageHeadersAdapter.notifyItemInserted(mMessageHeadersAdapter.itemCount - 1)

                  Realm.getDefaultInstance().executeTransactionAsync { realm ->
                    realm.copyToRealmOrUpdate(item)
                  }
                }
                dialog.dismiss()
              }
              .create()
              .show()
    }

    mRvFromList.setupSwipeToDismiss(object : SwipeDismissRecyclerViewTouchListener.DismissCallbacks {
      override fun canDismiss(position: Int): Boolean = true

      override fun onDismiss(recyclerView: RecyclerView, reverseSortedPositions: IntArray) {
        reverseSortedPositions.forEach {
          mSpareAnimator.setSkipNext(true)
          val removedHeader = mMessageHeaders[it]?.header
          mMessageHeaders.removeAt(it)
          mMessageHeadersAdapter.notifyItemRemoved(it)
          Realm.getDefaultInstance().executeTransactionAsync { realm ->
            val foundItem = realm.where(MessageHeader::class.java).equalTo("mHeader", removedHeader).findFirst()
            foundItem?.deleteFromRealm()
          }
        }
      }
    })
  }
}