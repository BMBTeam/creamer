package bmb.kokos.mixin.fluid;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LavaFluid.class)
public abstract class MixinLavaFluid extends FlowingFluid {
    @Inject(at = @At("RETURN"), method = "canConvertToSource", cancellable = true)
    private void kokosCanConvertToSource(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
