package com.ome_r.superiorskyblock.commands.command;

import com.ome_r.superiorskyblock.Locale;
import com.ome_r.superiorskyblock.SuperiorSkyblock;
import com.ome_r.superiorskyblock.commands.ICommand;
import com.ome_r.superiorskyblock.island.Island;
import com.ome_r.superiorskyblock.wrappers.WrappedPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class CmdTeam implements ICommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("team", "showteam");
    }

    @Override
    public String getPermission() {
        return "superior.island.team";
    }

    @Override
    public String getUsage() {
        return "island team [player-name]";
    }

    @Override
    public int getMinArgs() {
        return 1;
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
        WrappedPlayer targetPlayer;

        if(args.length == 1){
            if(!(sender instanceof Player)){
                Locale.sendMessage(sender, "&cYou must specify a player's name.");
                return;
            }

            targetPlayer = WrappedPlayer.of(sender);
        }
        else{
            targetPlayer = WrappedPlayer.of(args[1]);

            if(targetPlayer == null){
                Locale.INVALID_PLAYER.send(sender, args[1]);
                return;
            }
        }

        Island island = targetPlayer.getIsland();

        if(island == null){
            if(targetPlayer.asPlayer().equals(sender))
                Locale.INVALID_ISLAND.send(sender);
            else
                Locale.INVALID_ISLAND_OTHER.send(sender, targetPlayer.getName());
            return;
        }

        new Thread(() -> {
            StringBuilder infoMessage = new StringBuilder(), adminsMessage = new StringBuilder(),
                    modsMessage = new StringBuilder(), membersMessage = new StringBuilder();

            if(!Locale.ISLAND_TEAM_STATUS_HEADER.isEmpty())
                infoMessage.append(Locale.ISLAND_TEAM_STATUS_HEADER.getMessage(island.getOwner().getName(),
                        island.getAllMembers().size(), island.getTeamLimit())).append("\n");

            List<UUID> members = island.getAllMembers();
            members.sort(Comparator.comparing(o -> WrappedPlayer.of(o).getName()));

            String onlineStatus = Locale.ISLAND_TEAM_STATUS_ONLINE.getMessage(),
                    offlineStatus = Locale.ISLAND_TEAM_STATUS_OFFLINE.getMessage();
            WrappedPlayer wrappedMember;

            for(UUID member : members){
                wrappedMember = WrappedPlayer.of(member);
                switch (wrappedMember.getIslandRole()){
                    case LEADER:
                        if(!Locale.ISLAND_TEAM_STATUS_LEADER.isEmpty())
                            infoMessage.append(Locale.ISLAND_TEAM_STATUS_LEADER.getMessage(wrappedMember.getName(),
                                    wrappedMember.isOnline() ? onlineStatus : offlineStatus));
                        break;
                    case ADMIN:
                        if(!Locale.ISLAND_TEAM_STATUS_ADMINS.isEmpty())
                            infoMessage.append(Locale.ISLAND_TEAM_STATUS_ADMINS.getMessage(wrappedMember.getName(),
                                    wrappedMember.isOnline() ? onlineStatus : offlineStatus));
                        break;
                    case MODERATOR:
                        if(!Locale.ISLAND_TEAM_STATUS_MODS.isEmpty())
                            infoMessage.append(Locale.ISLAND_TEAM_STATUS_MODS.getMessage(wrappedMember.getName(),
                                    wrappedMember.isOnline() ? onlineStatus : offlineStatus));
                        break;
                    case MEMBER:
                        if(!Locale.ISLAND_TEAM_STATUS_MEMBERS.isEmpty())
                            infoMessage.append(Locale.ISLAND_TEAM_STATUS_MEMBERS.getMessage(wrappedMember.getName(),
                                    wrappedMember.isOnline() ? onlineStatus : offlineStatus));
                        break;
                }
            }

            infoMessage.append(adminsMessage).append(modsMessage).append(membersMessage);

            Locale.sendMessage(sender, infoMessage.toString());
        }).start();
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblock plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
