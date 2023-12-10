package com.protsprog.highroad.articles.ui.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ArticleItemModel
import com.protsprog.highroad.articles.ArticlePutErrorsUiState
import com.protsprog.highroad.articles.ArticlePutModel
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme
import com.protsprog.highroad.authentication.ErrorForInput
import java.util.Objects

@Composable
fun ArticleDetailItemEdit(
    modifier: Modifier = Modifier,
    item: ArticleItemModel,
    errors: ArticlePutErrorsUiState,
    onClickSubmit: (ArticlePutModel) -> Unit = {},
    takePhoto: () -> Unit = {}
) {
    val (itemPublish, onStateChangePublish) = rememberSaveable { mutableStateOf(item.publish) }
    var sortIndex: String by rememberSaveable { mutableStateOf("${item.sort}") }
    var itemName: String by rememberSaveable { mutableStateOf(item.title) }
    var itemDescription: String by rememberSaveable { mutableStateOf(item.description ?: "") }

//    val file = context.createImageFile()

//    val uri = FileProvider.getUriForFile(
//        Objects.requireNonNull(getApplicationContext(context)),
//        "com.app.id.fileProvider", file
//    )

//    var imageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }

//    val cameraLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture(),
//        onResult = { success ->
//            if (success) imageUri = uri
//            if (imageUri.toString().isNotEmpty()) {
//                Log.d("myImageUri", "$imageUri ")
//            }
//        }
//    )

//    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    /*val mediaPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) listOf(
            android.Manifest.permission.READ_MEDIA_IMAGES
        ) else listOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )*/

//    val hasCameraPermission = cameraPermissionState.status.isGranted
//    val hasMediaPermission = mediaPermissionState.allPermissionsGranted


    Column {
//        publish
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .toggleable(
                    value = itemPublish,
                    onValueChange = { onStateChangePublish(!itemPublish) },
                    role = Role.Checkbox
                )
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = itemPublish, onCheckedChange = null)
            Text(
                text = "Publish",
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
//        sort
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = sortIndex,
            label = { Text("Sort Index") },
            singleLine = true,
            onValueChange = {
                sortIndex = it
            },
            isError = !errors.sort.isNullOrEmpty()
        )
        AnimatedVisibility(!errors.sort.isNullOrEmpty()) {
            ErrorForInput(text = errors.sort.toString())
        }
//        title
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = itemName,
            label = { Text("Title") },
            singleLine = true,
            onValueChange = {
                itemName = it
            },
            isError = !errors.title.isNullOrEmpty()
        )
        AnimatedVisibility(!errors.title.isNullOrEmpty()) {
            ErrorForInput(text = errors.title.toString())
        }
//        description
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = itemDescription,
            minLines = 6,
            maxLines = 18,
            onValueChange = { itemDescription = it },
            label = { Text("Description") },
            isError = !errors.description.isNullOrEmpty()
        )
        AnimatedVisibility(!errors.description.isNullOrEmpty()) {
            ErrorForInput(text = errors.description.toString())
        }

        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    /*if(hasMediaPermission){
                        galleryLauncher.launch(galleryIntent)
                    } else {
                        mediaPermissionState.launchMultiplePermissionRequest()
                    }*/
                }
            ) {
                Text(
                    text = "From gallery",
                    style = MaterialTheme.typography.bodyLarge,
                )

            }

            Button(
                onClick = takePhoto
            ) {
                Text(
                    text = "Make photo",
                    style = MaterialTheme.typography.bodyLarge,
                )

            }

        }
//        submit
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    /*onClickSubmit(
                        ArticlePutModel(
                            id = item.id,
                            publish = itemPublish,
                            sort = sortIndex,
                            title = itemName,
                            description = itemDescription,
                            picture =
                        )
                    )*/
                }
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.bodyLarge,
                )

            }
        }

        AnimatedVisibility(!errors.common.isNullOrEmpty()) {
            ErrorForInput(text = errors.common.toString())
        }
    }
}

@Preview(
//    showBackground = true,
    device = "id:pixel_5",
    backgroundColor = 0xFF995533,
)
@Composable
fun ArticleDetailItemEditPreview() {
    ArticlesTheme {
        ArticleDetailItemEdit(
            item = ArticleItemModel(
                title = "lorem ipsum lorem ipsum 234",
                sort = 10,
                picture = "https://protsprog.com/storage/articles/JwJMDBplxxrOrEyvcVvR4BjwDH2QpIMm5R9VsGBF.jpg",
                description = "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsumlorem ipsum",
                id = 123,
                publish = true
            ),
            errors = ArticlePutErrorsUiState(
                sort = "The sort field must consist only integer.",
                title = "The title field is required.",
                description = "The description field is required."
            )
        )
    }
}