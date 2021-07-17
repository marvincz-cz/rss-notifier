package cz.marvincz.rssnotifier.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cz.marvincz.rssnotifier.R
import cz.marvincz.rssnotifier.viewmodel.Settings2ViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: Settings2ViewModel = getViewModel()

    val wifiOnly: Boolean? by viewModel.wifiOnly.collectAsState(null)

    SettingsScreen(
        onBack = navController::popBackStack,
        wifiOnly = wifiOnly,
        setWifiOnly = viewModel::setWifiOnly
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    wifiOnly: Boolean?,
    setWifiOnly: (Boolean) -> Unit
) {
    MaterialTheme(colors = colors(isSystemInDarkTheme())) {
        Surface {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.fragment_settings)) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_back),
                                    contentDescription = stringResource(R.string.action_back)
                                )
                            }
                        }
                    )
                }
            ) {
                Column {
                    ListItem(
                        text = { Text(text = stringResource(R.string.preference_wifi_only_title)) },
                        secondaryText = { Text(text = stringResource(R.string.preference_wifi_only_summary)) },
                        trailing = {
                            Switch(
                                checked = wifiOnly ?: false,
                                enabled = wifiOnly != null,
                                onCheckedChange = setWifiOnly
                            )
                        }
                    )
                }
            }
        }
    }
}