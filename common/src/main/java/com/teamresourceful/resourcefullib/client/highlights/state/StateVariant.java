package com.teamresourceful.resourcefullib.client.highlights.state;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public final class StateVariant {

    private static final Splitter COMMA_SPLITTER = Splitter.on(',');
    private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);

    public static Codec<List<BlockState>> stateCodec(Block block) {
        StateDefinition<Block, BlockState> stateDefinition = block.getStateDefinition();
        return Codec.STRING.flatXmap(string -> getStates(stateDefinition, string), state -> DataResult.error("State variants can not be encoded back to a string."));
    }

    private static DataResult<List<BlockState>> getStates(StateDefinition<Block, BlockState> definition, String input) {
        var states = definition.getPossibleStates().stream().filter(predicate(definition, input)).toList();
        return states.isEmpty() ? DataResult.error("No states found for " + input) : DataResult.success(states);
    }

    private static Predicate<BlockState> predicate(StateDefinition<Block, BlockState> state, String stateString) {
        Map<Property<?>, Comparable<?>> map = Maps.newHashMap();

        for(String s : COMMA_SPLITTER.split(stateString)) {
            Iterator<String> iterator = EQUAL_SPLITTER.split(s).iterator();
            if (iterator.hasNext()) {
                String key = iterator.next();
                Property<?> property = state.getProperty(key);
                if (property != null && iterator.hasNext()) {
                    String value = iterator.next();
                    Comparable<?> comparable = property.getValue(value).orElse(null);
                    if (comparable == null) {
                        throw new RuntimeException("Unknown value: '" + value + "' for blockstate property: '" + key + "' " + property.getPossibleValues());
                    }

                    map.put(property, comparable);
                } else if (!key.isEmpty()) {
                    throw new RuntimeException("Unknown blockstate property: '" + key + "'");
                }
            }
        }

        return (stateIn) -> {
            if (stateIn != null && stateIn.is(state.getOwner())) {
                for(Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {
                    if (!Objects.equals(stateIn.getValue(entry.getKey()), entry.getValue())) {
                        return false;
                    }
                }

                return true;
            }
            return false;
        };
    }

}