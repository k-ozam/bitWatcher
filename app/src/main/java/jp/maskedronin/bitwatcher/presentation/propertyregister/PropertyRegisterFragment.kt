package jp.maskedronin.bitwatcher.presentation.propertyregister

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.FragmentPropertyRegisterBinding
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.extension.makeSnackbar
import jp.maskedronin.bitwatcher.presentation.toPrevious
import javax.inject.Inject

class PropertyRegisterFragment : Fragment() {
    private val binding: FragmentPropertyRegisterBinding by lazy {
        FragmentPropertyRegisterBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: PropertyRegisterViewModel.Factory
    private val viewModel: PropertyRegisterViewModel
            by navGraphViewModels(R.id.navigation_property_register) { viewModelFactory }

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

        requireActivity().setTitle(R.string.property_register_title)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.cryptoSelectDialogEvent.observe(viewLifecycleOwner, Observer {
            showCurrencySelectDialog()
        })

        viewModel.exchangeSelectDialogEvent.observe(viewLifecycleOwner, Observer {
            showExchangeSelectDialog()
        })

        viewModel.finishEvent.observe(viewLifecycleOwner, Observer {
            toPrevious()
        })

        viewModel.snackbarEvent.observe(viewLifecycleOwner, Observer {
            makeSnackbar(config = it).show()
        })
    }

    private fun showCurrencySelectDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.currency_select_dialog_title)
            .also { builder ->
                val items: List<Currency> = viewModel.getCryptos()
                builder.setItems(
                    items.map(Currency::getSymbol).toTypedArray()
                ) { _, which ->
                    viewModel.onClickCryptoListItem(items[which])
                }
            }
            .create()
            .show()
    }

    private fun showExchangeSelectDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.exchange_select_dialog_title)
            .also { builder ->
                val items: List<Exchange?> = mutableListOf<Exchange?>(null).apply {
                    addAll(viewModel.getExchanges())
                }
                builder.setItems(
                    items.map { exchange ->
                        exchange?.canonicalName ?: getString(R.string.unselected)
                    }.toTypedArray()
                ) { _, which ->
                    viewModel.onExchangeListItemClick(items[which])
                }
            }
            .create()
            .show()
    }
}