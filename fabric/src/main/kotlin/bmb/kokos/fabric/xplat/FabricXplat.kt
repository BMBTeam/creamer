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

import bmb.kokos.api.Env
import bmb.kokos.xplat.Mod
import bmb.kokos.xplat.Xplat
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.loader.api.metadata.ModMetadata
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

class FabricXplat : Xplat {
    override val isFabric: Boolean = true
    override val gameFolder: Path = FabricLoader.getInstance().gameDir.toAbsolutePath().normalize()
    override val configFolder: Path = FabricLoader.getInstance().configDir.toAbsolutePath().normalize()
    override val modsFolder: Path = gameFolder.resolve("mods")
    override fun isModLoaded(namespace: String): Boolean = FabricLoader.getInstance().isModLoaded(namespace)
    override fun getMod(namespace: String): Mod = MODS.computeIfAbsent(namespace) { FabricMod(namespace) }
    override val mods: Collection<Mod>
        get() {
            for (mod in FabricLoader.getInstance().allMods) getMod(mod.metadata.id)
            return MODS.values
        }
    override val namespaces: Collection<String> = FabricLoader.getInstance().allMods
        .map(ModContainer::getMetadata).map(ModMetadata::getId).toList()
    override val environment: Env? = Env.fromXplat(FabricLoader.getInstance().environmentType)
    override val isDevelopmentEnvironment: Boolean = FabricLoader.getInstance().isDevelopmentEnvironment

    companion object {
        private val MODS: MutableMap<String, Mod> = ConcurrentHashMap()
    }
}
