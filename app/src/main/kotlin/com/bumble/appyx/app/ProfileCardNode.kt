package com.bumble.appyx.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

class ProfileCardNode(
    buildContext: BuildContext,
    private val imageUrl: Int,
    private val name: String,
    private val age: Int
) : Node(buildContext) {

    @Composable
    @Override
    override fun View(modifier: Modifier) {
        ProfileCard(imageUrl, name, age, modifier)
    }
}

@Composable
fun ProfileCard(
    imageUrl: Int,
    name: String,
    age: Int,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.clip(shape = RoundedCornerShape(20.dp))
    ) {
        if (maxHeight < threshold) {
            ProfileImage(imageUrl)
        } else {
            ProfileImage(imageUrl = imageUrl)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            ) {
                Text(text = "$name, $age", color = Color.White, fontSize = 22.sp)
                Spacer(modifier = Modifier.requiredHeight(4.dp))
                Text(text = "London", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ProfileImage(imageUrl: Int) {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = imageUrl),
        contentDescription = null
    )
//    AsyncImage(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(imageUrl)
//            .crossfade(true)
//            .build(),
//        contentDescription = null,
//        contentScale = ContentScale.Crop,
//        modifier = Modifier
//            .fillMaxSize()
//            .clip(RoundedCornerShape(8.dp))
//    )
}

private val threshold = 600.dp
