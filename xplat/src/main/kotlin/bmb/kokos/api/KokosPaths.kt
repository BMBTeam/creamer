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

import bmb.kokos.xplat.Xplat
import java.nio.file.Files
import java.nio.file.Path

object KokosPaths {
    private val game: Path = Xplat.gameFolder

    @JvmField
    val KOKOS: Path = game.resolve("kokos")

    @JvmField
    val CONFIG: Path = KOKOS.resolve("config")

    @JvmField
    val DATA: Path = KOKOS.resolve("data")

    @JvmField
    val DATAPACKS: Path = KOKOS.resolve("datapack")

    @JvmField
    val RESOURCEPACKS: Path = KOKOS.resolve("resourcepacks")

    @JvmField
    val RESOURCES: Path = KOKOS.resolve("resources")

    init {
        runCatching {
            Files.createDirectories(KOKOS)
            Files.createDirectories(CONFIG)
            Files.createDirectories(DATA)
            Files.createDirectories(DATAPACKS)
            Files.createDirectories(RESOURCEPACKS)
            Files.createDirectories(RESOURCES)
        }
    }
}
