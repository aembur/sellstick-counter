package com.aembr.sellstickcounter;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.regex.Matcher;


public class EventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void keyboardEvent(GuiScreenEvent.KeyboardInputEvent.Post event) {
        GuiScreen screen = event.getGui();
        if (isValidGui(screen)) {
            if (!(Keyboard.getEventKey() == SellstickCounter.countKey || Keyboard.getEventKey() == SellstickCounter.countRunningTotalKey)) {
                return;
            }

            int uses;
            GuiContainer c = (GuiContainer) screen;

            if (Keyboard.getEventKey() == SellstickCounter.countKey && Keyboard.getEventKeyState()) {
                uses = doCount(c);
                SellstickCounter.runningTotal = 0;
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("%s uses", uses)));
            }

            else if (Keyboard.getEventKey() == SellstickCounter.countRunningTotalKey && Keyboard.getEventKeyState()) {
                uses = doCount(c);
                SellstickCounter.runningTotal += uses;
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(String.format("%s uses (+%s)", SellstickCounter.runningTotal, uses)));
            }
        }
    }

    public int doCount(GuiContainer container) {
        int uses = 0;

        int slots = getContainerSlotSize(container);
        NonNullList<ItemStack> itemStackList = container.inventorySlots.getInventory();

        for (int i = 0; i < slots; i++) {
            ItemStack itemStack = itemStackList.get(i);
            if (itemStack.getItem().getUnlocalizedNameInefficiently(itemStack).equals("item.stick")) {
                if (!(itemStack.hasTagCompound())) {
                    continue;
                }

                NBTTagList lore = itemStack.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
                for (int j = 0; j < lore.tagCount(); j++) {
                    String line = lore.getStringTagAt(j);
                    Matcher matcher = SellstickCounter.searchPattern.matcher(line);
                    if (matcher.matches()) {
                        uses += Integer.parseInt(matcher.group(1));
                    }
                }
            }
        }
        return uses;
    }

    private Boolean isValidGui(GuiScreen screen) {
        return screen instanceof GuiChest || screen instanceof GuiInventory;
    }

    private int getContainerSlotSize(GuiContainer screen) {
        // if chest is open, only count uses inside it
        if (screen instanceof GuiInventory) {
            return screen.inventorySlots.inventoryItemStacks.size();
        } else {
            return screen.inventorySlots.inventoryItemStacks.size() - 36;
        }
    }
}
