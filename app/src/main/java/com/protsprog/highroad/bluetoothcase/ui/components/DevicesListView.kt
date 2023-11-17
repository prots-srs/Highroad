package com.protsprog.highroad.bluetoothcase.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.protsprog.highroad.R
import com.protsprog.highroad.bluetoothcase.StatePairDevice

@Composable
fun DevicesListView(
    modifier: Modifier = Modifier,
    pairedList: List<StatePairDevice> = emptyList(),
    foundList: List<StatePairDevice> = emptyList(),
    onClickPaired: (String) -> Unit = {},
    onClickBonding: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        val deviceListState = rememberLazyListState()
        LazyColumn(
//            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            state = deviceListState
        ) {
            if (pairedList.isNotEmpty()) {
                item {
                    Text("Paired devices:")
                }
            }
            items(pairedList) {
                DeviceView(
                    name = it.name,
                    address = it.address,
                    boundState = it.boundState,
                    onClickAction = { onClickPaired(it.address) }
                )
            }
            if (foundList.isNotEmpty()) {
                item {
                    Text("Found devices:")
                }
            }
            items(foundList) {
                DeviceView(
                    name = it.name,
                    address = it.address,
                    boundState = it.boundState,
                    onClickAction = { onClickBonding(it.address) }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun SettingViewPeview() {
    DevicesListView(
        pairedList = listOf(
            StatePairDevice(
                name = "Fora signetic sldk 56",
                address = "DD:EE:22:55:66:99",
                boundState = 1
            ),
            StatePairDevice(
                name = "Nikolla poderom",
                address = "DD:EE:22:55:66:99",
                boundState = 2
            ),
            StatePairDevice(
                name = "Mokirjt lodipoe kiemn",
                address = "DD:EE:22:55:66:99",
                boundState = 3
            ),
            StatePairDevice(
                name = "Sonik",
                address = "DD:EE:22:55:66:99",
                boundState = 4
            )
        )
    )
}
