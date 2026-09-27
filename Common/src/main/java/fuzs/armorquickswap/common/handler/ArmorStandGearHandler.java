package fuzs.armorquickswap.common.handler;

import fuzs.armorquickswap.common.ArmorQuickSwap;
import fuzs.puzzleslib.common.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.common.api.network.v4.NetworkingHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArmorStandGearHandler {

    public static EventResultHolder<InteractionResult> onUseEntity(Player player, Level level, InteractionHand hand, Entity entity, Vec3 hitVector) {
        if (entity instanceof ArmorStand armorStand && player.isSecondaryUseActive() && !armorStand.isMarker()
                && !player.isSpectator()) {
            if (hasItemsToSwap(armorStand, armorStand) || hasItemsToSwap(armorStand, player)) {
                if (!level.isClientSide()) {
                    for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
                        swapItem(armorStand, player, slot);
                    }

                    SwingAnimation animation = player.getItemInHand(hand).getInteractAnimation();
                    // Manually trigger hand swing on server-side, so it works for vanilla clients when connected to a server with the mod.
                    boolean sendToSwingingEntity = !NetworkingHelper.isModPresentClientside((ServerPlayer) player,
                            ArmorQuickSwap.MOD_ID);
                    player.swing(hand, animation, sendToSwingingEntity);
                }

                return EventResultHolder.interrupt(InteractionResult.SUCCESS);
            }
        }

        return EventResultHolder.pass();
    }

    private static boolean hasItemsToSwap(ArmorStand armorStand, LivingEntity entity) {
        for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
            if (!isSlotDisabled(armorStand, slot) && !entity.getItemBySlot(slot).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private static boolean swapItem(ArmorStand armorStand, Player player, EquipmentSlot slot) {
        ItemStack playerEquipment = player.getItemBySlot(slot);
        ItemStack armorStandEquipment = armorStand.getItemBySlot(slot);
        // We respect disabled slots on the server.
        // This is not possible to do with the client-side implementation;
        // since the field is not synced to the client, the interaction should just fail then.
        if (isSlotDisabled(armorStand, slot)) {
            return false;
        } else if (!playerEquipment.isEmpty() && playerEquipment.getCount() > 1) {
            if (!armorStandEquipment.isEmpty()) {
                return false;
            } else {
                armorStand.setItemSlot(slot, playerEquipment.split(1));
                return true;
            }
        } else {
            armorStand.setItemSlot(slot, playerEquipment);
            player.setItemSlot(slot, armorStandEquipment);
            return true;
        }
    }

    private static boolean isSlotDisabled(ArmorStand armorStand, EquipmentSlot slot) {
        return (armorStand.disabledSlots & 1 << slot.getFilterBit(0)) != 0;
    }
}
