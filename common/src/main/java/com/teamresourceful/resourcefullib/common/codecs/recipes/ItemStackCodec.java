package com.teamresourceful.resourcefullib.common.codecs.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import com.teamresourceful.resourcefullib.common.exceptions.UtilityClassException;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

public final class ItemStackCodec {

    private static final Codec<ItemStack> STRING_EITHER = BuiltInRegistries.ITEM.byNameCodec().xmap(ItemStack::new, ItemStack::getItem);

    private static final Codec<ItemStack> STACK_CODEC = ItemStack.CODEC;

    public static final Codec<ItemStack> CODEC = CodecExtras.eitherRight(Codec.either(STRING_EITHER, STACK_CODEC));

    /**
     * @deprecated Use {@link com.teamresourceful.resourcefullib.common.bytecodecs.ExtraByteCodecs#ITEM_STACK} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "21.0")
    public static final Codec<ItemStack> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecExtras.registryId(BuiltInRegistries.ITEM).fieldOf("id").forGetter(ItemStack::getItem),
            Codec.INT.fieldOf("count").orElse(1).forGetter(ItemStack::getCount),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ItemStack::getComponentsPatch)
    ).apply(instance, ItemStackCodec::createItemStack));

    private ItemStackCodec() throws UtilityClassException {
        throw new UtilityClassException();
    }

    private static ItemStack createItemStack(ItemLike item, int count, DataComponentPatch components) {
        return new ItemStack(item.asItem().builtInRegistryHolder(), count, components);
    }
}
