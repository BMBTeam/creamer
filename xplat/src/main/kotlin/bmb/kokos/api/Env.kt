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

import net.fabricmc.api.EnvType

enum class Env {
    CLIENT, SERVER;

    fun toXplat(): EnvType = if (this == CLIENT) EnvType.CLIENT else EnvType.SERVER

    companion object {
        /**
         * Converts xplat-specific environment enum to
         * xplat-agnostic environment enum.
         */
        @JvmStatic
        fun fromXplat(type: Any): Env? =
            if (type::class.java.isEnum && type.toString().equals("CLIENT", true)) CLIENT
            else if (type::class.java.isEnum && type.toString().equals("SERVER", true)) SERVER
            else null
    }
}
