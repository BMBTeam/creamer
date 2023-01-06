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

package bmb.kokos.fabric.xplat

import bmb.kokos.xplat.Mod
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata
import net.fabricmc.loader.api.metadata.Person
import java.nio.file.Path
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FabricMod(namespace: String) : Mod {
    private val container: ModContainer
    private val metadata: ModMetadata

    init {
        container = FabricLoader.getInstance().getModContainer(namespace).orElseThrow()
        metadata = container.metadata
    }

    override val namespace: String = metadata.id
    override val version: String = metadata.version.friendlyString
    override val name: String = metadata.name
    override val description: String = metadata.description
    override fun getLogoFile(preferredSize: Int): Optional<String> = metadata.getIconPath(preferredSize)
    override val filePaths: List<Path> = container.rootPaths
    override fun findResource(vararg path: String): Optional<Path> = container.findPath(path.joinToString("/"))
    override val authors: Collection<String> = metadata.authors.map(Person::getName).toList()
    override val license: Collection<String>? = metadata.license
    override val homepage: Optional<String> = metadata.contact.get("homepage")
    override val sources: Optional<String> = metadata.contact.get("sources")
    override val issueTracker: Optional<String> = metadata.contact.get("issues")
    override fun registerConfigurationScreen(provider: Mod.ConfigurationScreenProvider) {
        if (CONFIG_SCREENS.containsKey(namespace)) throw IllegalStateException(
            "Can not register configuration screen for mod '$namespace' because it was already registered!"
        )
        CONFIG_SCREENS[namespace] = provider
    }

    companion object {
        @JvmField
        val CONFIG_SCREENS: MutableMap<String, Mod.ConfigurationScreenProvider> = ConcurrentHashMap()
    }
}
