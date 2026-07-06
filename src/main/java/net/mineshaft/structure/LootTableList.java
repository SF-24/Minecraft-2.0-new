package net.mineshaft.structure;

import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;

import java.util.List;

public class LootTableList {

    public static final List<WeightedRandomChestContent> LOOT_IGLOO = Lists.newArrayList(
            new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 0, true),
            new WeightedRandomChestContent(Items.coal, 0, 1, 4, 5),
            new WeightedRandomChestContent(Items.apple, 0, 1, 3, 5),
            new WeightedRandomChestContent(Items.wheat_seeds, 0, 2, 3, 3),
            new WeightedRandomChestContent(Items.gold_nugget, 0, 1, 3, 3),
            new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 1),
            new WeightedRandomChestContent(Items.emerald, 0, 1, 1, 1)
    );

}
