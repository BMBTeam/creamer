package bmb.kokos.logging

import org.slf4j.Marker
import org.slf4j.MarkerFactory

object LogTags {
    @JvmField
    val API: Marker = MarkerFactory.getMarker("API")
}
