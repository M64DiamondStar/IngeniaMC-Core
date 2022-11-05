package me.m64diamondstar.ingeniamccore.general.player;

import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.data.files.PlayerConfig;
import me.m64diamondstar.ingeniamccore.general.scoreboard.Scoreboard;
import me.m64diamondstar.ingeniamccore.general.tablist.TabList;
import me.m64diamondstar.ingeniamccore.utils.messages.Colors;
import me.m64diamondstar.ingeniamccore.utils.messages.MessageLocation;
import me.m64diamondstar.ingeniamccore.utils.messages.MessageType;
import me.m64diamondstar.ingeniamccore.wands.Wands;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IngeniaPlayer {

    private final Player player;
    private Scoreboard scoreboard;
    private PlayerConfig config;
    private Inventory previousInventory;

    public IngeniaPlayer (Player player){
        this.player = player;
        this.config = new PlayerConfig(player.getUniqueId());
    }

    public void startUp(){
        player.setCollidable(false);

        if(player.isOp())
            player.setPlayerListName(Colors.format("#c43535&lLead #ffdede" + getName()));
        else if(player.hasPermission("ingenia.team"))
            player.setPlayerListName(Colors.format("#4180bf&lTeam #deefff" + getName()));
        else if(player.hasPermission("ingenia.vip+"))
            player.setPlayerListName(Colors.format("#9054b0VIP+ #f9deff" + getName()));
        else if(player.hasPermission("ingenia.vip"))
            player.setPlayerListName(Colors.format("#54b0b0VIP #defdff" + getName()));
        else
            player.setPlayerListName(Colors.format("#a1a1a1Visitor #cccccc" + getName()));

    }

    public Player getPlayer(){
        return this.player;
    }

    public String getName(){
        return player.getName();
    }

    public String getPrefix(){
        if(player.isOp())
            return Colors.format("#c43535&lLead");
        else if(player.hasPermission("ingenia.team"))
            return Colors.format("#4180bf&lTeam");
        else if(player.hasPermission("ingenia.vip+"))
            return Colors.format("#9054b0VIP+");
        else if(player.hasPermission("ingenia.vip"))
            return Colors.format("#54b0b0VIP");
        else
        return Colors.format("#a1a1a1Visitor");
    }

    public void sendMessage(String string){
        player.sendMessage(Colors.format(string));
    }

    public void sendMessage(String string, MessageType messageType){
        player.sendMessage(Colors.format(string, messageType));
    }

    public void sendMessage(String string, MessageLocation messageLocation){
        switch (messageLocation) {
            case CHAT -> player.sendMessage(Colors.format(string));
            case HOTBAR -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Colors.format(string)));
            case TITLE -> player.sendTitle(Colors.format(string), "", 10, 50, 10);
            case SUBTITLE -> player.sendTitle("", Colors.format(string), 10, 50, 10);
        }
    }

    public void setGameMode(GameMode gameMode){
        player.setGameMode(gameMode);
        this.sendMessage("Your gamemode has changed to: " + gameMode.toString().toLowerCase(), MessageType.BACKGROUND);
    }

    public void sendMessage(String s, String color) {
        this.sendMessage(Colors.format(color + s));
    }

    public long getExp(){
        this.config = new PlayerConfig(player.getUniqueId());
        return config.getExp();
    }

    public void setExp(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setExp(l);
    }

    public void addExp(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setExp(l + getExp());
    }

    public long getBal(){
        this.config = new PlayerConfig(player.getUniqueId());
        return config.getBal();
    }

    public void setBal(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setBal(l);
    }

    public void addBal(long l){
        this.config = new PlayerConfig(player.getUniqueId());
        config.setBal(l + getBal());
    }

    public void setScoreboard(boolean on){
        if(scoreboard == null)
            scoreboard = new Scoreboard(this);

        if(on) {
            scoreboard.createBoard();
            scoreboard.startUpdating();
            scoreboard.showBoard();
        }else{
            scoreboard.hideBoard();
        }
    }

    public void setTablist(boolean on){
        if(on) {
            TabList tabList = new TabList(Main.plugin);
            for (String header : Objects.requireNonNull(Main.plugin.getConfig().getConfigurationSection("Tablist")).getStringList("Header")) {
                tabList.addHeader(header, player);
            }

            for (String footer : Objects.requireNonNull(Main.plugin.getConfig().getConfigurationSection("Tablist")).getStringList("Footer")) {
                tabList.addFooter(
                        footer.replace(
                                "%online%", Bukkit.getOnlinePlayers().size() + ""
                        )
                );
            }
            tabList.showTab(player);
        }else{
            TabList tabList = new TabList(Main.plugin);
            tabList.clearHeader();
            tabList.clearFooter();
            tabList.showTab(player);
        }
    }

    public List<ItemStack> getWands(){
        return Wands.getAccessibleWands(player);
    }

    public void setWand(ItemStack item){
        player.getInventory().setItem(5, item);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 1.5F);
    }

    public void setJoinMessage(String msg){
        config.setJoinMessage(msg);
    }

    public String getJoinMessage(){
        return Objects.requireNonNull(config.getJoinMessage()).replace("%player%", Colors.format(config.getJoinColor() + player.getName() + "#ababab"));
    }

    public void setLeaveMessage(String msg){
        config.setLeaveMessage(msg);
    }

    public String getLeaveMessage(){
        return Objects.requireNonNull(config.getLeaveMessage()).replace("%player%", Colors.format(config.getJoinColor() + player.getName() + "#ababab"));
    }

    public Inventory getPreviousInventory(){
        return this.previousInventory;
    }

    public void openInventory(Inventory inventory){
        if(player.getOpenInventory().getType() != InventoryType.CRAFTING)
            this.previousInventory = player.getOpenInventory().getTopInventory();

        player.openInventory(inventory);
    }

    public void giveMenuItem(){
        ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(Colors.format("#f4b734&lIngeniaMC"));
        itemMeta.setLore(List.of(Colors.format(MessageType.LORE + "Click to open the IngeniaMC menu.")));
        itemStack.setItemMeta(itemMeta);

        player.getInventory().setItem(4, itemStack);
    }

}
