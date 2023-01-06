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

package bmb.kokos.xplat

import bmb.kokos.service.InlineService
import bmb.kokos.service.loadService
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screens.Screen
import java.nio.file.Path
import java.util.*

interface Mod {
    val namespace: String
    val version: String
    val name: String
    val description: String

    /**
     * Gets the logo file path of the mod.
     *
     * @param preferredSize The preferred logo size, only used in fabric.
     * @return The logo file path relative to the file.
     */
    fun getLogoFile(preferredSize: Int): Optional<String>

    /**
     * Gets a list of all possible root paths for the mod.
     * This is especially relevant on fabric, as a single mod
     * may have multiple source sets (such as client / server-specific ones),
     * each corresponding to one root path.
     *
     * @return A list of root paths belonging to the mod.
     */
    val filePaths: List<Path>

    /**
     * Gets a nio path to the given resource contained within
     * the mod file / folder. The path is verified to exist,
     * and an empty optional is returned if it doesn't.
     *
     * @param path The resource to search for.
     * @return The path of the resource if it exists, or [Optional.empty]
     * if it doesn't.
     */
    fun findResource(vararg path: String): Optional<Path>
    val authors: Collection<String>
    val license: Collection<String>?
    val homepage: Optional<String>
    val sources: Optional<String>
    val issueTracker: Optional<String>

    @Environment(EnvType.CLIENT)
    fun registerConfigurationScreen(provider: ConfigurationScreenProvider)

    @Environment(EnvType.CLIENT)
    interface ConfigurationScreenProvider {
        fun provide(parent: Screen): Screen
    }

    @InlineService
    companion object {
        private val self: Mod by lazy { loadService() }

        @JvmStatic
        fun instance(): Mod = self
        inline val namespace: String get() = instance().namespace
        inline val version: String get() = instance().version
        inline val name: String get() = instance().name
        inline val description: String get() = instance().description

        /**
         * Gets the logo file path of the mod.
         *
         * @param preferredSize The preferred logo size, only used in fabric.
         * @return The logo file path relative to the file.
         */
        fun getLogoFile(preferredSize: Int): Optional<String> = instance().getLogoFile(preferredSize)

        /**
         * Gets a list of all possible root paths for the mod.
         * This is especially relevant on fabric, as a single mod
         * may have multiple source sets (such as client / server-specific ones),
         * each corresponding to one root path.
         *
         * @return A list of root paths belonging to the mod.
         */
        inline val filePaths: List<Path> get() = instance().filePaths

        /**
         * Gets a nio path to the given resource contained within
         * the mod file / folder. The path is verified to exist,
         * and an empty optional is returned if it doesn't.
         *
         * @param path The resource to search for.
         * @return The path of the resource if it exists, or [Optional.empty]
         * if it doesn't.
         */
        fun findResource(vararg path: String): Optional<Path> = instance().findResource(*path)
        inline val authors: Collection<String> get() = instance().authors
        inline val license: Collection<String>? get() = instance().license
        inline val homepage: Optional<String> get() = instance().homepage
        inline val sources: Optional<String> get() = instance().sources
        inline val issueTracker: Optional<String> get() = instance().issueTracker

        @Environment(EnvType.CLIENT)
        fun registerConfigurationScreen(provider: ConfigurationScreenProvider) =
            instance().registerConfigurationScreen(provider)
    }
}
