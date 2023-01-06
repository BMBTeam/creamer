package bmb.kokos.mixin.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.UntouchingEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UntouchingEnchantment.class)
public abstract class MixinUntouchingEnchantment extends Enchantment {
    protected MixinUntouchingEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.DIGGER, slots);
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment);
    }
}
