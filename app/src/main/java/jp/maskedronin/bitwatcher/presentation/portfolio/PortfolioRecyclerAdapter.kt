package jp.maskedronin.bitwatcher.presentation.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import jp.maskedronin.bitwatcher.databinding.ItemPortfolioBinding
import jp.maskedronin.bitwatcher.domain.valueobject.Currency

class PortfolioRecyclerAdapter(
    private val viewHolderViewModel: ViewHolder.ViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<PortfolioRecyclerAdapter.ViewHolder>() {
    var portfolioItemList: List<PortfolioItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPortfolioBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding,
            viewHolderViewModel,
            lifecycleOwner
        )
    }

    override fun getItemCount(): Int = portfolioItemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(portfolioItemList[position])
    }

    class ViewHolder(
        private val binding: ItemPortfolioBinding,
        private val viewModel: ViewModel,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = viewModel
            binding.lifecycleOwner = lifecycleOwner
        }

        interface ViewModel {
            val settlement: LiveData<Currency>
            fun onPortfolioItemClick(portfolioItem: PortfolioItem)
            fun onPortfolioItemLongClick(portfolioItem: PortfolioItem)
        }

        fun bindTo(item: PortfolioItem) {
            binding.item = item
            itemView.setOnClickListener {
                viewModel.onPortfolioItemClick(item)
            }
            itemView.setOnLongClickListener {
                viewModel.onPortfolioItemLongClick(item)
                true
            }
        }
    }
}