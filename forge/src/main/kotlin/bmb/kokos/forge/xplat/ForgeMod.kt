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

package bmb.kokos.forge.xplat

import bmb.kokos.xplat.Mod
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo
import net.minecraftforge.forgespi.language.IModInfo
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class ForgeMod(namespace: String) : Mod {
    private val container: ModContainer
    private val info: IModInfo

    init {
        container = ModList.get().getModContainerById(namespace).orElseThrow()
        info = ModList.get().mods
            .filter { modInfo -> modInfo.modId == namespace }
            .stream().findAny().orElseThrow()
    }

    override val namespace: String = info.modId
    override val version: String = info.version.toString()
    override val name: String = info.displayName
    override val description: String = info.description
    override fun getLogoFile(preferredSize: Int): Optional<String> = info.logoFile
    override val filePaths: List<Path> = listOf(info.owningFile.file.secureJar.rootPath)
    override fun findResource(vararg path: String): Optional<Path> =
        Optional.of(info.owningFile.file.findResource(*path)).filter(Files::exists)

    override val authors: Collection<String>
        get() {
            val optional = info.config.getConfigElement<String>("authors").map { it }
            return if (optional.isPresent) Collections.singleton(optional.get()) else Collections.emptyList()
        }
    override val license: Collection<String>? = Collections.singleton(info.owningFile.license)
    override val homepage: Optional<String> = info.config.getConfigElement<String?>("displayURL").map { it }
    override val sources: Optional<String> = Optional.empty()
    override val issueTracker: Optional<String>
        get() {
            val owingFile = info.owningFile
            if (owingFile is ModFileInfo) return Optional.ofNullable(owingFile.issueURL).map(URL::toString)
            return Optional.empty()
        }

    override fun registerConfigurationScreen(provider: Mod.ConfigurationScreenProvider) {
        container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
            ConfigScreenHandler.ConfigScreenFactory { _, screen -> provider.provide(screen) }
        }
    }
}
