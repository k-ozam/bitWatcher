package jp.maskedronin.bitwatcher.presentation

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dagger.android.AndroidInjection
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.databinding.ActivityMainBinding
import jp.maskedronin.bitwatcher.presentation.common.LoggerFragmentLifecycleCallbacks
import jp.maskedronin.bitwatcher.presentation.common.extension.createMessageDialog
import jp.maskedronin.bitwatcher.presentation.common.extension.makeSnackbar
import jp.maskedronin.bitwatcher.presentation.common.extension.makeToast
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val navController: NavController by lazy {
        val navHostFragmentId: Int = supportFragmentManager.fragments
            .find { it is NavHostFragment }!!
            .id
        findNavController(navHostFragmentId)
    }

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            LoggerFragmentLifecycleCallbacks, true
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.setPortfolioShown(
                destination.id == R.id.portfolioFragment
            )
        }

        viewModel.notificationEvent.observe(this, Observer {
            navController.navigate(R.id.action_to_notification)
        })

        viewModel.settingsEvent.observe(this, Observer {
            navController.navigate(R.id.action_to_settings)
        })

        viewModel.toastEvent.observe(this, Observer {
            makeToast(it).show()
        })

        viewModel.snackbarEvent.observe(this, Observer {
            makeSnackbar(config = it).show()
        })

        viewModel.messageDialogEvent.observe(this, Observer { message ->
            createMessageDialog(message).show()
        })
    }

    /**
     * キーボード表示中に他の領域がタップされた場合に、キーボードを非表示にする
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(binding.root)
        binding.root.requestFocus()
        return super.dispatchTouchEvent(ev)
    }
}

private fun hideKeyboard(rootView: View) {
    rootView.context.getSystemService(Activity.INPUT_METHOD_SERVICE)
        .let { it as InputMethodManager }
        .hideSoftInputFromWindow(
            rootView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
}