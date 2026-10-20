package com.example.timeloop.recording;

import net.minecraft.entity.player.PlayerEntity;
import java.util.ArrayList;
import java.util.List;

public class LoopRecorder {
    public static final int TICKS_PER_MINUTE = 1200;
    private final List<PlayerStateFrame> currentRecording = new ArrayList<>(TICKS_PER_MINUTE);
    private final List<List<PlayerStateFrame>> recordedLoops = new ArrayList<>();
    private int currentTick = 0;

    public void recordTick(PlayerEntity player) {
        currentRecording.add(new PlayerStateFrame(
                player.getPos(), player.getYaw(), player.getPitch(), player.getVelocity(),
                player.isSneaking(), player.isSprinting(), player.handSwinging, player.getActiveHand()
        ));
        currentTick++;
        if (currentTick >= TICKS_PER_MINUTE) {
            recordedLoops.add(new ArrayList<>(currentRecording));
            currentRecording.clear();
            currentTick = 0;
        }
    }
    public List<List<PlayerStateFrame>> getRecordedLoops() { return recordedLoops; }
    public int getCurrentTick() { return currentTick; }
    public void clearAll() { currentRecording.clear(); recordedLoops.clear(); currentTick = 0; }
}