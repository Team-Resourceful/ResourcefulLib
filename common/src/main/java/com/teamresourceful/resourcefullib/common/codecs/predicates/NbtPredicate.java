package com.teamresourceful.resourcefullib.common.codecs.predicates;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

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
        return this.matches(tag, true);
    }

    public boolean matches(@Nullable Tag tag, boolean strict) {
        if (this == ANY) return true;
        boolean empty = isEmpty(this.tag);
        if (isEmpty(tag)) return empty;
        return strict ? NbtUtils.compareNbt(this.tag, tag, true) : compareNbt(this.tag, tag);
    }

    public static boolean compareNbt(@Nullable Tag tag, @Nullable Tag tag2) {
        if (tag == tag2) {
            return true;
        } else if (tag == null || tag2 == null) {
            return true;
        } else if (tag instanceof NumericTag num1 && tag2 instanceof NumericTag num2) {
            BigDecimal bigDecimal = new BigDecimal(num1.getAsNumber().toString());
            BigDecimal bigDecimal2 = new BigDecimal(num2.getAsNumber().toString());
            return bigDecimal.compareTo(bigDecimal2) == 0;
        } else if (!tag.getClass().equals(tag2.getClass())) {
            return false;
        } else if (tag instanceof CompoundTag compound) {
            CompoundTag compound2 = (CompoundTag) tag2;
            for (String key : compound.getAllKeys()) {
                if (!compareNbt(compound.get(key), compound2.get(key))) {
                    return false;
                }
            }
            return true;
        } else if (tag instanceof ListTag list) {
            ListTag list2 = (ListTag) tag2;
            if (list.isEmpty()) {
                return list2.isEmpty();
            } else {
                for (Tag entry : list) {
                    boolean oneMatches = false;

                    for (Tag value : list2) {
                        if (compareNbt(entry, value)) {
                            oneMatches = true;
                            break;
                        }
                    }

                    if (!oneMatches) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return tag.equals(tag2);
        }
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
