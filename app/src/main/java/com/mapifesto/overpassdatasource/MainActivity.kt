package com.mapifesto.overpassdatasource

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mapifesto.domain.BoundingBox
import com.mapifesto.domain.OsmTag
import com.mapifesto.domain.OverpassElement
import com.mapifesto.domain.OverpassElements
import com.mapifesto.overpass_datasource.*
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

    var page by remember { mutableStateOf("start") }
    var showWhat by remember { mutableStateOf("") }
    var errorMsg by remember {mutableStateOf("")}
    var element by remember { mutableStateOf<OverpassElement?>(null)}
    var overpassElements by remember { mutableStateOf<OverpassElements?>(null)}
    var southWest by remember { mutableStateOf("55.702224, 13.193593")}
    var northEast by remember { mutableStateOf("55.702998, 13.195188")}
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column() {

            when(page) {

                "start" -> {
                    Text(text = "OVERPASS")
                    Button(
                        onClick = {
                            page = "element"
                        },
                    ) {
                        Text(text = "Get Element")
                    }
                    Button(
                        onClick = {
                            page = "name"
                        },
                    ) {
                        Text(text = "Items with name")
                    }
                    Button(
                        onClick = {
                            page = "tagRestriction"
                        },
                    ) {
                        Text(text = "Items with tag-restriction")
                    }
                }

                "element" -> {
                    Button(
                        onClick = {
                            page = "start"
                        },
                    ) {
                        Text(text = "Start")
                    }

                    Button(
                        onClick = {
                            showWhat = "spinner"
                            errorMsg = ""
                            overpassIntermediary.getElementByIdAndType(53037, "relation") {
                                when(it) {
                                    is OverpassDataState.Error ->{
                                        errorMsg = it.error
                                    }
                                    is OverpassDataState.Data -> {
                                        element = it.data
                                        showWhat = "element"
                                    }
                                }
                            }

                        }
                    ) {
                        Text("Element")
                    }

                    if(errorMsg != "") Text("Error: $errorMsg") else {
                        when(showWhat) {
                            "element" -> {
                                element!!.tags().tags.forEach {
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

                "name" -> {
                    Button(
                        onClick = {
                            page = "start"
                        },
                    ) {
                        Text(text = "Start")
                    }

                    Row(
                        modifier = Modifier.height(30.dp)

                    ) {
                        Text("SouthWest: ")
                        BasicTextField(
                            value = southWest,
                            onValueChange = {
                                southWest = it
                            },
                        )
                    }
                    Row(
                        modifier = Modifier.height(30.dp)

                    ) {
                        Text("NorthEast: ")
                        BasicTextField(
                            value = northEast,
                            onValueChange = {
                                northEast = it
                            },
                        )
                    }
                    Button(
                        onClick = {
                            showWhat = ""
                            errorMsg = ""
                            overpassIntermediary.getElementsWithNameInBoundingBox(
                                boundingBox = BoundingBox.createFromStrings(southWest, northEast)!!
                            ) {
                                when(it) {
                                    is OverpassDataState.Error -> { errorMsg = it.error}
                                    is OverpassDataState.Data -> {
                                        showWhat = "itemsWithName"
                                        overpassElements = it.data
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Get names")
                    }

                    if(errorMsg != "") Text("Error: $errorMsg") else {
                        when(showWhat) {
                            "itemsWithName" -> {

                                LazyColumn(
                                    state = rememberLazyListState()
                                ) {
                                    items(overpassElements!!.items.map { "${it.tags.name} (${it.tags.type?.nameEn?: "unknown type)"})"}) {
                                        Text(it)
                                    }

                                }
                            }
                        }

                    }
                }

                "tagRestriction" -> {
                    Button(
                        onClick = {
                            page = "start"
                        },
                    ) {
                        Text(text = "Start")
                    }
                    Row(
                        modifier = Modifier.height(30.dp)

                    ) {
                        Text("SouthWest: ")
                        BasicTextField(
                            value = southWest,
                            onValueChange = {
                                southWest = it
                            },
                        )
                    }
                    Row(
                        modifier = Modifier.height(30.dp)

                    ) {
                        Text("NorthEast: ")
                        BasicTextField(
                            value = northEast,
                            onValueChange = {
                                northEast = it
                            },
                        )
                    }
                    Button(
                        onClick = {
                            showWhat = ""
                            errorMsg = ""
/*                            val x = IntersectionOfOverpassTagRestrictions(
                                restrictionUnions = listOf(
                                    UnionOfOverpassTagRestrictions(
                                        restrictions = listOf(
                                            OverpassTagRestriction(
                                                key = "key1",
                                                value = "value11",
                                            ),
                                            OverpassTagRestriction(
                                                key = "key1",
                                                value = "value12",
                                            ),
                                            OverpassTagRestriction(
                                                key = "key1",
                                                value = "value13",
                                            ),
                                        ),

                                        ),
                                    UnionOfOverpassTagRestrictions(
                                        restrictions = listOf(
                                            OverpassTagRestriction(
                                                key = "key2",
                                                value = "value2",
                                            ),
                                        ),

                                    ),

                                    UnionOfOverpassTagRestrictions(
                                        restrictions = listOf(
                                            OverpassTagRestriction(
                                                key = "key3",
                                                value = "value31",
                                            ),
                                            OverpassTagRestriction(
                                                key = "key3",
                                                value = "value32",
                                            ),
                                        ),

                                    ),
                                    UnionOfOverpassTagRestrictions(
                                        restrictions = listOf(
                                            OverpassTagRestriction(
                                                key = "key4",
                                                value = "value41",
                                            ),
                                            OverpassTagRestriction(
                                                key = "key3",
                                                value = "value42",
                                            ),
                                            OverpassTagRestriction(
                                                key = "key3",
                                                value = "value43",
                                            ),
                                        ),

                                    ),
                                )
                            )
                            val y = x.overpassString()*/

                            overpassIntermediary.getElementsSatisfyingTagRestrictions(
                                boundingBox = BoundingBox.createFromStrings(southWest, northEast)!!,
                                tagRestrictions = IntersectionOfOverpassTagRestrictions(
                                    restrictionUnions = listOf(
                                        UnionOfOverpassTagRestrictions(
                                            restrictions = listOf(
                                                OverpassTagRestriction(
                                                    key = "amenity",
                                                    value = "cafe",
                                                )
                                            )
                                        ),
                                        UnionOfOverpassTagRestrictions(
                                            restrictions = listOf(
                                                OverpassTagRestriction(
                                                    key = "wheelchair",
                                                    value = "yes",
                                                ),
                                                OverpassTagRestriction(
                                                    key = "wheelchair",
                                                    value = "limited",
                                                ),
                                            )
                                        )
                                    )
                                )
                            ) {
                                when(it) {
                                    is OverpassDataState.Error -> { errorMsg = it.error}
                                    is OverpassDataState.Data -> {
                                        showWhat = "itemsWithName"
                                        overpassElements = it.data
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Get items")
                    }

                    if(errorMsg != "") Text("Error: $errorMsg") else {
                        when(showWhat) {
                            "itemsWithName" -> {

                                LazyColumn(
                                    state = rememberLazyListState()
                                ) {
                                    items(overpassElements!!.items.map { "${it.tags.name} (${it.tags.type?.nameEn?: "unknown type)"})"}) {
                                        Text(it)
                                    }

                                }
                            }
                        }

                    }

                }
            }
        }
    }
}