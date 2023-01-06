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

package bmb.kokos.mixin.player;

import bmb.kokos.api.KokosAPI;
import bmb.kokos.integration.zoom.Spyglass;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class MixinInventory {
    @Inject(at = @At("HEAD"), method = "swapPaint", cancellable = true)
    private void kokosSwapPaint(double scrollAmount, CallbackInfo ci) {
        if (KokosAPI.minecraft.player == null) return;
        if (KokosAPI.minecraft.player.isScoping() && KokosAPI.minecraft.options.getCameraType().isFirstPerson()) {
            // zooms in/out
            if (scrollAmount > 0d && Spyglass.zoom > 0.001f) Spyglass.zoom *= 9f / 10f;
            if (scrollAmount < 0d && Spyglass.zoom < 1f) Spyglass.zoom *= 10f / 9f;
            // presents zooming from going OOB
            Spyglass.zoom = Mth.clamp(Spyglass.zoom, 0.001f, 1f);
            ci.cancel();
        }
    }
}
