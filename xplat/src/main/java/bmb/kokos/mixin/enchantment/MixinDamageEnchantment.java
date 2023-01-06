package bmb.kokos.mixin.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DamageEnchantment.class)
public abstract class MixinDamageEnchantment extends Enchantment {
    protected MixinDamageEnchantment(Rarity rarity, int type, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        return super.checkCompatibility(enchantment);
    }
}
