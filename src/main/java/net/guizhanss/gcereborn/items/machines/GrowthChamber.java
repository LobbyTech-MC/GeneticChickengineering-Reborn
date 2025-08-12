package net.guizhanss.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.gcereborn.core.genetics.DNA;
import net.guizhanss.gcereborn.items.chicken.PocketChicken;
import net.guizhanss.gcereborn.utils.ChickenUtils;
import net.guizhanss.gcereborn.utils.Keys;

public class GrowthChamber extends AbstractMachine {

    public GrowthChamber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return new ItemStack(Material.WHEAT_SEEDS);
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        var config = GeneticChickengineering.getConfigService();
        ItemStack chicken = null;
        ItemStack seed = null;
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);

            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (ChickenUtils.isPocketChicken(item) && !ChickenUtils.isAdult(item)) {
                chicken = item;
            } else if (ChickenUtils.isFood(item) && item.getAmount() == item.getMaxStackSize()) {
                seed = item;
            }
        }

        if (chicken == null || seed == null) {
            return null;
        }

        ItemStack output = chicken.clone();
        output.setAmount(1);
        ItemMeta outputMeta = output.getItemMeta();
        JsonObject adapter = PersistentDataAPI.get(outputMeta, Keys.POCKET_CHICKEN_ADAPTER, PocketChicken.ADAPTER);
        var dnaState = PersistentDataAPI.getIntArray(outputMeta, Keys.POCKET_CHICKEN_DNA);
        adapter.addProperty("baby", false);
        adapter.addProperty("_age", 6000);
        adapter.addProperty("_breedable", false);
        ChickenUtils.setPocketChicken(output, adapter, new DNA(dnaState));

        MachineRecipe recipe = new MachineRecipe(
            config.isTest() ? 1 : config.getGrowthChamberTime(),
            new ItemStack[] {seed.clone(), chicken.clone()},
            new ItemStack[] {output}
        );
        if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
            return null;
        }
        ItemUtils.consumeItem(chicken, false);
        ItemUtils.consumeItem(seed, seed.getMaxStackSize(), false);
        return recipe;
    }
}
