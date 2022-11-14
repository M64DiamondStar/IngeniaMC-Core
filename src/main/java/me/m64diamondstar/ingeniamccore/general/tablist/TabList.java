package me.m64diamondstar.ingeniamccore.general.tablist;

import me.m64diamondstar.ingeniamccore.IngeniaMC;
import me.m64diamondstar.ingeniamccore.utils.messages.Colors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabList {

    private final List<String> header = new ArrayList<>();
    private final List<String> footer = new ArrayList<>();


    private final IngeniaMC plugin;

    public TabList(IngeniaMC plugin) {
        this.plugin = plugin;
    }

    public void showTab(Player player) {


        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            int count1 = 0; //headers
            int count2 = 0; //footers

            @Override
            public void run() {

                if (count1 >= header.size())
                    count1 = 0;
                if (count2 >= footer.size())
                    count2 = 0;


                player.setPlayerListHeaderFooter(header.get(count1), footer.get(count2));

                count1++;
                count2++;
            }

        }, 10, 5);
    }

    public void clearHeader(){
        header.clear();
    }

    public void clearFooter(){
        footer.clear();
    }

    public void addHeader(String header1, Player player) {
        header.add(Colors.format(header1.replace("%player%", player.getName())));
    }

    public void addFooter(String footer1) {
        footer.add(Colors.format(footer1));
    }
}