package com.example.timeloop;

import com.example.timeloop.entity.CloneEntity;
import com.example.timeloop.recording.LoopRecorder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class TimeLoopMod implements ModInitializer {
    public static final String MOD_ID = "timeloop";
    public static final LoopRecorder RECORDER = new LoopRecorder();
    private static KeyBinding resetKey;
    private static Vec3d customSpawnPoint = null;

    public static final EntityType<CloneEntity> CLONE_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "clone"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, CloneEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build()
    );

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(CLONE_ENTITY_TYPE, CloneEntity.createCloneAttributes());

        resetKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.timeloop.reset", InputUtil.Type.KEY_SYM, GLFW.GLFW_KEY_K, "category.timeloop.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;

            if (resetKey.wasPressed()) {
                customSpawnPoint = client.player.getPos();
                client.world.getEntitiesByClass(CloneEntity.class, client.player.getBoundingBox().expand(500), e -> true)
                        .forEach(e -> e.discard());
                RECORDER.clearAll();
                client.player.sendMessage(Text.literal("§c[TimeLoop] Clones réinitialisés ! Reprise à vos coordonnées actuelles."), true);
            }

            RECORDER.recordTick(client.player);

            if (RECORDER.getCurrentTick() == 0 && !RECORDER.getRecordedLoops().isEmpty()) {
                Vec3d spawnPos = (customSpawnPoint != null) ? customSpawnPoint : Vec3d.ofCenter(client.world.getSpawnPos());
                CloneEntity newClone = CLONE_ENTITY_TYPE.create(client.world);
                if (newClone != null) {
                    newClone.refreshPositionAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, 0, 0);
                    newClone.setSequence(RECORDER.getRecordedLoops().get(RECORDER.getRecordedLoops().size() - 1));
                    client.world.addEntity(newClone.getId(), newClone);
                }
                client.player.sendMessage(Text.literal("§a[TimeLoop] Nouvelle minute ! Un double apparaît."), true);
            }
        });
    }
}