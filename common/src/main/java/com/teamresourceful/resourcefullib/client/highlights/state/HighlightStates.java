package com.teamresourceful.resourcefullib.client.highlights.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.client.highlights.HighlightHandler;
import com.teamresourceful.resourcefullib.client.highlights.base.Highlight;
import com.teamresourceful.resourcefullib.client.highlights.base.HighlightLine;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public record HighlightStates(Map<List<BlockState>, Highlight> states) {

    private static final Vector3f CENTER = new Vector3f(0.5f, 0, 0.5f);

    public static final Codec<BlockModelRotation> ROTATION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").orElse(0).forGetter(ignored -> 0),
            Codec.INT.fieldOf("y").orElse(0).forGetter(ignored -> 0)
    ).apply(instance, BlockModelRotation::by));

    public static final Codec<Highlight> TRANSLATED_BOX_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            HighlightHandler.HIGHLIGHT_CODEC.fieldOf("highlight").forGetter(i -> i),
            ExtraCodecs.VECTOR3F.fieldOf("translation").orElse(new Vector3f(0, 0, 0)).forGetter(ignored -> new Vector3f(0, 0, 0)),
            ROTATION_CODEC.fieldOf("rotation").orElse(BlockModelRotation.X0_Y0).forGetter(ignored -> BlockModelRotation.X0_Y0)
    ).apply(instance, HighlightStates::createBox));

    public static final Codec<Highlight> BOX_CODEC = CodecExtras.eitherRight(Codec.either(HighlightHandler.HIGHLIGHT_CODEC, TRANSLATED_BOX_CODEC));

    public static Codec<HighlightStates> codec(Block block) {
        return RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(StateVariant.stateCodec(block), BOX_CODEC).fieldOf("variants").forGetter(HighlightStates::states)
        ).apply(instance, HighlightStates::new));
    }

    private static Highlight createBox(Highlight box, Vector3f translation, BlockModelRotation rotation) {
        if (translation.equals(new Vector3f(0, 0, 0)) && rotation.equals(BlockModelRotation.X0_Y0)) return box;

        Highlight newBox = box.copy();
        for (HighlightLine line : newBox.lines()) {

            line.start().sub(CENTER);
            line.end().sub(CENTER);

            line.start().rotate(rotation.getRotation().getLeftRotation());
            line.end().rotate(rotation.getRotation().getLeftRotation());

            line.start().add(CENTER);
            line.end().add(CENTER);

            line.start().add(translation);
            line.end().add(translation);

            line.recalculateNormal();
        }

        return newBox;
    }

}