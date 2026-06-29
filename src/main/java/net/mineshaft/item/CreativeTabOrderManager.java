package net.mineshaft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.mineshaft.MineshaftLogger;

import java.util.*;

import static net.minecraft.init.Items.*;

public class CreativeTabOrderManager {

    // 0  Building Blocks
    // 1  Decorations
    // 2  Redstone
    // 3  Transportation
    // 4  Misc.
    // 5  Search
    // 6  Food
    // 7  Tools
    // 8  Combat
    // 9  Brewing
    // 10 Materials
    // 11 Inventory

    private static Map<Item, Float> ITEM_SORT_VALUES = new HashMap<>();
    private static boolean HAS_BEEN_INITIALISED = false;
    private static final Map<Integer, ArrayList<Item>> MOVE_AFTER_RULES = new HashMap<>();
    private static final Map<Integer, ArrayList<Item>> MOVE_BEFORE_RULES = new HashMap<>();
    private static final Map<Integer, Integer> INCREMENTAL_INDEX = new HashMap<>();

    private static List<Item> getEquipmentSetList(Item.ToolMaterial toolMaterial) {
        switch (toolMaterial) {
            case WOOD: return Arrays.asList(wooden_shovel,wooden_pickaxe,wooden_axe,wooden_hoe,wooden_sword);
            case STONE: return Arrays.asList(stone_shovel,stone_pickaxe,stone_axe,stone_hoe,stone_sword);
            case IRON: return Arrays.asList(iron_shovel,iron_pickaxe,iron_axe,iron_hoe,iron_sword);
            case EMERALD: return Arrays.asList(diamond_shovel,diamond_pickaxe,diamond_axe,diamond_hoe,diamond_sword);
            case GOLD: return Arrays.asList(golden_shovel,golden_pickaxe,golden_axe,golden_hoe,golden_sword);
            case STEEL: return Arrays.asList(steel_shovel,steel_pickaxe,steel_axe,steel_hoe,steel_sword);
            case AMETHYST:
            case SMITE:
                return List.of();
        }
        return Collections.emptyList();
    }

    public static void initCreativeTabOrder() {
        // Tools and weapons
        ArrayList<Item> itemList = new ArrayList<>(List.of());
        itemList.add(iron_hoe);
        itemList.add(iron_sword);
        itemList.addAll(getEquipmentSetList(Item.ToolMaterial.WOOD));
        itemList.addAll(getEquipmentSetList(Item.ToolMaterial.STONE));
        itemList.addAll(getEquipmentSetList(Item.ToolMaterial.GOLD));
        itemList.addAll(getEquipmentSetList(Item.ToolMaterial.EMERALD));
        itemList.addAll(getEquipmentSetList(Item.ToolMaterial.STEEL));
        itemList.add(bow);
        itemList.add(arrow);
        addAfter(iron_axe, itemList);
        addAfter(name_tag,ender_pouch);
        addAfter(fishing_rod,compass);
        // Misc
        addAfter(lava_bucket, milk_bucket);
        addAfter(snowball, wind_charge);
        addAfter(book, new ArrayList<>(Arrays.asList(writable_book,map)));
        // Materials
        addAfter(diamond, new ArrayList<>(Arrays.asList(emerald,ruby,quartz,prismarine_crystals,prismarine_shard)));
        addAfter(gold_ingot, new ArrayList<>(Arrays.asList(gold_nugget, steel_ingot,steel_nugget,nether_ash,gunpowder,glowstone_dust,blaze_rod,breeze_rod,nether_star)));
        addAfter(feather,new ArrayList<>(Arrays.asList(leather,rabbit_hide)));
        addAfter(coal,flint);
        addAfter(brick,netherbrick);
        addAfter(wheat_seeds,new ArrayList<>(Arrays.asList(pumpkin_seeds,melon_seeds)));
        addAfter(wheat,new ArrayList<>(Arrays.asList(reeds,sugar,egg,nether_wart)));
        addAfter(prismarine_crystals,dye);
        // Redstone
        addAfter(oak_door,new ArrayList<>(Arrays.asList(spruce_door,birch_door,jungle_door,acacia_door,dark_oak_door)));
        setItemSortValues();
    }

    public static void setItemSortValues() {
        for(int anchorId : MOVE_AFTER_RULES.keySet()) {
            for(Item item : MOVE_AFTER_RULES.get(anchorId)) {
//                incrementIndex(anchorId);
                ITEM_SORT_VALUES.put(item,anchorId + MOVE_AFTER_RULES.get(anchorId).indexOf(item)*0.001F);
            }
        }
    }

    public static void addAfter(Item anchorItem, ArrayList<Item> items) {
        MOVE_AFTER_RULES.put(Item.getIdFromItem(anchorItem), items);
    }

    public static void addAfter(Item anchorItem, Item item) {
        MOVE_AFTER_RULES.put(Item.getIdFromItem(anchorItem), new ArrayList<>(Collections.singleton(item)));
    }

    public static void addBefore(Item anchorItem, ArrayList<Item> items) {
        MOVE_BEFORE_RULES.put(Item.getIdFromItem(anchorItem), items);
    }

    public static void addBefore(Item anchorItem, Item item) {
        MOVE_BEFORE_RULES.put(Item.getIdFromItem(anchorItem), new ArrayList<>(Collections.singleton(item)));
    }

//    public static Item getAnchor(Item item) {
//        for(int anchor : MOVE_AFTER_RULES.keySet()) {
//            if(MOVE_AFTER_RULES.get(anchor).contains(item)) {
//                MineshaftLogger.logDebug("Found anchor: " + Item.getItemById(anchor).getUnlocalizedName());
//                return anchor;
//            }
//        }
//        MineshaftLogger.logError("Could not get item anchor! " + item.getUnlocalizedName());
//        return ender_pouch;
//    }

    public static int getIndex(int itemId) {
        if(!INCREMENTAL_INDEX.containsKey(itemId)) {
            return 0;
        }
        return INCREMENTAL_INDEX.get(itemId);
    }

    public static void incrementIndex(int item) {
        if(item<0) return;
        if(INCREMENTAL_INDEX.containsKey(item) || INCREMENTAL_INDEX.get(item)==null) {
            MineshaftLogger.logDebug("Adding incremental index for anchor: " + Item.getItemById(item).getUnlocalizedName());
            INCREMENTAL_INDEX.put(item,1);
        }
        INCREMENTAL_INDEX.put(item, INCREMENTAL_INDEX.get(item) + 1);
    }

    public static Comparator<ItemStack> getComparator() {
        if(!HAS_BEEN_INITIALISED) {
            HAS_BEEN_INITIALISED=true;
            initCreativeTabOrder();
        }
        return new Comparator<>() {
            @Override
            public int compare(ItemStack stack1, ItemStack stack2) {
                Item item1 = stack1.getItem();
                Item item2 = stack2.getItem();

                // Calculate "Virtual ID" for both items
                double sortValue1 = getSortValue(item1);
                double sortValue2 = getSortValue(item2);

                return Double.compare(sortValue1, sortValue2);
            }

            private double getSortValue(Item item) {
                if(ITEM_SORT_VALUES.containsKey(item) && ITEM_SORT_VALUES.get(item)!=null) {
                    return ITEM_SORT_VALUES.get(item);
                }

                // No rule: Use standard Item ID
                return Item.getIdFromItem(item);
            }
        };
    }
}
