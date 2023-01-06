package bmb.kokos.mixin.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FrostWalkerEnchantment.class)
public abstract class MixinFrostWalkerEnchantment extends Enchantment {
    protected MixinFrostWalkerEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.ARMOR_FEET, slots);
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment);
    }
}
