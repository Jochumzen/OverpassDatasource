package com.mapifesto.overpassdatasource

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mapifesto.domain.OsmElement
import com.mapifesto.overpass_datasource.OverpassDataState
import com.mapifesto.overpass_datasource.OverpassIntermediary
import com.mapifesto.overpassdatasource.ui.theme.OverpassDatasourceTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var overpassIntermediary: OverpassIntermediary

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            OverpassDatasourceTheme {
               Compose(overpassIntermediary = overpassIntermediary)
            }
        }
    }
}

@Composable
fun Compose(
    overpassIntermediary: OverpassIntermediary
) {

    var showWhat by remember { mutableStateOf("") }
    var error by remember {mutableStateOf(false)}
    var element by remember { mutableStateOf<OsmElement?>(null)}

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column() {

            Row() {

                Button(
                    onClick = {
                        showWhat = "spinner"
                        error = false
                        overpassIntermediary.getElementByIdAndType(1019648199, "way") {
                            when(it) {
                                is OverpassDataState.Error ->{
                                    error = true
                                }
                                is OverpassDataState.OverpassData -> {
                                    element = it.data
                                    showWhat = "element"
                                }
                            }
                        }

                    }
                ) {
                    Text("Element")
                }

            }

            if(error) Text("Error") else {
                when(showWhat) {
                    "element" -> {
                        element!!.osmTags().tags.forEach {
                            Text("${it.key}: ${it.value}")
                        }
                    }
                    "spinner" -> {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}