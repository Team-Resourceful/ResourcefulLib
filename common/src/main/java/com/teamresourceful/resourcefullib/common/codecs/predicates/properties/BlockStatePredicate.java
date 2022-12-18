package com.teamresourceful.resourcefullib.common.codecs.predicates.properties;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;

import java.util.Map;
import java.util.Optional;

public record BlockStatePredicate(Map<String, PropertyMatcher> properties) {

    public static final BlockStatePredicate ANY = new BlockStatePredicate(ImmutableMap.of());
    public static final Codec<BlockStatePredicate> CODEC = Codec.unboundedMap(Codec.STRING, PropertyMatcher.CODEC)
            .xmap(BlockStatePredicate::new, BlockStatePredicate::properties);

    public BlockState construct(Block block, RandomSource random) {
        StateDefinition<Block, BlockState> definition = block.getStateDefinition();
        BlockState state = block.defaultBlockState();
        for (Map.Entry<String, PropertyMatcher> entry : properties.entrySet()) {
            String name = entry.getKey();
            PropertyMatcher matcher = entry.getValue();
            Property<?> property = definition.getProperty(name);
            state = setProperty(state, property, matcher.value(random).orElse(null));
        }
        return state;
    }

    private static <T extends Comparable<T>> BlockState setProperty(BlockState state, Property<T> property, String string) {
        if (string == null) return state;
        Optional<T> optional = property.getValue(string);
        if (optional.isEmpty()) return state;
        return state.setValue(property, optional.get());
    }

    public boolean matches(BlockState blockState) {
        return this.matches(blockState.getBlock().getStateDefinition(), blockState);
    }

    public boolean matches(FluidState fluidState) {
        return this.matches(fluidState.getType().getStateDefinition(), fluidState);
    }

    public <S extends StateHolder<?, S>> boolean matches(StateDefinition<?, S> stateDefinition, S stateHolder) {
        for (var entry : properties.entrySet()) {
            if (!entry.getValue().match(entry.getKey(), stateDefinition, stateHolder)) {
                return false;
            }
        }
        return true;
    }


}
