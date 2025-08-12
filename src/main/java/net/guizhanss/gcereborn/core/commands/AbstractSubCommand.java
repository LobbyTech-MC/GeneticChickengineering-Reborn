package net.guizhanss.gcereborn.core.commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.guizhanlib.minecraft.commands.AbstractCommand;
import net.guizhanss.guizhanlib.minecraft.commands.SubCommand;

@Getter
public abstract class AbstractSubCommand extends SubCommand {

    protected AbstractSubCommand(@Nullable AbstractCommand parent, @Nonnull String name, @Nonnull String usage, SubCommand... subCommands) {
        super(parent, name, (cmd, sender) -> getDescription(name, sender), usage, subCommands);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    private static String getDescription(String name, CommandSender sender) {
        return GeneticChickengineering.getLocalization().getString("commands." + name + ".description");
    }

    protected boolean hasPermission(@Nonnull CommandSender sender) {
        return sender.hasPermission("geneticchickengineering.command." + getName());
    }
}
