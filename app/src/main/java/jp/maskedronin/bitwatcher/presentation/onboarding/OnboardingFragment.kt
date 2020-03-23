package jp.maskedronin.bitwatcher.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.FragmentOnboardingBinding
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.presentation.common.extension.makeSnackbar
import jp.maskedronin.bitwatcher.presentation.common.extension.makeToast
import jp.maskedronin.bitwatcher.presentation.common.extension.createMessageDialog
import jp.maskedronin.bitwatcher.presentation.toPortfolio
import javax.inject.Inject

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {
    @Inject
    lateinit var viewModelFactory: OnboardingViewModel.Factory
    private val viewModel: OnboardingViewModel by viewModels { viewModelFactory }

    private val binding: FragmentOnboardingBinding by lazy {
        FragmentOnboardingBinding.bind(requireView())
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currencySelectDialogEvent.observe(viewLifecycleOwner, Observer {
            showCurrencySelectDialog()
        })

        viewModel.portfolioEvent.observe(viewLifecycleOwner, Observer {
            toPortfolio()
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

        viewModel.init()
    }

    private fun showCurrencySelectDialog() {
        val items: Array<Currency> = Currency.settlements.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.settlement_currency_select_dialog_title)
            .setCancelable(false)
            .setItems(items.map(Currency::getSymbol).toTypedArray()) { _, which ->
                viewModel.onCurrencySelected(items[which])
            }
            .create()
            .show()
    }
}