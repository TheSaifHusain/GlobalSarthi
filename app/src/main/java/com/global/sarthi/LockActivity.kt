package com.global.sarthi

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.global.sarthi.db.DataStoreManager
import com.global.sarthi.network.ApiRepository
import com.global.sarthi.ui.theme.GlobalSarthiTheme
import com.global.sarthi.utils.Utils
import com.global.sarthi.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class LockActivity : ComponentActivity() {

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var apiRepository: ApiRepository

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                try{
                    dataStoreManager.getToken().collect {
                        val requestBody: MutableMap<String, String> = HashMap()
                        requestBody["imei"] = viewModel.imei.value
                        requestBody["deviceId"] = viewModel.gsfId.value
                        requestBody["mac"] = viewModel.mac.value
                        requestBody["fcmToken"] = it
                        delay(2000)
                        apiRepository.postData(requestBody)
                        Log.e("SAVED_TOKEN", it)
                    }
                }catch (e: Exception){
                    e.localizedMessage?.let { Log.e("ERROR", it) }
                }
            }
           GlobalSarthiTheme {
               LockScreen(this)
           }
        }
        }
    @SuppressLint("InlinedApi")
    @Composable
    fun LockScreen(lockActivity: LockActivity) {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)) {
            val (header,logo,call,wifi) = createRefs()
            Image(painter = painterResource(id = R.drawable.back), contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            Image(painter = painterResource(id = R.drawable.call), contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth(0.2f)
                    .constrainAs(call) {
                        top.linkTo(logo.top)
                        start.linkTo(parent.start)
                        end.linkTo(logo.start)
                        bottom.linkTo(logo.bottom)
                    }
                    .clickable {

                    })
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth(0.5f)
                    .constrainAs(logo) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(header.bottom)
                        bottom.linkTo(parent.bottom)
                    })
            Image(painter = painterResource(id = R.drawable.wifi), contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth(0.2f)
                    .constrainAs(wifi) {
                        start.linkTo(logo.end)
                        end.linkTo(parent.end)
                        top.linkTo(logo.top)
                        bottom.linkTo(logo.bottom)
                    }
                    .clickable {
                        try {
                            startActivity(lockActivity, Intent(Settings.Panel.ACTION_WIFI), null)
                        } catch (e: Exception) {
                            e.localizedMessage?.let { Log.e("CLICK_EXCP", it) }
                            startActivity(lockActivity, Intent(Settings.ACTION_WIFI_SETTINGS), null)
                        }
                    })

        }
    }
}



@Preview(showBackground = true)
@Composable
private fun PreviewLockScreen() {
//    LockScreen(this)
}