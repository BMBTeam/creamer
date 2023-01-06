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

package bmb.kokos.mixin.client;

import bmb.kokos.api.KokosAPI;
import bmb.kokos.integration.zoom.Spyglass;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MixinMouseHandler {
    /**
     * Lowers mouse sensitivity for Spyglass.
     */
    @Inject(at = @At("HEAD"), method = "turnPlayer", cancellable = true)
    private void kokosTurnPlayer(CallbackInfo ci) {
        var player = KokosAPI.minecraft.player;
        var options = KokosAPI.minecraft.options;
        if (player == null) return;
        if (player.isScoping() && options.getCameraType().isFirstPerson()) {
            var f = options.sensitivity().get() * 0.6000000238418579d + 0.20000000298023224d;
            var g = f * f * f;
            // Janky calculation that probably isn't right but works
            var zoom = -20.0 / 9 * Spyglass.zoom * Spyglass.zoom + 92.0 / 9 * Spyglass.zoom; // -20/9x²+92/9x
            g *= zoom;
            var o = getAccumulatedDX() * g;
            var p = getAccumulatedDY() * g;
            setAccumulatedDX(0d);
            setAccumulatedDY(0d);
            var q = 1;
            if (options.invertYMouse().get()) q = -1;
            KokosAPI.minecraft.getTutorial().onMouse(o, p);
            player.turn(o, p * q);
            ci.cancel();
        }
    }

    @Accessor
    public abstract double getAccumulatedDX();

    @Accessor
    public abstract void setAccumulatedDX(double dx);

    @Accessor
    public abstract double getAccumulatedDY();

    @Accessor
    public abstract void setAccumulatedDY(double dy);
}
