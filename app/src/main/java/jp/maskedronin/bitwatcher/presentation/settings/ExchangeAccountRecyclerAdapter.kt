package jp.maskedronin.bitwatcher.presentation.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.maskedronin.bitwatcher.databinding.ItemExchangeAccountInvalidBinding
import jp.maskedronin.bitwatcher.databinding.ItemExchangeAccountUnconfirmedBinding
import jp.maskedronin.bitwatcher.databinding.ItemExchangeAccountUnregisteredBinding
import jp.maskedronin.bitwatcher.databinding.ItemExchangeAccountValidBinding
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange

class ExchangeAccountRecyclerAdapter(
    private val accountValidViewModel: ViewHolder.AccountValid.ViewModel,
    private val accountUnconfirmedViewModel: ViewHolder.AccountUnconfirmed.ViewModel,
    private val accountInvalidViewModel: ViewHolder.AccountInvalid.ViewModel,
    private val accountUnregisteredViewModel: ViewHolder.AccountUnregistered.ViewModel
) : RecyclerView.Adapter<ExchangeAccountRecyclerAdapter.ViewHolder>() {
    var items: List<Pair<Exchange, ExchangeAccount?>> = emptyList()

    override fun getItemViewType(position: Int): Int {
        val account: ExchangeAccount? = items[position].second
        return when {
            account == null -> ViewType.ACCOUNT_UNREGISTERED
            account.isValid == null -> ViewType.ACCOUNT_NOT_UNCONFIRMED
            account.isValid -> ViewType.ACCOUNT_VALID
            account.isValid.not() -> ViewType.ACCOUNT_INVALID
            else -> error("unknown state: ${items[position]}")
        }.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewTypeInt: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (ViewType.from(viewTypeInt)!!) {
            ViewType.ACCOUNT_VALID -> ViewHolder.AccountValid(
                accountValidViewModel,
                ItemExchangeAccountValidBinding.inflate(layoutInflater, parent, false)
            )
            ViewType.ACCOUNT_NOT_UNCONFIRMED -> ViewHolder.AccountUnconfirmed(
                accountUnconfirmedViewModel,
                ItemExchangeAccountUnconfirmedBinding.inflate(layoutInflater, parent, false)
            )
            ViewType.ACCOUNT_INVALID -> ViewHolder.AccountInvalid(
                accountInvalidViewModel,
                ItemExchangeAccountInvalidBinding.inflate(layoutInflater, parent, false)
            )
            ViewType.ACCOUNT_UNREGISTERED -> ViewHolder.AccountUnregistered(
                accountUnregisteredViewModel,
                ItemExchangeAccountUnregisteredBinding.inflate(layoutInflater, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(items[position].first)
    }

    private enum class ViewType {
        ACCOUNT_VALID,
        ACCOUNT_NOT_UNCONFIRMED,
        ACCOUNT_INVALID,
        ACCOUNT_UNREGISTERED,
        ;

        companion object {
            fun from(viewTypeInt: Int): ViewType? = values().find { it.ordinal == viewTypeInt }
        }
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bindTo(exchange: Exchange)

        class AccountValid(
            viewModel: ViewModel,
            private val binding: ItemExchangeAccountValidBinding
        ) : ViewHolder(binding.root) {
            interface ViewModel {
                fun onDeleteIconClick(exchange: Exchange)
            }

            init {
                binding.viewModel = viewModel
            }

            override fun bindTo(exchange: Exchange) {
                binding.exchange = exchange
            }
        }

        class AccountUnconfirmed(
            viewModel: ViewModel,
            private val binding: ItemExchangeAccountUnconfirmedBinding
        ) : ViewHolder(binding.root) {
            interface ViewModel {
                fun onEditIconClick(exchange: Exchange)
                fun onDeleteIconClick(exchange: Exchange)
            }

            init {
                binding.viewModel = viewModel
            }

            override fun bindTo(exchange: Exchange) {
                binding.exchange = exchange
            }
        }

        class AccountInvalid(
            viewModel: ViewModel,
            private val binding: ItemExchangeAccountInvalidBinding
        ) : ViewHolder(binding.root) {
            interface ViewModel {
                fun onEditIconClick(exchange: Exchange)
                fun onDeleteIconClick(exchange: Exchange)
            }

            init {
                binding.viewModel = viewModel
            }

            override fun bindTo(exchange: Exchange) {
                binding.exchange = exchange
            }
        }

        class AccountUnregistered(
            viewModel: ViewModel,
            private val binding: ItemExchangeAccountUnregisteredBinding
        ) : ViewHolder(binding.root) {
            interface ViewModel {
                fun onAccountUnregisteredClick(exchange: Exchange)
            }

            init {
                binding.viewModel = viewModel
            }

            override fun bindTo(exchange: Exchange) {
                binding.exchange = exchange
            }
        }
    }
}