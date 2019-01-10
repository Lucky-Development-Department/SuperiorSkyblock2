package com.ome_r.superiorskyblock.commands.command.admin;

import com.ome_r.superiorskyblock.Locale;
import com.ome_r.superiorskyblock.SuperiorSkyblock;
import com.ome_r.superiorskyblock.commands.ICommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CmdAdminSave implements ICommand {

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("save");
    }

    @Override
    public String getPermission() {
        return "superior.admin.save";
    }

    @Override
    public String getUsage() {
        return "island admin save";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public void execute(SuperiorSkyblock plugin, CommandSender sender, String[] args) {
        new Thread(() -> {
            Locale.SAVE_PROCCESS_REQUEST.send(sender);
            plugin.getDataHandler().saveDatabase(false);
            Locale.SAVED_DATABASE.send(sender);
        }).start();
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblock plugin, CommandSender sender, String[] args) {
        return null;
    }
}
