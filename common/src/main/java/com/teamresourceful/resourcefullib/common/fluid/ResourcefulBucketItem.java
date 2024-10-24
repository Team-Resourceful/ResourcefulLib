package com.teamresourceful.resourcefullib.common.fluid;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourcefulBucketItem extends BucketItem {

    private final FluidData data;

    public ResourcefulBucketItem(FluidData data, Properties properties) {
        super(data.still().get(), properties);
        this.data = data;
        this.data.setBucket(() -> this);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (this.data.properties().canPlace()) {
            return super.use(level, player, hand);
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
        SoundEvent event = this.data.properties().sounds().getOrDefault(FluidSounds.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
        level.playSound(player, pos, event, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
