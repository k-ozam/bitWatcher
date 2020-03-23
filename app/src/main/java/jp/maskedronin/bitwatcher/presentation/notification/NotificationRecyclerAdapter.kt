package jp.maskedronin.bitwatcher.presentation.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.maskedronin.bitwatcher.databinding.ItemNotificationBinding
import jp.maskedronin.bitwatcher.databinding.ItemNotificationWarningBinding
import jp.maskedronin.bitwatcher.domain.entity.Notification

class NotificationRecyclerAdapter(
    private val viewHolderViewModel: ViewHolder.ViewModel
) : RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder>() {
    var items: List<Notification> = emptyList()

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is Notification.Unauthorized -> ViewType.WARNING
            else -> TODO()
        }.toInt()

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeInt: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        return when (ViewType.fromInt(viewTypeInt)) {
            ViewType.NORMAL -> ViewHolder.Normal(
                ItemNotificationBinding.inflate(layoutInflater, parent, false),
                viewHolderViewModel
            )
            ViewType.WARNING -> ViewHolder.Warning(
                ItemNotificationWarningBinding.inflate(layoutInflater, parent, false),
                viewHolderViewModel
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(items[position])
    }

    override fun getItemCount(): Int = items.size

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        interface ViewModel {
            fun onNotificationClick(notification: Notification)
        }

        abstract fun bindTo(notification: Notification)

        class Normal(
            private val binding: ItemNotificationBinding,
            viewModel: ViewModel
        ) : ViewHolder(binding.root) {
            init {
                binding.viewModel = viewModel
            }

            override fun bindTo(notification: Notification) {
                binding.notification = notification
            }
        }

        class Warning(
            private val binding: ItemNotificationWarningBinding,
            viewModel: ViewModel
        ) : ViewHolder(binding.root) {
            init {
                binding.viewModel = viewModel
            }

            override fun bindTo(notification: Notification) {
                check(notification is Notification.Unauthorized)
                binding.notification = notification
            }
        }
    }
}

private enum class ViewType {
    NORMAL,
    WARNING,
    ;

    fun toInt(): Int = ordinal

    companion object {
        fun fromInt(value: Int): ViewType = values().find { it.toInt() == value }!!
    }
}