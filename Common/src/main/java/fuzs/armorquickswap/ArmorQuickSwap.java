package fuzs.armorquickswap;

import fuzs.armorquickswap.handler.ArmorStandGearHandler;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.common.api.event.v1.entity.player.PlayerInteractEvents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArmorQuickSwap implements ModConstructor {
    public static final String MOD_ID = "armorquickswap";
    public static final String MOD_NAME = "Armor Quick Swap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        // run before other mods like Quark that might interfere here
        PlayerInteractEvents.USE_ENTITY.register(EventPhase.BEFORE, ArmorStandGearHandler::onUseEntity);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
