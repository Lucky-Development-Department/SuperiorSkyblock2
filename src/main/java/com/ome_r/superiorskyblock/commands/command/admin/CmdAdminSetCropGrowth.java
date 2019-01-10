package com.ome_r.superiorskyblock.commands.command.admin;

import com.ome_r.superiorskyblock.Locale;
import com.ome_r.superiorskyblock.SuperiorSkyblock;
import com.ome_r.superiorskyblock.commands.ICommand;
import com.ome_r.superiorskyblock.island.Island;
import com.ome_r.superiorskyblock.wrappers.WrappedPlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CmdAdminSetCropGrowth implements ICommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("setcropgrowth");
    }

    @Override
    public String getPermission() {
        return "superior.admin.setcropgrowth";
    }

    @Override
    public String getUsage() {
        return "island admin setcropgrowth <player-name> <multiplier>";
    }

    @Override
    public int getMinArgs() {
        return 4;
    }

    @Override
    public int getMaxArgs() {
        return 4;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblock plugin, CommandSender sender, String[] args) {
        WrappedPlayer targetPlayer = WrappedPlayer.of(args[2]);

        if(targetPlayer == null){
            Locale.INVALID_PLAYER.send(sender, args[2]);
            return;
        }

        Island island = targetPlayer.getIsland();

        if(island == null){
            Locale.INVALID_ISLAND_OTHER.send(sender, targetPlayer.getName());
            return;
        }

        double multiplier;

        try{
            multiplier = Double.valueOf(args[3]);
        }catch(IllegalArgumentException ex){
            Locale.INVALID_MULTIPLIER.send(sender, args[3]);
            return;
        }

        island.setCropGrowthMultiplier(multiplier);
        Locale.CHANGED_CROP_GROWTH.send(sender, targetPlayer.getName());
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblock plugin, CommandSender sender, String[] args) {
        return null;
    }
}
