package jp.maskedronin.bitwatcher.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.common.util.Constants
import jp.maskedronin.bitwatcher.databinding.FragmentSettingsBinding
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.presentation.common.extension.makeSnackbar
import jp.maskedronin.bitwatcher.presentation.common.extension.makeToast
import jp.maskedronin.bitwatcher.presentation.toExchangeAccountSettings
import javax.inject.Inject

class SettingsFragment : Fragment() {
    private val binding: FragmentSettingsBinding by lazy {
        FragmentSettingsBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: SettingsViewModel.Factory
    private val viewModel: SettingsViewModel by viewModels { viewModelFactory }

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

        requireActivity().setTitle(R.string.settings_title)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.settlementCurrencySelectDialogEvent.observe(viewLifecycleOwner, Observer {
            showSettlementCurrencySelectDialog()
        })

        viewModel.exchangeAccountEvent.observe(viewLifecycleOwner, Observer {
            toExchangeAccountSettings()
        })

        viewModel.ossLicenseEvent.observe(viewLifecycleOwner, Observer {
            toOssLicense()
        })

        viewModel.contactEvent.observe(viewLifecycleOwner, Observer {
            toOfficialLineAccount()
        })

        viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            makeToast(it).show()
        })

        viewModel.snackbarEvent.observe(viewLifecycleOwner, Observer {
            makeSnackbar(config = it).show()
        })
    }

    private fun showSettlementCurrencySelectDialog() {
        val items: Array<Currency> = Currency.settlements.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.settlement_currency_select_dialog_title)
            .setItems(items.map(Currency::getSymbol).toTypedArray()) { _, which ->
                viewModel.onSettlementCurrencyListItemClick(items[which])
            }
            .create()
            .show()
    }

    private fun toOssLicense() {
        OssLicensesMenuActivity.setActivityTitle(
            getString(R.string.oss_license_title)
        )
        startActivity(
            Intent(context, OssLicensesMenuActivity::class.java)
        )
    }

    private fun toOfficialLineAccount() {
        val lineAccountUri: Uri = Uri.parse(Constants.OFFICIAL_LINE_ACCOUNT_URL)
        startActivity(
            Intent(Intent.ACTION_VIEW, lineAccountUri)
        )
    }
}