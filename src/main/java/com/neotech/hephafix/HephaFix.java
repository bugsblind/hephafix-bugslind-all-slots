package com.neotech.hephafix;

import com.mojang.logging.LogUtils;
import com.neotech.hephafix.capability.HephaestusCenterSlotHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.slf4j.Logger;

@Mod("hephafix")
public class HephaFix {
   public static final String MOD_ID = "hephafix";
   public static final Logger LOGGER = LogUtils.getLogger();

   public HephaFix(IEventBus modEventBus) {
      modEventBus.addListener(this::onRegisterCapabilities);
   }

   private void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
      BlockCapability<IItemHandler, Direction> cap = ItemHandler.BLOCK;
      ResourceLocation forgeBeId = ResourceLocation.fromNamespaceAndPath("forbidden_arcanus", "hephaestus_forge");
      BlockEntityType<?> beType = (BlockEntityType)BuiltInRegistries.BLOCK_ENTITY_TYPE.get(forgeBeId);
      if (beType == null) {
         LOGGER.warn("[HephaFix] Could not find forbidden_arcanus:hephaestus_forge. Is Forbidden & Arcanus installed?");
      } else {
         LOGGER.info("[HephaFix] Registering full access item handler.");
         event.registerBlockEntity(cap, beType, (blockEntity, side) -> {
            return new HephaestusCenterSlotHandler(blockEntity, 0);
         });
      }
   }
}
