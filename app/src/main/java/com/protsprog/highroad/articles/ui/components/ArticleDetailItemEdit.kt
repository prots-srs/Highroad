package com.protsprog.highroad.articles.ui.components

/*
TO READ

https://coil-kt.github.io/coil/compose/
 */
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.protsprog.highroad.IMAGE_SOURCES
import com.protsprog.highroad.R
import com.protsprog.highroad.articles.ArticleItemModel
import com.protsprog.highroad.articles.ArticlePutErrorsUiState
import com.protsprog.highroad.articles.ArticlePutModel
import com.protsprog.highroad.articles.ServiceUiState
import com.protsprog.highroad.articles.ui.theme.ArticlesTheme
import com.protsprog.highroad.authentication.ErrorForInput

@Composable
fun ArticleDetailItemEdit(
    modifier: Modifier = Modifier,
    item: ArticlePutModel,
    changeItemPublish: (Boolean) -> Unit = {},
    changeItemSort: (String) -> Unit = {},
    changeItemTitle: (String) -> Unit = {},
    changeItemDescription: (String) -> Unit = {},
    changeItemPicture: (Uri) -> Unit = {},
    changeUseMedia: (Boolean) -> Unit = {},
    changeUseCamera: (Boolean) -> Unit = {},
    errors: ArticlePutErrorsUiState,
    services: ServiceUiState,
    onClickSubmit: () -> Unit = {},
    checkPermissionCameraX: () -> Unit = {},
    permissionCamera: Boolean = false,
    startCamera: (context: Context, lifecycleOwner: LifecycleOwner) -> PreviewView,
    takePhoto: () -> Unit = {},
    checkPermissionPhotoPicker: () -> Unit = {},
    permissionMedia: Boolean = false,
    startPickPhoto: () -> Unit = {},
    outputFiles: Map<IMAGE_SOURCES, Uri>,
    clearOutput: (IMAGE_SOURCES) -> Unit,
) {
    Log.d("TEST_FLOW", "composable put item: ${item}")

    outputFiles.filter { it != Uri.EMPTY }.forEach { k, v ->
        when (k) {
            IMAGE_SOURCES.CAMERA -> {
                if (services.useCamera) {
                    changeItemPicture(v)
                }
            }

            IMAGE_SOURCES.MEDIA -> {
                if (services.useMedia) {
                    changeItemPicture(v)
                }
            }
        }
    }

    Column {
//        publish
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp)
                .toggleable(
                    value = item.publish,
                    onValueChange = changeItemPublish,
                    role = Role.Checkbox
                )
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.publish, onCheckedChange = null)
            Text(
                text = "Publish",
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
//        sort
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = item.sort,
            label = { Text("Sort Index") },
            singleLine = true,
            onValueChange = changeItemSort,
            isError = !errors.sort.isNullOrEmpty()
        )
        AnimatedVisibility(!errors.sort.isNullOrEmpty()) {
            ErrorForInput(text = errors.sort.toString())
        }
//        title
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = item.title,
            label = { Text("Title") },
            singleLine = true,
            onValueChange = changeItemTitle,
            isError = !errors.title.isNullOrEmpty()
        )
        AnimatedVisibility(!errors.title.isNullOrEmpty()) {
            ErrorForInput(text = errors.title.toString())
        }
//        description
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = item.description,
            minLines = 6,
            maxLines = 18,
            onValueChange = changeItemDescription,
            label = { Text("Description") },
            isError = !errors.description.isNullOrEmpty()
        )
        AnimatedVisibility(!errors.description.isNullOrEmpty()) {
            ErrorForInput(text = errors.description.toString())
        }

        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))

        Text(
            text = "Add image:",
            style = MaterialTheme.typography.bodyLarge,
        )

//        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_small)))

        Image(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1f, false)
                .clip(MaterialTheme.shapes.large),
            painter = if (item.picture != Uri.EMPTY) rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = item.picture)
                    .build()
            ) else painterResource(id = R.drawable.ic_broken_image),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_small)))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(modifier = modifier.semantics {
                contentDescription = "Use media"
            }, checked = services.useMedia, onCheckedChange = {
//                useMedia = it
                changeUseMedia(it)
                if (it) {
                    changeUseCamera(false)
//                    useCamera = false
//                    itemPicture = Uri.EMPTY
                    changeItemPicture(Uri.EMPTY)
                    checkPermissionPhotoPicker()
                } else {
                    clearOutput(IMAGE_SOURCES.MEDIA)
                }
            })
            Spacer(modifier.width(dimensionResource(id = R.dimen.padding_medium)))
            Text(text = "From gallery")
        }
        if (permissionMedia) {
            AnimatedVisibility(visible = services.useMedia) {
                Spacer(modifier.height(dimensionResource(id = R.dimen.padding_small)))
                Button(onClick = startPickPhoto) {
                    Text(
                        text = "Pick photo",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        val lifecycleOwner = LocalLifecycleOwner.current
        Column {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(modifier = modifier.semantics {
                    contentDescription = "Use camera"
                }, checked = services.useCamera, onCheckedChange = {
//                    useCamera = it
                    changeUseCamera(it)
                    if (it) {
                        changeUseMedia(false)
//                        useMedia = false
                        changeItemPicture(Uri.EMPTY)
//                        itemPicture = Uri.EMPTY
                        checkPermissionCameraX()
                    } else {
                        clearOutput(IMAGE_SOURCES.CAMERA)
                    }
                })
                Spacer(modifier.width(dimensionResource(id = R.dimen.padding_medium)))
                Text(text = "Use camera")
            }
            if (permissionCamera) {
                Spacer(modifier.height(dimensionResource(id = R.dimen.padding_small)))

                AnimatedVisibility(visible = services.useCamera) {
                    Column {
                        AndroidView(
                            modifier = modifier
                                .fillMaxWidth()
                                .aspectRatio(1f, false)
//            .requiredHeight(360.dp)
                                .clip(MaterialTheme.shapes.large),
                            factory = { ctx ->
                                startCamera(ctx, lifecycleOwner)
                            },
                        )

                        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_small)))
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = takePhoto) {
                                Text(
                                    text = "Take photo",
                                    style = MaterialTheme.typography.bodyMedium,
                                )

                            }

                        }
                    }
                }
            }
        }

//        submit
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        AnimatedVisibility(!errors.common.isNullOrEmpty()) {
            ErrorForInput(text = errors.common.toString())
        }
        Spacer(modifier.height(dimensionResource(id = R.dimen.padding_medium)))

//        Log.d("TEST_FLOW", "composable enableSubmit: ${services.enableSubmit}")

        Row(
            modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onClickSubmit,
//                {
//                    onClickSubmit(
//                        ArticlePutModel(
//                            id = item.id,
//                            publish = itemPublish,
//                            sort = sortIndex,
//                            title = itemName,
//                            description = itemDescription,
//                            picture = itemPicture
//                        )
//                    )
//                },
                enabled = services.enableSubmit
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.bodyLarge,
                )

            }
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
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current

    ArticlesTheme {
        /*ArticleDetailItemEdit(
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
            ),
            permissionCamera = true,
            startCamera = { context, _ -> PreviewView(context) },
            outputFiles = mapOf(
                IMAGE_SOURCES.CAMERA to Uri.EMPTY,
                IMAGE_SOURCES.MEDIA to Uri.EMPTY
            ),
            clearOutput = {},
            services = ServiceUiState(),
//            navigateToArticle = {}
        )*/
    }
}