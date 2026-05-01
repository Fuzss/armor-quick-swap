package fuzs.armorquickswap.fabric.client;

import fuzs.armorquickswap.common.ArmorQuickSwap;
import fuzs.armorquickswap.common.client.ArmorQuickSwapClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class ArmorQuickSwapFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(ArmorQuickSwap.MOD_ID, ArmorQuickSwapClient::new);
    }
}
