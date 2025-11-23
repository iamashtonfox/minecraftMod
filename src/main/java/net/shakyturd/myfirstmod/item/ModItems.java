package net.shakyturd.myfirstmod.item;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.shakyturd.myfirstmod.ShakyTurdMod;
import net.shakyturd.myfirstmod.item.custom.WoodenStaffItem;
import net.shakyturd.myfirstmod.item.custom.ZiplineItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ShakyTurdMod.MOD_ID);


//    public static final RegistryObject<Item> ZIPLINE = ITEMS.register("zipline",
//        () -> new ZiplineItem(new Item.Properties()));

    public static final RegistryObject<Item> MAGICCRYSTAL = ITEMS.register("magic_crystal",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOODENSTAFFDORMANT = ITEMS.register("dormant_wooden_staff",
            () -> new Item(new Item.Properties().durability(8)));
    public static final RegistryObject<Item> WOODENSTAFF = ITEMS.register("wooden_staff",
            () -> new WoodenStaffItem(new Item.Properties().durability(128)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
