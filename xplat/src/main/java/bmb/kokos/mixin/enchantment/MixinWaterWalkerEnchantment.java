package bmb.kokos.mixin.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.WaterWalkerEnchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterWalkerEnchantment.class)
public abstract class MixinWaterWalkerEnchantment extends Enchantment {
    protected MixinWaterWalkerEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.ARMOR_FEET, slots);
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment);
    }
}
