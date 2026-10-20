package com.example.timeloop.entity;

import com.example.timeloop.recording.PlayerStateFrame;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import java.util.List;

public class CloneEntity extends MobEntity {
    private List<PlayerStateFrame> sequence;
    private int playhead = 0;

    public CloneEntity(EntityType<? extends MobEntity> type, World world) { super(type, world); }

    public static DefaultAttributeContainer.Builder createCloneAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3);
    }

    public void setSequence(List<PlayerStateFrame> sequence) { this.sequence = sequence; this.playhead = 0; }

    @Override
    public void tick() {
        super.tick();
        if (sequence == null || sequence.isEmpty()) return;
        if (playhead < sequence.size()) {
            PlayerStateFrame frame = sequence.get(playhead);
            this.refreshPositionAndAngles(frame.position.x, frame.position.y, frame.position.z, frame.yaw, frame.pitch);
            this.setVelocity(frame.velocity);
            this.setSneaking(frame.isSneaking);
            this.setSprinting(frame.isSprinting);
            if (frame.isAttacking) this.swingHand(frame.activeHand != null ? frame.activeHand : net.minecraft.util.Hand.MAIN_HAND);
            playhead++;
        } else {
            playhead = 0;
        }
    }

    @Override public boolean isCollidable() { return true; }
    @Override public boolean isPushable() { return true; }
}