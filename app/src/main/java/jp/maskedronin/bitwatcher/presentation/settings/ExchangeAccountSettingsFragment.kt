package jp.maskedronin.bitwatcher.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.LinearRecyclerViewBinding
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.extension.makeSnackbar
import jp.maskedronin.bitwatcher.presentation.common.extension.makeToast
import jp.maskedronin.bitwatcher.presentation.common.MarginItemDecoration
import jp.maskedronin.bitwatcher.presentation.common.extension.createMessageDialog
import jp.maskedronin.bitwatcher.presentation.toExchangeAccountRegister
import javax.inject.Inject

class ExchangeAccountSettingsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ExchangeAccountSettingsViewModel.Factory
    private val viewModel: ExchangeAccountSettingsViewModel by viewModels { viewModelFactory }

    private val binding: LinearRecyclerViewBinding by lazy {
        LinearRecyclerViewBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().setTitle(R.string.exchange_api_settings_title)

        if (binding.recyclerView.itemDecorationCount == 0) {
            // 区切り線を入れる
            val orientation: Int = binding.recyclerView.layoutManager
                .let { it as LinearLayoutManager }
                .orientation
            val dividerItemDecoration = DividerItemDecoration(context, orientation)
            binding.recyclerView.addItemDecoration(dividerItemDecoration)

            // マージン
            binding.recyclerView.addItemDecoration(
                MarginItemDecoration(
                    verticalMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal),
                    horizontalMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
                )
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ExchangeAccountRecyclerAdapter(
            accountValidViewModel = viewModel,
            accountUnconfirmedViewModel = viewModel,
            accountInvalidViewModel = viewModel,
            accountUnregisteredViewModel = viewModel
        )
        binding.recyclerView.adapter = adapter

        viewModel.exchangeToAccountList.observe(viewLifecycleOwner, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        viewModel.registerAccountEvent.observe(viewLifecycleOwner, Observer { exchange ->
            toExchangeAccountRegister(exchange)
        })

        viewModel.editEvent.observe(viewLifecycleOwner, Observer { exchange ->
            toExchangeAccountRegister(exchange)
        })

        viewModel.deleteConfirmDialogEvent.observe(viewLifecycleOwner, Observer { exchange ->
            showDeleteConfirmDialog(exchange)
        })

        viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            makeToast(it).show()
        })

        viewModel.snackbarEvent.observe(viewLifecycleOwner, Observer {
            makeSnackbar(config = it).show()
        })

        viewModel.messageDialogEvent.observe(viewLifecycleOwner, Observer { message ->
            createMessageDialog(message).show()
        })
    }

    private fun showDeleteConfirmDialog(exchange: Exchange) {
        AlertDialog.Builder(requireContext())
            .setMessage(
                getString(
                    R.string.exchange_api_unlink_confirm_dialog_message,
                    exchange.canonicalName
                )
            )
            .setPositiveButton(android.R.string.ok) { _, _ ->
                viewModel.onDeleteSelected(exchange)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}