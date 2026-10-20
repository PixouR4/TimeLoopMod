package com.example.timeloop.recording;

import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class PlayerStateFrame {
    public final Vec3d position;
    public final float yaw, pitch;
    public final Vec3d velocity;
    public final boolean isSneaking, isSprinting, isAttacking;
    public final Hand activeHand;

    public PlayerStateFrame(Vec3d pos, float yaw, float pitch, Vec3d velocity, boolean s1, boolean s2, boolean att, Hand h) {
        this.position = pos; this.yaw = yaw; this.pitch = pitch; this.velocity = velocity;
        this.isSneaking = s1; this.isSprinting = s2; this.isAttacking = att; this.activeHand = h;
    }
}