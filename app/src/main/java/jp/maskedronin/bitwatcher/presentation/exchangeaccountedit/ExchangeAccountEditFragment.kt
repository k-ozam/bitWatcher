package jp.maskedronin.bitwatcher.presentation.exchangeaccountedit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.BitWatcher
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.FragmentExchangeAccountEditBinding
import jp.maskedronin.bitwatcher.presentation.common.extension.createMessageDialog
import jp.maskedronin.bitwatcher.presentation.common.extension.makeSnackbar
import jp.maskedronin.bitwatcher.presentation.common.extension.makeToast
import javax.inject.Inject

class ExchangeAccountEditFragment : Fragment() {
    private val navArgs: ExchangeAccountEditFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ExchangeAccountEditViewModel.Factory
    private val viewModel: ExchangeAccountEditViewModel by viewModels {
        viewModelFactory.apply { exchange = navArgs.exchange }
    }

    private val binding: FragmentExchangeAccountEditBinding by lazy {
        FragmentExchangeAccountEditBinding.inflate(layoutInflater)
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

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.authorizeConfirmDialogEvent.observe(viewLifecycleOwner, Observer {
            val positiveButtonTextResId: Int = android.R.string.ok
            AlertDialog.Builder(requireContext())
                .setMessage(
                    getString(
                        R.string.exchange_api_link_confirm_dialog_message,
                        getString(positiveButtonTextResId)
                    )
                )
                .setPositiveButton(positiveButtonTextResId) { _, _ ->
                    viewModel.onAuthorizeConfirmButtonClick()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        })

        viewModel.appRestartEvent.observe(viewLifecycleOwner, Observer {
            BitWatcher.restart(requireContext())
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
}