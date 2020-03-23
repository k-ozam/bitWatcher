package jp.maskedronin.bitwatcher.presentation.launch

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.android.support.AndroidSupportInjection
import jp.maskedronin.bitwatcher.presentation.toOnboarding
import jp.maskedronin.bitwatcher.presentation.toPortfolio
import javax.inject.Inject

class LaunchFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: LaunchViewModel.Factory
    private val viewModel: LaunchViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onboardingEvent.observe(this, Observer {
            toOnboarding()
        })

        viewModel.portfolioEvent.observe(this, Observer {
            toPortfolio()
        })

        viewModel.init()
    }
}