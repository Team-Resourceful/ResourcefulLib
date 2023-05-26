package com.teamresourceful.resourcefullib.common.codecs.predicates;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record NbtPredicate(CompoundTag tag) {

    public static final NbtPredicate ANY = new NbtPredicate(null);
    public static final Codec<NbtPredicate> CODEC = CompoundTag.CODEC.xmap(NbtPredicate::new, NbtPredicate::tag);

    public boolean matches(ItemStack pStack) {
        if (this == ANY || isEmpty(this.tag)) return true;
        return this.matches(pStack.getTag());
    }

    public boolean matches(Entity pEntity) {
        if (this == ANY || isEmpty(this.tag)) return true;
        return this.matches(getEntityTagToCompare(pEntity));
    }

    public boolean matches(@Nullable Tag tag) {
        if (this == ANY || isEmpty(this.tag)) return true;
        return isEmpty(tag) ? isEmpty(this.tag) : NbtUtils.compareNbt(this.tag, tag, true);
    }

    public static boolean isEmpty(Tag tag) {
        return tag == null || (tag instanceof CompoundTag compoundTag && compoundTag.isEmpty()) || (tag instanceof ListTag list && list.isEmpty());
    }
    private static CompoundTag getEntityTagToCompare(Entity entity) {
        CompoundTag compoundtag = entity.saveWithoutId(new CompoundTag());
        if (entity instanceof Player player) {
            ItemStack itemstack = player.getInventory().getSelected();
            if (!itemstack.isEmpty()) {
                compoundtag.put("SelectedItem", itemstack.save(new CompoundTag()));
            }
        }

        return compoundtag;
    }

}
