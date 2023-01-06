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

import bmb.kokos.api.Env
import bmb.kokos.service.loadService
import net.minecraft.SharedConstants
import java.nio.file.Path
import java.util.*

interface Xplat {
    val isFabric: Boolean get() = !isForge
    val isForge: Boolean get() = !isFabric
    val minecraftVersion: String get() = SharedConstants.getCurrentVersion().id

    /**
     * Gets the root directory for the current instance of Minecraft.
     *
     * The returned path is guaranteed to be **absolute** and **normalized**.
     */
    val gameFolder: Path

    /**
     * Gets the main `config` folder for the current instance of Minecraft.
     *
     * The returned path is guaranteed to be **absolute** and **normalized**.
     */
    val configFolder: Path

    /**
     * Gets the `mods folder for the current instance of Minecraft.
     *
     * The returned path is guaranteed to be **absolute** and **normalized**.
     */
    val modsFolder: Path

    /**
     * Checks whether a mod with the given namespace is present.
     *
     * @param namespace The namespace to check.
     * @return `true` if the mod is loaded, `false` otherwise.
     */
    fun isModLoaded(namespace: String): Boolean

    /**
     * Gets a [Mod] container by its namespace.
     *
     * @param namespace The namespace to look for.
     * @return The mod container, if found.
     * @throws NoSuchElementException If no mod with the given namespace exists.
     */
    fun getMod(namespace: String): Mod

    /**
     * Gets a collection of [Mod] containers for all currently-loaded mods.
     *
     * @return A collection of mod containers.
     */
    val mods: Collection<Mod>

    /**
     * Gets a collection of strings representing the namespaces of
     * all currently-loaded mods.
     *
     * @return A collection of all loaded namespaces.
     */
    val namespaces: Collection<String>
    val environment: Env?
    val isDevelopmentEnvironment: Boolean

    companion object {
        private val self: Xplat by lazy { loadService() }

        @JvmStatic
        fun instance(): Xplat = self
        inline val isFabric: Boolean get() = instance().isFabric
        inline val isForge: Boolean get() = instance().isForge
        inline val minecraftVersion: String get() = instance().minecraftVersion

        /**
         * Gets the root directory for the current instance of Minecraft.
         *
         * The returned path is guaranteed to be **absolute** and **normalized**.
         */
        inline val gameFolder: Path get() = instance().gameFolder

        /**
         * Gets the main `config` folder for the current instance of Minecraft.
         *
         * The returned path is guaranteed to be **absolute** and **normalized**.
         */
        inline val configFolder: Path get() = instance().configFolder

        /**
         * Gets the `mods folder for the current instance of Minecraft.
         *
         * The returned path is guaranteed to be **absolute** and **normalized**.
         */
        inline val modsFolder: Path get() = instance().modsFolder

        /**
         * Checks whether a mod with the given namespace is present.
         *
         * @param namespace The namespace to check.
         * @return `true` if the mod is loaded, `false` otherwise.
         */
        fun isModLoaded(namespace: String): Boolean = instance().isModLoaded(namespace)

        /**
         * Gets a [Mod] container by its namespace.
         *
         * @param namespace The namespace to look for.
         * @return The mod container, if found.
         * @throws NoSuchElementException If no mod with the given namespace exists.
         */
        fun getMod(namespace: String): Mod = instance().getMod(namespace)

        /**
         * Optionally gets a [Mod] container by its namespace if it exists.
         *
         * @param namespace The namespace to look for.
         * @return An optional representing the mod container, if found,
         * or an empty optional otherwise.
         */
        fun getOptionalMod(namespace: String): Optional<Mod> = try {
            Optional.of(getMod(namespace))
        } catch (_: NoSuchElementException) {
            Optional.empty()
        }

        /**
         * Gets a collection of [Mod] containers for all currently-loaded mods.
         *
         * @return A collection of mod containers.
         */
        inline val mods: Collection<Mod> get() = instance().mods

        /**
         * Gets a collection of strings representing the namespaces of
         * all currently-loaded mods.
         *
         * @return A collection of all loaded namespaces.
         */
        inline val namespaces: Collection<String> get() = instance().namespaces
        inline val environment: Env? get() = instance().environment
        inline val isDevelopmentEnvironment: Boolean get() = instance().isDevelopmentEnvironment
    }
}
