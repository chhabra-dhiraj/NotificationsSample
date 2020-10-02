package io.github.chhabra_dhiraj.notire.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.chhabra_dhiraj.notire.R
import io.github.chhabra_dhiraj.notire.domain.Notification
import io.github.chhabra_dhiraj.notire.ui.NotificationListFragmentDirections

class NotificationsListAdapter :
    ListAdapter<Notification, NotificationsListAdapter.ViewHolder>(
        NotificationsDiffCallback()
    ) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tv_notification_title)

        fun bind(item: Notification) {
            itemView.setOnClickListener {
                itemView.findNavController()
                    .navigate(
                        NotificationListFragmentDirections.actionNotificationListFragmentToNotificationDetailsFragment(
                            item
                        )


                    )
            }

            title.text = item.title
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_notifications_recycler_view, parent, false)

                return ViewHolder(view)
            }
        }
    }
}

class NotificationsDiffCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(
        oldItem: Notification,
        newItem: Notification
    ): Boolean {
        return oldItem.alert_id == newItem.alert_id
    }

    override fun areContentsTheSame(
        oldItem: Notification,
        newItem: Notification
    ): Boolean {
        return oldItem == newItem
    }
}