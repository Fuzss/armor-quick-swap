package fuzs.armorquickswap.fabric;

import fuzs.armorquickswap.common.ArmorQuickSwap;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class ArmorQuickSwapFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(ArmorQuickSwap.MOD_ID, ArmorQuickSwap::new);
    }
}
