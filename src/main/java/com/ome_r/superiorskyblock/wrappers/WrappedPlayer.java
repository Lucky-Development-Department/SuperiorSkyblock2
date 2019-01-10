package com.ome_r.superiorskyblock.wrappers;

import com.ome_r.superiorskyblock.SuperiorSkyblock;
import com.ome_r.superiorskyblock.island.Island;
import com.ome_r.superiorskyblock.island.IslandPermission;
import com.ome_r.superiorskyblock.island.IslandRole;
import com.ome_r.superiorskyblock.utils.jnbt.CompoundTag;
import com.ome_r.superiorskyblock.utils.jnbt.IntTag;
import com.ome_r.superiorskyblock.utils.jnbt.StringTag;
import com.ome_r.superiorskyblock.utils.jnbt.Tag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WrappedPlayer {

    private static SuperiorSkyblock plugin = SuperiorSkyblock.getPlugin();

    private final UUID player;

    private UUID teamLeader;
    private String name, textureValue = "";
    private IslandRole islandRole;

    private int islandSize = plugin.getSettings().defaultIslandSize;
    private boolean worldBorderEnabled = true;
    private boolean blocksStackerEnabled = true;
    private boolean schematicModeEnabled = false;
    private boolean teamChatEnabled = false;
    private WrappedLocation schematicPos1 = null, schematicPos2 = null;

    public WrappedPlayer(CompoundTag tag){
        Map<String, Tag> compoundValues = tag.getValue();

        player = UUID.fromString(((StringTag) compoundValues.get("player")).getValue());
        teamLeader = UUID.fromString(((StringTag) compoundValues.get("teamLeader")).getValue());
        name = ((StringTag) compoundValues.get("name")).getValue();
        islandRole = IslandRole.valueOf(((StringTag) compoundValues.get("islandRole")).getValue());
        islandSize  = ((IntTag) compoundValues.get("islandSize")).getValue();
        textureValue = ((StringTag) compoundValues.get("textureValue")).getValue();

        if(plugin.getGrid().getIsland(WrappedPlayer.of(teamLeader)) == null)
            teamLeader = player;
    }

    public WrappedPlayer(UUID player){
        OfflinePlayer offlinePlayer;
        this.player = player;
        this.name = (offlinePlayer = Bukkit.getOfflinePlayer(player)) == null ? "null" : offlinePlayer.getName();
        this.teamLeader = player;
        this.islandRole = IslandRole.GUEST;
    }

    public UUID getUniqueId(){
        return player;
    }

    public String getName() {
        return name;
    }

    public String getTextureValue() {
        return textureValue;
    }

    public void setTexture(String texture){
        setTextureValue(texture);
    }

    public void setTextureValue(String textureValue) {
        this.textureValue = textureValue;
    }

    public void updateName(){
        this.name = Bukkit.getPlayer(player).getName();
    }

    public World getWorld(){
        return getLocation().getWorld();
    }

    public Location getLocation(){
        return asPlayer().getLocation();
    }

    public UUID getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(UUID teamLeader) {
        this.teamLeader = teamLeader;
    }

    public Island getIsland(){
        return plugin.getGrid().getIsland(this);
    }

    public IslandRole getIslandRole() {
        return islandRole;
    }

    public void setIslandRole(IslandRole islandRole) {
        this.islandRole = islandRole;
    }

    public int getIslandSize() {
        return islandSize;
    }

    public void setIslandSize(int islandSize) {
        this.islandSize = islandSize;
    }

    public boolean hasWorldBorderEnabled() {
        return worldBorderEnabled;
    }

    public void toggleWorldBorder() {
        worldBorderEnabled = !worldBorderEnabled;
    }

    public boolean hasBlocksStackerEnabled() {
        return blocksStackerEnabled;
    }

    public void toggleBlocksStacker() {
        blocksStackerEnabled = !blocksStackerEnabled;
    }

    public boolean hasSchematicModeEnabled() {
        return schematicModeEnabled;
    }

    public void toggleSchematicMode() {
        schematicModeEnabled = !schematicModeEnabled;
    }

    public boolean hasTeamChatEnabled() {
        return teamChatEnabled;
    }

    public void toggleTeamChat() {
        teamChatEnabled = !teamChatEnabled;
    }

    public WrappedLocation getSchematicPos1() {
        return schematicPos1;
    }

    public void setSchematicPos1(Block block) {
        this.schematicPos1 = block == null ? null : WrappedLocation.of(block.getLocation());
    }

    public WrappedLocation getSchematicPos2() {
        return schematicPos2;
    }

    public void setSchematicPos2(Block block) {
        this.schematicPos2 = block == null ? null : WrappedLocation.of(block.getLocation());
    }

    public Player asPlayer(){
        return asOfflinePlayer().getPlayer();
    }

    public OfflinePlayer asOfflinePlayer(){
        return Bukkit.getOfflinePlayer(player);
    }

    public boolean isOnline(){
        return asOfflinePlayer().isOnline();
    }

    public boolean hasPermission(String permission){
        return permission.isEmpty() || asPlayer().hasPermission(permission);
    }

    public boolean hasPermission(IslandPermission permission){
        Island island = getIsland();
        return island != null && island.hasPermission(this, permission);
    }

    public CompoundTag getAsTag(){
        Map<String, Tag> compoundValues = new HashMap<>();

        compoundValues.put("player", new StringTag(player.toString()));
        compoundValues.put("teamLeader", new StringTag(teamLeader.toString()));
        compoundValues.put("name", new StringTag(this.name));
        compoundValues.put("islandRole", new StringTag(islandRole.name()));
        compoundValues.put("islandSize", new IntTag(islandSize));
        compoundValues.put("textureValue", new StringTag(textureValue));

        return new CompoundTag(compoundValues);
    }

    @Override
    public String toString() {
        return "WrappedPlayer{" +
                "uuid=[" + player + "]," +
                "name=[" + name + "]" +
                "}";
    }

    public static WrappedPlayer of(CommandSender sender){
        return of((Player) sender);
    }

    public static WrappedPlayer of(Player player){
        return of(player.getUniqueId());
    }

    public static WrappedPlayer of(UUID player){
        return plugin.getPlayers().getWrappedPlayer(player);
    }

    public static WrappedPlayer of(String name){
        return plugin.getPlayers().getWrappedPlayer(name);
    }

}
