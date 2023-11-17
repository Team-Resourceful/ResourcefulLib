package com.teamresourceful.resourcefullib.common.codecs.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.tags.HolderSetCodec;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Map;

public record EnchantmentPredicate(HolderSet<Enchantment> enchantments, MinMaxBounds.Ints level) {

    public static final EnchantmentPredicate ANY = new EnchantmentPredicate(HolderSet.direct(), MinMaxBounds.Ints.ANY);
    public static final Codec<EnchantmentPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            HolderSetCodec.of(BuiltInRegistries.ENCHANTMENT).fieldOf("enchantments").orElse(HolderSet.direct()).forGetter(EnchantmentPredicate::enchantments),
            MinMaxBounds.Ints.CODEC.fieldOf("level").orElse(MinMaxBounds.Ints.ANY).forGetter(EnchantmentPredicate::level)
    ).apply(instance, EnchantmentPredicate::new));

    public boolean matches(Enchantment enchantment) {
        Holder<Enchantment> holder = BuiltInRegistries.ENCHANTMENT.wrapAsHolder(enchantment);
        return this == ANY || (enchantments != null && enchantments.contains(holder));
    }

    public boolean matches(Enchantment enchantment, int level) {
        if (enchantments.size() == 0) {
            return this.level.matches(level);
        }
        return this.matches(enchantment) && this.level.matches(level);
    }

    public boolean matches(Map<Enchantment, Integer> enchantments) {
        if (this == ANY) return true;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            if (this.matches(entry.getKey(), entry.getValue())) return true;
        }
        return false;
    }

}
