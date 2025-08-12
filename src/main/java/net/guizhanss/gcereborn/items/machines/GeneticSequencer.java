package net.guizhanss.gcereborn.items.machines;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.inventory.InvUtils;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.gcereborn.items.GCEItems;
import net.guizhanss.gcereborn.utils.ChickenUtils;

public class GeneticSequencer extends AbstractMachine {

    public GeneticSequencer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public ItemStack getProgressBar() {
        return GCEItems.POCKET_CHICKEN.clone();
    }

    @Override
    @Nullable
    protected MachineRecipe findNextRecipe(@Nonnull BlockMenu menu) {
        var config = GeneticChickengineering.getConfigService();
        for (int slot : getInputSlots()) {
            ItemStack item = menu.getItemInSlot(slot);
            if (!ChickenUtils.isPocketChicken(item) || ChickenUtils.isLearned(item)) {
                continue;
            }
            ItemStack chicken = item.clone();
            // Just in case these got stacked somehow
            chicken.setAmount(1);

            ItemStack learnedChicken = ChickenUtils.learnDNA(chicken);
            if (config.isPainEnabled()) {
                if (!ChickenUtils.survivesPain(learnedChicken) && !config.isPainDeathEnabled()) {
                    // stop processing when pain kill is disabled
                    continue;
                }
                ChickenUtils.possiblyHarm(learnedChicken);
            }
            MachineRecipe recipe = new MachineRecipe(
                config.isTest() ? 1 : 30,
                new ItemStack[] {chicken},
                new ItemStack[] {learnedChicken}
            );
            if (!InvUtils.fitAll(menu.toInventory(), recipe.getOutput(), getOutputSlots())) {
                continue;
            }
            if (config.isPainEnabled() && ChickenUtils.getHealth(learnedChicken) <= 0d) {
                ItemUtils.consumeItem(chicken, false);
                menu.getBlock().getWorld().playSound(menu.getLocation(), Sound.ENTITY_CHICKEN_DEATH, 1f, 1f);
                continue;
            }
            menu.consumeItem(slot, 1);

            return recipe;
        }

        return null;
    }

}
