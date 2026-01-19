package com.marcos.myspentapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcos.myspentapp.ui.composable.BottomBar
import com.marcos.myspentapp.ui.composable.LoginScreen
import com.marcos.myspentapp.ui.composable.MySpentApp
import com.marcos.myspentapp.ui.composable.PerfilEdit
import com.marcos.myspentapp.ui.composable.PerfilScreen
import com.marcos.myspentapp.ui.composable.RegisterScreen
import com.marcos.myspentapp.ui.composable.RegisterScreenPart2
import com.marcos.myspentapp.ui.theme.MySpentAppTheme
import com.marcos.myspentapp.ui.viewmodel.GastoViewModel
import com.marcos.myspentapp.ui.viewmodel.GastoViewModelFactory
import com.marcos.myspentapp.ui.viewmodel.UserViewModel
import com.marcos.myspentapp.ui.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // Checagem do tema do dispositivo
            /*val systemDark = isSystemInDarkTheme()
            LaunchedEffect(Unit) {
                userViewModel.setDarkTheme(systemDark)
            }

            val isDarkMode = userViewModel.userState.darkTheme
            */
            MySpentAppTheme {
                AppNavigation()
            }
        }
    }
}

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val REGISTER2 = "register2"
    const val MAIN = "main"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
}

@Composable
fun AppNavigation(
    gastoViewModel: GastoViewModel = viewModel(
        factory = GastoViewModelFactory(
            LocalContext.current
        ),
    ),
    userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            LocalContext.current
        ),
    )
)
 {
    val navController = rememberNavController()
    var showBottomBar by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

     Scaffold(

         bottomBar = {
             if (showBottomBar) {
                 BottomBar(
                     selectedItem = selectedIndex,
                     onItemSelected = { index ->
                         selectedIndex = index
                         when (index) {
                             0 -> navController.navigate(Routes.MAIN)
                             1 -> navController.navigate(Routes.PROFILE)
                         }
                     }
                 )
             }
         }
     ) { padding ->

         // Navegação do APP
         NavHost(
             navController = navController,
             startDestination = Routes.LOGIN,
             modifier = Modifier.padding(padding)
         ) {

             composable(Routes.LOGIN) {
                 showBottomBar = false
                 LoginScreen(
                     navController = navController,
                     userViewModel = userViewModel
                 )
             }
             composable(Routes.REGISTER) {
                 showBottomBar = false
                 RegisterScreen(
                     navController = navController,
                     userViewModel = userViewModel
                 )
             }
             composable(Routes.REGISTER2) {
                 showBottomBar = false
                 RegisterScreenPart2(
                     navController = navController,
                     userViewModel = userViewModel
                 )
             }
             composable(Routes.MAIN) {
                 showBottomBar = true
                 MySpentApp(
                     gastoViewModel = gastoViewModel,
                     userViewModel = userViewModel
                 )
             }
             composable(Routes.PROFILE) {
                 showBottomBar = true
                 PerfilScreen(
                     navController = navController,
                     userViewModel = userViewModel,
                     gastoViewModel = gastoViewModel
                 )
             }
             composable(Routes.EDIT_PROFILE) {
                 showBottomBar = false
                 PerfilEdit(
                     navController = navController,
                     userViewModel = userViewModel
                 )
             }
         }
     }
}