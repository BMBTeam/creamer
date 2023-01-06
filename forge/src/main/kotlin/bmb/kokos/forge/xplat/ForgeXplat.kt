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

import bmb.kokos.api.Env
import bmb.kokos.xplat.Mod
import bmb.kokos.xplat.Xplat
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.forgespi.language.IModInfo
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

class ForgeXplat : Xplat {
    override val isForge: Boolean = true
    override val gameFolder: Path = FMLPaths.GAMEDIR.get().toAbsolutePath().normalize()
    override val configFolder: Path = FMLPaths.CONFIGDIR.get().toAbsolutePath().normalize()
    override val modsFolder: Path = FMLPaths.MODSDIR.get().toAbsolutePath().normalize()
    override fun isModLoaded(namespace: String): Boolean = ModList.get().isLoaded(namespace)
    override fun getMod(namespace: String): Mod = MODS.computeIfAbsent(namespace) { ForgeMod(namespace) }
    override val mods: Collection<Mod>
        get() {
            for (mod in ModList.get().mods) getMod(mod.modId)
            return MODS.values
        }
    override val namespaces: Collection<String> = ModList.get().mods.map(IModInfo::getModId).toList()
    override val environment: Env? = Env.fromXplat(FMLEnvironment.dist)
    override val isDevelopmentEnvironment: Boolean = !FMLLoader.isProduction()

    companion object {
        private val MODS: MutableMap<String, Mod> = ConcurrentHashMap()
    }
}
