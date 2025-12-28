package com.neotech.hephafix.capability;

import com.neotech.hephafix.HephaFix;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class HephaestusCenterSlotHandler implements IItemHandler {
   private static final Map<Class<?>, Field> HANDLER_FIELD_CACHE = new ConcurrentHashMap<>();
   private static final Map<BlockEntity, ItemStackHandler> HANDLER_CACHE = Collections.synchronizedMap(new WeakHashMap<>());
   private final BlockEntity be;

   public HephaestusCenterSlotHandler(BlockEntity forgeBe, int ignored) {
      this.be = forgeBe;
   }

   private ItemStackHandler getItemStackHandler() {
      ItemStackHandler cached = HANDLER_CACHE.get(this.be);
      if (cached != null) return cached;

      Class<?> clazz = this.be.getClass();
      Field field = HANDLER_FIELD_CACHE.get(clazz);
      try {
         if (field == null) {
            for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
               for (Field f : c.getDeclaredFields()) {
                  if (ItemStackHandler.class.isAssignableFrom(f.getType())) {
                     f.setAccessible(true);
                     field = f;
                     HANDLER_FIELD_CACHE.put(clazz, f);
                     break;
                  }
               }
               if (field != null) break;
            }
         }
         if (field != null) {
            Object obj = field.get(this.be);
            if (obj instanceof ItemStackHandler handler) {
               HANDLER_CACHE.put(this.be, handler);
               return handler;
            }
         }
      } catch (Throwable ignored) {}
      return null;
   }

   @Override public int getSlots() {
      ItemStackHandler h = getItemStackHandler();
      return h != null ? h.getSlots() : 0;
   }

   @Override public ItemStack getStackInSlot(int slot) {
      ItemStackHandler h = getItemStackHandler();
      return h != null ? h.getStackInSlot(slot) : ItemStack.EMPTY;
   }

   @Override public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      ItemStackHandler h = getItemStackHandler();
      return h != null ? h.insertItem(slot, stack, simulate) : stack;
   }

   @Override public ItemStack extractItem(int slot, int amount, boolean simulate) {
      ItemStackHandler h = getItemStackHandler();
      return h != null ? h.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
   }

   @Override public int getSlotLimit(int slot) {
      ItemStackHandler h = getItemStackHandler();
      return h != null ? h.getSlotLimit(slot) : 0;
   }

   @Override public boolean isItemValid(int slot, ItemStack stack) {
      ItemStackHandler h = getItemStackHandler();
      return h != null && h.isItemValid(slot, stack);
   }
}
