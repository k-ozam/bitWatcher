package jp.maskedronin.bitwatcher.presentation.portfolio

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.FragmentPortfolioBinding
import jp.maskedronin.bitwatcher.databinding.FragmentPortfolioItemDetailDialogBinding
import jp.maskedronin.bitwatcher.databinding.IncludePropertyAmountModifyBinding
import jp.maskedronin.bitwatcher.databinding.PopupSwipeRefreshTutorialBinding
import jp.maskedronin.bitwatcher.presentation.common.extension.*
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import jp.maskedronin.bitwatcher.presentation.toExchangeAccountRegister
import jp.maskedronin.bitwatcher.presentation.toPropertyRegister
import javax.inject.Inject

class PortfolioFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: PortfolioViewModel.Factory
    private val viewModel: PortfolioViewModel by viewModels { viewModelFactory }

    private val binding: FragmentPortfolioBinding by lazy {
        FragmentPortfolioBinding.inflate(layoutInflater)
    }

    private val swipeRefreshTutorialPopup: PopupWindow by lazy {
        PopupWindow(context)
            .apply {
                contentView = PopupSwipeRefreshTutorialBinding
                    .inflate(layoutInflater)
                    .root
                setBackgroundDrawable(null)

                contentView.setOnClickListener {
                    dismiss()
                    viewModel.onSwipeRefreshTutorialCloseClick()
                }
                lifecycle.observeEvent(Lifecycle.Event.ON_STOP) {
                    dismiss()
                }
            }
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

        requireActivity().setTitle(R.string.portfolio_title)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initPortfolioView()

        binding.portfolioRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val registerButtonVisible: Boolean = when {
                    recyclerView.isVerticalScrollBarEnabled.not() -> true
                    newState != RecyclerView.SCROLL_STATE_IDLE /* スクロール中 */ -> false
                    recyclerView.canScrollDown().not() /* 下端に到達 */ -> false
                    else -> true
                }
                with(binding.registerButton) {
                    if (registerButtonVisible) show() else hide()
                }
            }
        })

        viewModel.swipeRefreshTutorialVisible.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible) {
                swipeRefreshTutorialPopup
                    .showAtLocation(binding.swipeRefreshLayout, Gravity.CENTER, 0, 0)
            } else {
                swipeRefreshTutorialPopup.dismiss()
            }
        })

        viewModel.propertyDetailDialogEvent.observe(viewLifecycleOwner, Observer { portfolioItem ->
            showPropertyDetailDialog(portfolioItem)
        })

        viewModel.propertyActionSelectDialogEvent.observe(
            viewLifecycleOwner,
            Observer { portfolioItem ->
                showPropertyActionSelectDialog(portfolioItem)
            }
        )

        viewModel.portfolioRegisterActionSelectDialogEvent.observe(viewLifecycleOwner, Observer {
            showPortfolioRegisterActionSelectDialog()
        })

        viewModel.exchangeAccountRegisterEvent.observe(viewLifecycleOwner, Observer {
            toExchangeAccountRegister()
        })

        viewModel.propertyRegisterEvent.observe(viewLifecycleOwner, Observer {
            toPropertyRegister()
        })

        viewModel.amountModifyDialogEvent.observe(viewLifecycleOwner, Observer { portfolioItem ->
            showPropertyAmountModifyDialog(portfolioItem)
        })

        viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            makeToast(it).show()
        })

        viewModel.snackbarEvent.observe(viewLifecycleOwner, Observer {
            makeSnackbar(config = it).show()
        })
    }

    private fun initPortfolioView() {
        // 区切り線を入れる
        val orientation: Int = binding.portfolioRecyclerView.layoutManager
            .let { it as LinearLayoutManager }
            .orientation
        val dividerDrawable: Drawable = requireContext()
            .resolveAttrResourceId(android.R.attr.listDivider)
            .let { resId ->
                ResourcesCompat.getDrawable(resources, resId, requireContext().theme)!!
            }
        val dividerItemDecoration = DividerItemDecoration(context, orientation)
            .apply { setDrawable(dividerDrawable) }
        binding.portfolioRecyclerView.addItemDecoration(dividerItemDecoration)

        val portfolioAdapter = PortfolioRecyclerAdapter(
            viewModel,
            viewLifecycleOwner
        )
        binding.portfolioRecyclerView.adapter = portfolioAdapter
        viewModel.portfolioItemList.observe(viewLifecycleOwner, Observer { portfolioItemList ->
            portfolioAdapter.portfolioItemList = portfolioItemList
            portfolioAdapter.notifyDataSetChanged()
        })
    }

    private fun showPropertyDetailDialog(portfolioItem: PortfolioItem) {
        AlertDialog.Builder(requireContext())
            .also { builder ->
                val dialogBinding =
                    FragmentPortfolioItemDetailDialogBinding.inflate(layoutInflater)
                dialogBinding.item = portfolioItem

                builder.setView(dialogBinding.root)
            }
            .create()
            .show()
    }

    private fun showPropertyActionSelectDialog(portfolioItem: PortfolioItem) {
        val actions = PropertyAction.values()

        AlertDialog.Builder(requireContext())
            .setItems(
                actions.map {
                    it.labelStringResource.apply(requireContext())
                }.toTypedArray()
            ) { _, which ->
                when (actions[which]) {
                    PropertyAction.MODIFY_AMOUNT -> viewModel.onAmountModifySelected(portfolioItem)
                    PropertyAction.DELETE -> viewModel.onPropertyDeleteSelected(portfolioItem)
                }
            }
            .create()
            .show()
    }

    private fun showPropertyAmountModifyDialog(portfolioItem: PortfolioItem) {
        val dialogBinding = IncludePropertyAmountModifyBinding.inflate(layoutInflater)
            .apply {
                amountEdit.setText(portfolioItem.amount.toString())
                currencySymbol.text = portfolioItem.currency.getSymbol()
            }

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.property_amount_modify_dialog_title)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.property_amount_modify_positive_button) { _, _ ->
                val newAmount: Double = dialogBinding.amountEdit.text.toString().toDoubleOrNull()
                    ?: return@setPositiveButton
                viewModel.onAmountModifyButtonClicked(portfolioItem, newAmount)
            }
            .show()
    }

    private fun showPortfolioRegisterActionSelectDialog() {
        val actions = PortfolioRegisterAction.values()

        AlertDialog.Builder(requireContext())
            .setItems(
                actions.map {
                    it.labelStringResource.apply(requireContext())
                }.toTypedArray()
            ) { _, which ->
                when (actions[which]) {
                    PortfolioRegisterAction.REGISTER_EXCHANGE_ACCOUNT ->
                        viewModel.onRegisterExchangeAccountSelected()
                    PortfolioRegisterAction.REGISTER_PROPERTY ->
                        viewModel.onRegisterPropertySelected()
                }
            }
            .create()
            .show()
    }
}

private enum class PropertyAction(val labelStringResource: StringResource) {
    MODIFY_AMOUNT(StringResource.from(R.string.property_action_modify_amount_label)),
    DELETE(StringResource.from(R.string.property_action_delete_label))
}

private enum class PortfolioRegisterAction(val labelStringResource: StringResource) {
    REGISTER_EXCHANGE_ACCOUNT(StringResource.from(R.string.portfolio_register_action_link_exchange_api_label)),
    REGISTER_PROPERTY(StringResource.from(R.string.portfolio_register_action_manually_label))
}