package self.yue.messagereader.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import self.yue.messagereader.R
import self.yue.messagereader.base.list.BaseRealmAdapter
import self.yue.messagereader.base.list.BaseViewHolder
import self.yue.messagereader.data.local.model.MessageHeader

/**
 * Created by dong on 14/10/2017.
 */
class MessageHeadersAdapter(context: Context) : BaseRealmAdapter<MessageHeader, MessageHeadersAdapter.MessageHeaderItemHolder>(context) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHeaderItemHolder =
          MessageHeaderItemHolder(LayoutInflater.from(context).inflate(R.layout.item_message_header, parent, false))

  override fun onBindViewHolder(holder: MessageHeaderItemHolder, position: Int) {
    items[position]?.let {
      holder.bindView(it, position)
    }
  }

  class MessageHeaderItemHolder(rootView: View) : BaseViewHolder<MessageHeader>(rootView) {
    private val mTvMessageHeader = rootView.findViewById<TextView>(R.id.tv_message_header)

    override fun bindView(item: MessageHeader, position: Int) {
      mTvMessageHeader.text = item.header
    }
  }
}