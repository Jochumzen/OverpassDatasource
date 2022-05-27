package com.mapifesto.overpassdatasource

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mapifesto.overpass_datasource.DataState
import com.mapifesto.overpass_datasource.Node
import com.mapifesto.overpass_datasource.OverpassInteractors
import com.mapifesto.overpass_datasource.OverpassIntermediary
import com.mapifesto.overpassdatasource.ui.theme.OverpassDatasourceTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OverpassDatasourceTheme {
               Compose()
            }
        }
    }
}

@Composable
fun Compose() {

    var showWhat by remember { mutableStateOf("") }
    var error by remember {mutableStateOf(false)}
    var node by remember { mutableStateOf<Node?>(null)}

    val overpass = OverpassIntermediary()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column() {

            Row() {

                Button(
                    onClick = {
                        showWhat = ""
                        error = false
                        overpass.getNodeById(1222870560) {
                            if (it.node != null) {
                                node = it.node
                                showWhat = "node"
                            } else {
                                error = true
                            }
                        }

                    }
                ) {
                    Text("Node")
                }

            }

            if(error) Text("Error") else {
                when(showWhat) {
                    "node" -> {
                        node!!.tags.forEach {
                            Text("${it.key}: ${it.value}")
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}