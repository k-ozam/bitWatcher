package jp.maskedronin.bitwatcher.presentation.exchangeaccountregister

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.FragmentExchangeAccountRegisterBinding
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.extension.makeToast
import jp.maskedronin.bitwatcher.presentation.exchangeaccountedit.ExchangeAccountEditFragmentArgs
import jp.maskedronin.bitwatcher.presentation.toPrevious
import javax.inject.Inject

class ExchangeAccountRegisterFragment : Fragment() {
    private val navArgs: ExchangeAccountRegisterFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ExchangeAccountRegisterViewModel.Factory
    private val viewModel: ExchangeAccountRegisterViewModel
            by navGraphViewModels(R.id.navigation_exchange_account_register) {
                viewModelFactory.apply {
                    exchange = navArgs.exchangeInt.let { Exchange.fromInt(it) }
                }
            }

    private val binding: FragmentExchangeAccountRegisterBinding by lazy {
        FragmentExchangeAccountRegisterBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().setTitle(R.string.exchange_account_register_title)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.exchangeSelectDialogEvent.observe(viewLifecycleOwner, Observer {
            showExchangeSelectDialog()
        })

        val editNavController: NavController = Navigation.findNavController(
            requireActivity(), R.id.edit_nav_host_fragment
        )
        viewModel.exchange.observe(viewLifecycleOwner, Observer { exchange ->
            // 画面切り替え
            exchange?.run {
                // 認証情報入力画面
                editNavController.navigate(
                    R.id.action_to_exchangeAccountEditFragment,
                    ExchangeAccountEditFragmentArgs(exchange).toBundle()
                )
            } ?: run {
                // 何も表示しない
                editNavController.navigate(
                    R.id.action_to_emptyFragment
                )
            }
        })

        viewModel.finishEvent.observe(viewLifecycleOwner, Observer {
            toPrevious()
        })

        viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            makeToast(it).show()
        })
    }

    private fun showExchangeSelectDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.exchange_select_dialog_title)
            .also { builder ->
                val items = arrayOfNulls<Exchange?>(1).plus(viewModel.exchanges)
                val itemLabels = items.map {
                    it?.canonicalName ?: getString(R.string.unselected)
                }.toTypedArray()

                builder.setItems(itemLabels) { _, which ->
                    viewModel.onExchangeListItemClick(items[which])
                }
            }
            .create()
            .show()
    }
}