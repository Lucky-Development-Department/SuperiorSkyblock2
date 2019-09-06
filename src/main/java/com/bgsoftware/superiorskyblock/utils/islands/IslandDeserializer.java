package com.bgsoftware.superiorskyblock.utils.islands;

import com.bgsoftware.superiorskyblock.api.enums.Rating;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandRole;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.island.SIsland;
import com.bgsoftware.superiorskyblock.island.SPermissionNode;
import com.bgsoftware.superiorskyblock.utils.FileUtil;
import com.bgsoftware.superiorskyblock.utils.key.KeyMap;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class IslandDeserializer {

    public static void deserializeMembers(String members, Set<UUID> membersSet){
        for(String uuid : members.split(",")) {
            try {
                membersSet.add(UUID.fromString(uuid));
            }catch(Exception ignored){}
        }
    }

    public static void deserializeBanned(String banned, Set<UUID> bannedSet){
        deserializeMembers(banned, bannedSet);
    }

    public static void deserializePermissions(String permissions, Map<Object, SPermissionNode> permissionNodes){
        for(String entry : permissions.split(",")) {
            try {
                String[] sections = entry.split("=");
                try {
                    permissionNodes.put(IslandRole.valueOf(sections[0]), new SPermissionNode(sections.length == 1 ? "" : sections[1]));
                }catch(Exception ex){
                    permissionNodes.put(UUID.fromString(sections[0]), new SPermissionNode(sections.length == 1 ? "" : sections[1]));
                }
            }catch(Exception ignored){ }
        }
    }

    public static void deserializeUpgrades(String upgrades, Map<String, Integer> upgradesMap){
        for(String entry : upgrades.split(",")) {
            try {
                String[] sections = entry.split("=");
                upgradesMap.put(sections[0], Integer.valueOf(sections[1]));
            }catch(Exception ignored){}
        }
    }

    public static void deserializeWarps(String warps, Map<String, SIsland.WarpData> warpsMap){
        for(String entry : warps.split(";")) {
            try {
                String[] sections = entry.split("=");
                boolean privateFlag = sections.length == 3 && Boolean.parseBoolean(sections[2]);
                warpsMap.put(sections[0], new SIsland.WarpData(FileUtil.toLocation(sections[1]), privateFlag));
            }catch(Exception ignored){}
        }
    }

    public static void deserializeBlockCounts(String blocks, Island island){
        for(String entry : blocks.split(";")){
            try{
                String[] sections = entry.split("=");
                island.handleBlockPlace(Key.of(sections[0]), Integer.parseInt(sections[1]), false);
            }catch(Exception ignored){}
        }
    }

    public static void deserializeBlockLimits(String blocks, KeyMap<Integer> blockLimits){
        for(String limit : blocks.split(",")){
            try {
                String[] sections = limit.split("=");
                blockLimits.put(Key.of(sections[0]), Integer.parseInt(sections[1]));
            }catch(Exception ignored){}
        }
    }

    public static void deserializeRatings(String ratings, Map<UUID, Rating> ratingsMap){
        for(String entry : ratings.split(";")){
            try{
                String[] sections = entry.split("=");
                ratingsMap.put(UUID.fromString(sections[0]), Rating.valueOf(Integer.parseInt(sections[1])));
            }catch(Exception ignored){}
        }
    }

}
