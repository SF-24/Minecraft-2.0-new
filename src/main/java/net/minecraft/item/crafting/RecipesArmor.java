package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesArmor
{
    private String[][] recipePatterns = new String[][] {{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private Item[][] recipeItems = new Item[][] {{Items.leather, Items.iron_ingot, Items.diamond, Items.gold_ingot, Items.steel_ingot, Items.amethyst}, {Items.leather_helmet, Items.iron_helmet, Items.diamond_helmet, Items.golden_helmet, Items.steel_helmet, Items.amethyst_helmet}, {Items.leather_chestplate, Items.iron_chestplate, Items.diamond_chestplate, Items.golden_chestplate, Items.steel_chestplate, Items.amethyst_chestplate}, {Items.leather_leggings, Items.iron_leggings, Items.diamond_leggings, Items.golden_leggings, Items.steel_leggings, Items.amethyst_leggings}, {Items.leather_boots, Items.iron_boots, Items.diamond_boots, Items.golden_boots, Items.steel_boots, Items.amethyst_boots}};

    /**
     * Adds the armor recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager craftManager)
    {
        for (int i = 0; i < this.recipeItems[0].length; ++i)
        {
            Item item = this.recipeItems[0][i];

            for (int j = 0; j < this.recipeItems.length - 1; ++j)
            {
                Item item1 = this.recipeItems[j + 1][i];
                craftManager.addRecipe(new ItemStack(item1), this.recipePatterns[j], 'X', item);
            }
        }
    }
}
