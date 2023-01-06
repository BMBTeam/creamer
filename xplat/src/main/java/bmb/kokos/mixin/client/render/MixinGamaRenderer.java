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

package bmb.kokos.mixin.client.render;

import bmb.kokos.api.KokosAPI;
import bmb.kokos.integration.zoom.Spyglass;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGamaRenderer {
    /**
     * Zoom in screen for spyglass.
     */
    @Inject(at = @At("HEAD"), method = "tickFov", cancellable = true)
    private void kokosTickFov(CallbackInfo ci) {
        var player = KokosAPI.minecraft.player;
        var options = KokosAPI.minecraft.options;
        if (player != null) if (player.isScoping() && options.getCameraType().isFirstPerson()) {
            setOldFov(getFov());
            setFov(getFov() + (Spyglass.zoom - getFov()) * 0.5f);
            ci.cancel();
        }
    }

    @Accessor
    public abstract float getFov();

    @Accessor
    public abstract void setFov(float fov);

    @Accessor
    public abstract void setOldFov(float fov);
}
