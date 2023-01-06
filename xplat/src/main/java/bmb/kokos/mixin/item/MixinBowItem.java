package bmb.kokos.mixin.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public abstract class MixinBowItem {
    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void kokosUse(
        Level level, Player player, InteractionHand hand,
        CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
    ) {
        var stack = player.getItemInHand(hand);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0) {
            player.startUsingItem(hand);
            cir.setReturnValue(InteractionResultHolder.success(stack));
        }
    }
}
