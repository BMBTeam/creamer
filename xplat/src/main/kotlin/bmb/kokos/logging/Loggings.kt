package bmb.kokos.logging

import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.MarkerManager

object Loggings {
    @JvmField
    val API: Marker = MarkerManager.getMarker("API")
}
