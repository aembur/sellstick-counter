package com.aembr.sellstickcounter;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.input.Keyboard;

import java.util.regex.Pattern;

@Mod(modid = SellstickCounter.MOD_ID, name = SellstickCounter.MOD_NAME, version = SellstickCounter.VERSION)
public class SellstickCounter {
    public static final String MOD_ID = "sellstick-counter";
    public static final String MOD_NAME = "SellStick Counter";
    public static final String VERSION = "1.12.2-0.2.0";

    public static int countKey = Keyboard.KEY_C;
    public static int countRunningTotalKey = Keyboard.KEY_V;
    public static Pattern searchPattern = Pattern.compile("§.(\\d+) §fremaining uses");
    public static int runningTotal = 0;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
