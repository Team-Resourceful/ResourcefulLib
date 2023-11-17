package com.teamresourceful.resourcefullib.common.codecs.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.Optional;

public record RestrictedEntityPredicate(
        EntityType<?> entityType,
        Optional<LocationPredicate> location,
        Optional<MobEffectsPredicate> effects,
        Optional<NbtPredicate> nbt,
        Optional<EntityFlagsPredicate> flags,
        Optional<EntityPredicate> targetedEntity
) {

    public static final Codec<RestrictedEntityPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("type").forGetter(RestrictedEntityPredicate::entityType),
        ExtraCodecs.strictOptionalField(LocationPredicate.CODEC, "location").forGetter(RestrictedEntityPredicate::location),
        ExtraCodecs.strictOptionalField(MobEffectsPredicate.CODEC, "effects").forGetter(RestrictedEntityPredicate::effects),
        ExtraCodecs.strictOptionalField(NbtPredicate.CODEC, "nbt").forGetter(RestrictedEntityPredicate::nbt),
        ExtraCodecs.strictOptionalField(EntityFlagsPredicate.CODEC, "flags").forGetter(RestrictedEntityPredicate::flags),
        ExtraCodecs.strictOptionalField(EntityPredicate.CODEC, "target").forGetter(RestrictedEntityPredicate::targetedEntity)
    ).apply(instance, RestrictedEntityPredicate::new));

    public Optional<CompoundTag> getTag() {
        return nbt().map(NbtPredicate::tag);
    }

    public boolean matches(ServerLevel level, Entity entity) {
        if (entityType == null) {
            return false;
        } else if (this.entityType != entity.getType()) {
            return false;
        } else if (this.location.isPresent() && !this.location.get().matches(level, entity.getX(), entity.getY(), entity.getZ())) {
            return false;
        } else if (this.effects.isPresent() && !this.effects.get().matches(entity)) {
            return false;
        } else if (this.nbt.isPresent() && !this.nbt.get().matches(entity)) {
            return false;
        } else if (this.flags.isPresent() && !this.flags.get().matches(entity)) {
            return false;
        } else {
            return this.targetedEntity.isEmpty() || this.targetedEntity.get().matches(level, null, entity instanceof Mob mob ? mob.getTarget() : null);
        }
    }
}
