package bmb.kokos.util.io

import bmb.kokos.api.KokosAPI
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Decodes and deserializes the given [file] to the value of type [T]
 * using deserializer retrieved from the reified type parameter.
 */
@JvmSynthetic
inline fun <reified T> StringFormat.decodeFromFile(file: File): T = BufferedReader(FileReader(file)).use { reader ->
    KokosAPI.JSON.decodeFromString(reader.readText())
}

/**
 * Serializes and encodes the given [value] to string
 * using serializer retrieved from the reified type parameter.
 * Then write to [file].
 */
@JvmSynthetic
inline fun <reified T> StringFormat.encodeToFile(value: T, file: File) =
    BufferedWriter(FileWriter(file)).use { writer -> writer.write(KokosAPI.JSON.encodeToString(value)) }
