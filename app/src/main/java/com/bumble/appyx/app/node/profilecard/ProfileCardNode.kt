package com.bumble.appyx.app.node.profilecard

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kotlin.random.Random

class ProfileCardNode(
    buildContext: BuildContext
) : Node(buildContext) {

    @Composable
    @Override
    override fun View(modifier: Modifier) {
        ProfileCard(modifier)
    }
}

val imageUrls = listOf(
    "https://petapixel.com/assets/uploads/2019/02/download.jpeg",
    "https://petapixel.com/assets/uploads/2019/02/download-1.jpeg",
    "https://petapixel.com/assets/uploads/2019/02/download-2.jpeg",
    "https://petapixel.com/assets/uploads/2019/02/download-3.jpeg",
    "https://petapixel.com/assets/uploads/2019/02/download-4.jpeg",
    "https://petapixel.com/assets/uploads/2019/02/download-5.jpeg",
)

@Composable
fun ProfileCard(modifier: Modifier) {
    val imageUrl = remember {
        imageUrls[Random.nextInt(0, 5)]
    }
    BoxWithConstraints(modifier = modifier) {
        if (maxWidth < threshold) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(text = "Lara, 21", color = Color.White, fontSize = 22.sp)
                Spacer(modifier = Modifier.requiredHeight(4.dp))
                Text(text = "London", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

private val threshold = 200.dp