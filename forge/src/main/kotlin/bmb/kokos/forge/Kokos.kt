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

@file:Suppress("UNUSED_PARAMETER")

package bmb.kokos.forge

import bmb.kokos.api.KokosAPI
import bmb.kokos.forge.client.KokosClient
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.DistExecutor.SafeRunnable
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(KokosAPI.NAMESPACE)
object Kokos {
    init {
        ForgeMod.enableMilkFluid()
        MOD_BUS.addListener(this::init)
        EventsImplementedInJava().register(MOD_BUS, FORGE_BUS)
        DistExecutor.safeRunWhenOn(Dist.CLIENT) { SafeRunnable(KokosClient::init) }
    }

    private fun init(event: FMLCommonSetupEvent) {
    }
}
