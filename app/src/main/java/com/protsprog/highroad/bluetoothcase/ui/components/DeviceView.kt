package com.protsprog.highroad.bluetoothcase.ui.components

import android.bluetooth.BluetoothDevice.BOND_BONDED
import android.bluetooth.BluetoothDevice.BOND_BONDING
import android.bluetooth.BluetoothDevice.BOND_NONE
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun DeviceView(
    modifier: Modifier = Modifier,
    name: String = "",
    address: String = "",
    boundState: Int = 0,
    onClickAction: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClickAction),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Device: ${name}", fontSize = 18.sp)
            Text(text = address)
        }

        Text(
            text = when (boundState) {
                BOND_NONE -> "Not bonded"
                BOND_BONDING -> "Bonding is in progress"
                BOND_BONDED -> "Is bonded"
                else -> "Unknow"
            }
        )
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun DeviceViewPreview() {
    DeviceView(
        name = "My device",
        address = "DD:EE:22:55:66:99",
        boundState = 12
    )
}
