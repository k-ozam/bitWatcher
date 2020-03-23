package jp.maskedronin.bitwatcher.presentation.notification

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.LinearRecyclerViewBinding
import jp.maskedronin.bitwatcher.presentation.toExchangeAccountRegister
import javax.inject.Inject

class NotificationFragment : Fragment() {
    private val binding: LinearRecyclerViewBinding by lazy {
        LinearRecyclerViewBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: NotificationViewModel.Factory
    private val viewModel: NotificationViewModel by viewModels { viewModelFactory }

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

        requireActivity().setTitle(R.string.notification_title)

        if (binding.recyclerView.itemDecorationCount == 0) {
            val orientation: Int = binding.recyclerView.layoutManager
                .let { it as LinearLayoutManager }
                .orientation
            binding.recyclerView.addItemDecoration(
                DividerItemDecoration(context, orientation)
            )
        }

        val adapter = NotificationRecyclerAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        viewModel.notifications.observe(viewLifecycleOwner, Observer { notifications ->
            adapter.apply {
                items = notifications
                notifyDataSetChanged()
            }
        })

        viewModel.exchangeAccountRegisterEvent.observe(viewLifecycleOwner, Observer { exchange ->
            toExchangeAccountRegister(exchange)
        })
    }
}