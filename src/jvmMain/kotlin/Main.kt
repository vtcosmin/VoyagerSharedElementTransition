import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skydoves.orbital.*
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource


class BaseScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Button(onClick = {
            navigator.push(
                ScreenA(
                    listOf(
                        "https://miro.medium.com/v2/resize:fit:1400/1*QjQTzohDz9gznswKpuqUXQ.jpeg",
                        "https://miro.medium.com/v2/resize:fit:1400/1*QjQTzohDz9gznswKpuqUXQ.jpeg",
                        "https://miro.medium.com/v2/resize:fit:1400/1*QjQTzohDz9gznswKpuqUXQ.jpeg"
                    )
                )
            )
        }) {
            Text("Go To Screen A")
        }
    }
}


@Composable
fun SharedImage(imageUrl: String, modifier: Modifier, orbitalScope: OrbitalScope) {
    Box(modifier = modifier.aspectRatio(1.77f)) {
            KamelImage(
                resource = asyncPainterResource(data = imageUrl),
                contentDescription = "",
                modifier = Modifier
                    .animateSharedElementTransition(
                        orbitalScope,
                        SpringSpec(stiffness = 500f, dampingRatio = Spring.DampingRatioMediumBouncy),
                        SpringSpec(stiffness = 500f, dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
            )
    }
}

data class ScreenA(val images: List<String>) : Screen {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Orbital {
            Column {
                for (image in images) {
                    val shareImage = rememberContentWithOrbitalScope {
                        SharedImage(image, Modifier.width(200.dp), this)
                    }
                    Box(Modifier.onClick { navigator.push(ScreenB(shareImage)) }) {
                        shareImage()
                    }

                }
            }
        }
    }
}

data class ScreenB(val sharedContent: @Composable() (OrbitalScope.() -> Unit)) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column(modifier = Modifier.width(300.dp)) {
            Button(onClick = { navigator.pop() }) {
                Text("Go to Screen A")
            }
            Orbital {
                sharedContent()
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {

        Navigator(BaseScreen())
    }
}
