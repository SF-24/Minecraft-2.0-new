package net.mineshaft.structure;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

import java.util.List;

public class LootTableList {

    public static class LootOverworld {
        public static final List<WeightedRandomChestContent> IGLOO = Lists.newArrayList(
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 0),
                new WeightedRandomChestContent(Items.coal, 0, 1, 4, 5),
                new WeightedRandomChestContent(Items.apple, 0, 1, 3, 5),
                new WeightedRandomChestContent(Items.wheat_seeds, 0, 2, 3, 3),
                new WeightedRandomChestContent(Items.gold_nugget, 0, 1, 3, 3),
                new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.emerald, 0, 1, 1, 1)
        );

        public static final List<WeightedRandomChestContent> JUNGLE_PYRAMID = Lists.newArrayList(
                new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
                new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
                new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15),
                new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
                new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20),
                new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16),
                new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
                new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)
        );


        public static final List<WeightedRandomChestContent> DESERT_PYRAMID = Lists.newArrayList(
                new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3),
                new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10),
                new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15),
                new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
                new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20),
                new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16),
                new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
                new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)
        );
    }

    public static class LootNether {

    }

    public static class LootAether {
        public static final List<WeightedRandomChestContent> AETHER_DUNGEON = Lists.newArrayList(
//            new WeightedRandomChestContent(Items.glowing_bread, 0, 1, 1, 3),
//            new WeightedRandomChestContent(Items.ruby, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.glowstone_dust, 0, 1, 4, 10),
                new WeightedRandomChestContent(Items.slime_ball, 0, 1, 4, 2),
                new WeightedRandomChestContent(Items.apple, 0, 1, 2, 3),
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.record_magnetic_circuit, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.bread, 0, 1, 4, 5),
                new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 5),
                new WeightedRandomChestContent(Items.gold_nugget, 0, 3, 27, 5),
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 2));
    }

    public static class LootEnd {

    }
}
