package jp.maskedronin.bitwatcher.presentation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.navOptions
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.exchangeaccountregister.ExchangeAccountRegisterFragment
import jp.maskedronin.bitwatcher.presentation.exchangeaccountregister.ExchangeAccountRegisterFragmentArgs
import jp.maskedronin.bitwatcher.presentation.launch.LaunchFragment
import jp.maskedronin.bitwatcher.presentation.notification.NotificationFragment
import jp.maskedronin.bitwatcher.presentation.notification.NotificationFragmentDirections
import jp.maskedronin.bitwatcher.presentation.onboarding.OnboardingFragment
import jp.maskedronin.bitwatcher.presentation.portfolio.PortfolioFragment
import jp.maskedronin.bitwatcher.presentation.portfolio.PortfolioFragmentDirections
import jp.maskedronin.bitwatcher.presentation.propertyregister.PropertyRegisterFragment
import jp.maskedronin.bitwatcher.presentation.settings.ExchangeAccountSettingsFragment
import jp.maskedronin.bitwatcher.presentation.settings.ExchangeAccountSettingsFragmentDirections
import jp.maskedronin.bitwatcher.presentation.settings.SettingsFragment
import jp.maskedronin.bitwatcher.presentation.settings.SettingsFragmentDirections

/**
 * 画面遷移をここに定義する
 * ([navController] を隠蔽する)
 */

private val Fragment.navController: NavController
    get() = requireActivity()
        .let { it as MainActivity }
        .navController

fun LaunchFragment.toOnboarding() {
    navController.navigate(
        R.id.action_launch_to_onboarding,
        null,
        navOptions {
            popUpTo(R.id.navigation) {
                inclusive = false
            }
        }
    )
}

fun LaunchFragment.toPortfolio() {
    navController.navigate(
        R.id.action_launch_to_portfolio,
        null,
        navOptions {
            popUpTo(R.id.navigation) {
                inclusive = false
            }
        }
    )
}

fun OnboardingFragment.toPortfolio() {
    navController.navigate(
        R.id.action_onboarding_to_portfolio,
        null,
        navOptions {
            popUpTo(R.id.navigation) {
                inclusive = false
            }
        }
    )
}

fun PortfolioFragment.toExchangeAccountRegister() {
    navController.navigate(
        PortfolioFragmentDirections.actionPortfolioToExchangeAccountRegister()
    )
}

fun PortfolioFragment.toPropertyRegister() {
    navController.navigate(
        PortfolioFragmentDirections.actionPortfolioToPropertyRegister()
    )
}

fun ExchangeAccountRegisterFragment.toPrevious() {
    navController.popBackStack()
}

fun PropertyRegisterFragment.toPrevious() {
    navController.popBackStack()
}

fun NotificationFragment.toExchangeAccountRegister(exchange: Exchange) {
    navController.navigate(
        NotificationFragmentDirections
            .actionNotificationToExchangeAccountRegister().actionId,
        ExchangeAccountRegisterFragmentArgs(exchangeInt = exchange.toInt())
            .toBundle()
    )
}

fun ExchangeAccountSettingsFragment.toExchangeAccountRegister(exchange: Exchange) {
    navController.navigate(
        ExchangeAccountSettingsFragmentDirections
            .actionSettingsToExchangeAccountRegister().actionId,
        ExchangeAccountRegisterFragmentArgs(exchange.toInt()).toBundle()
    )
}

fun SettingsFragment.toExchangeAccountSettings() {
    navController.navigate(
        SettingsFragmentDirections.actionSettingsToExchangeAccountSettings()
    )
}

