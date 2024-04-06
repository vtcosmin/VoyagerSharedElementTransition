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


@Composable
fun SharedImage(imageUrl: String, orbitalScope: OrbitalScope) {
    KamelImage(
        resource = asyncPainterResource(data = imageUrl),
        contentDescription = "",
        modifier = Modifier
            .animateSharedElementTransition(
                orbitalScope,
                SpringSpec(stiffness = 200f, dampingRatio = Spring.DampingRatioLowBouncy),
                SpringSpec(stiffness = 200f, dampingRatio = Spring.DampingRatioLowBouncy)
            )
    )
}

class ScreenA : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val sharedImages = listOf(
            "https://upload.wikimedia.org/wikipedia/commons/2/28/030106-IMG_0819-HozMarreh-2.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/7/72/Beautiful_scenery_in_Attappadi.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/e/ea/030106-IMG_0700-Qom-Garmsar-2.jpg"
        ).map { image ->
            rememberContentWithOrbitalScope {
                SharedImage(image, this)
            }
        }

        Button(onClick = {
            navigator.push(
                ScreenB(sharedImages)
            )
        }) {
            Text("Go To Screen B")
        }
    }
}

data class ScreenB(val images: List<@Composable() (OrbitalScope.() -> Unit)>) : Screen {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column {
            Button(onClick = { navigator.pop() }) {
                Text("Go to Screen A")
            }
            for (image in images) {
                Orbital(Modifier.width(200.dp).aspectRatio(1.77f).onClick { navigator.push(ScreenC(image)) }) {
                    image()
                }
            }
        }
    }
}

data class ScreenC(val sharedContent: @Composable() (OrbitalScope.() -> Unit)) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Column(modifier = Modifier.width(300.dp)) {
            Button(onClick = { navigator.pop() }) {
                Text("Go to Screen B")
            }
            Orbital(modifier = Modifier.width(500.dp)) {
                sharedContent()
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        Navigator(ScreenA())
    }
}
