/*
 * This file is part of BMB
 * Copyright (C) 2022, 2023 BMB
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package bmb.kokos.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.serialization.json.Json
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object KokosAPI {
    const val NAMESPACE: String = "kokos"
    const val NAME: String = "Kokos"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(NAME)

    @JvmField
    val LOG: org.apache.logging.log4j.Logger = LogManager.getLogger(NAME)

    @JvmField
    val JSON: Json = Json {
        prettyPrint = true
    }

    @JvmField
    val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

    @JvmField
    var minecraft: Minecraft = Minecraft.getInstance()

    @JvmStatic
    internal fun id(id: String): ResourceLocation = ResourceLocation(NAMESPACE, id)

    @JvmStatic
    fun id(namespace: String, id: String): ResourceLocation = ResourceLocation(namespace, id)
}
