package com.teamresourceful.resourcefullib.common.codecs.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record RestrictedItemPredicate(
        Item item, Optional<NbtPredicate> nbt, MinMaxBounds.Ints durability, MinMaxBounds.Ints count
) {

    public static final Codec<RestrictedItemPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(RestrictedItemPredicate::item),
            NbtPredicate.CODEC.optionalFieldOf("nbt").forGetter(RestrictedItemPredicate::nbt),
            MinMaxBounds.Ints.CODEC.fieldOf("durability").orElse(MinMaxBounds.Ints.ANY).forGetter(RestrictedItemPredicate::durability),
            MinMaxBounds.Ints.CODEC.fieldOf("count").orElse(MinMaxBounds.Ints.ANY).forGetter(RestrictedItemPredicate::count)
    ).apply(instance, RestrictedItemPredicate::new));

    public Optional<CompoundTag> getTag() {
        return nbt().map(NbtPredicate::tag);
    }

    public boolean matches(ItemStack stack) {
        if (item == null) {
            return false;
        } else if (stack.is(this.item)) {
            return false;
        } else if (!this.durability.isAny() && !stack.isDamageableItem()) {
            return false;
        } else if (!this.durability.matches(stack.getMaxDamage() - stack.getDamageValue())) {
            return false;
        } else if (!this.count.matches(stack.getCount())) {
            return false;
        }
        return this.nbt.isEmpty() || this.nbt.get().matches(stack);
    }
}
