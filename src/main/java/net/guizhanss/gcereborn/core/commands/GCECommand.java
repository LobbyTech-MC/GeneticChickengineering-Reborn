package net.guizhanss.gcereborn.core.commands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import lombok.Getter;
import net.guizhanss.gcereborn.GeneticChickengineering;
import net.guizhanss.gcereborn.core.commands.subcommands.CalcChanceCommand;
import net.guizhanss.gcereborn.core.commands.subcommands.MakeChickenCommand;
import net.guizhanss.guizhanlib.minecraft.commands.BaseCommand;

@Getter
public final class GCECommand extends BaseCommand {

    @ParametersAreNonnullByDefault
    public GCECommand(PluginCommand command) {
        super(command, (cmd, sender) -> "", "<subcommand>");

        var config = GeneticChickengineering.getConfigService();
        if (config.isSubCommandEnabled("makechicken")) {
            addSubCommand(new MakeChickenCommand(this));
        }
        if (config.isSubCommandEnabled("calcchance")) {
            addSubCommand(new CalcChanceCommand(this));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        // does nothing, handled by subcommands
    }
}
