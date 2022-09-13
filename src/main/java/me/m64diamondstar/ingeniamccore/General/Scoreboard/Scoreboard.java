package me.m64diamondstar.ingeniamccore.General.Scoreboard;

import me.m64diamondstar.ingeniamccore.General.Player.IngeniaPlayer;
import me.m64diamondstar.ingeniamccore.Main;
import me.m64diamondstar.ingeniamccore.Utils.Colors;
import me.m64diamondstar.ingeniamccore.Utils.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class Scoreboard {

    private final IngeniaPlayer player;
    private Objective objective;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Team bal;
    private Team onl;
    private Team ip;

    public Scoreboard(IngeniaPlayer player){
        this.player = player;
    }

    public void createBoard() {

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        scoreboard = manager.getNewScoreboard();

        objective = scoreboard.registerNewObjective("IngeniaMC-Board", Criteria.DUMMY, Colors.format("#f4b734&lIngeniaMC"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(Colors.format("&7&7")).setScore(4);
        objective.getScore(Colors.format("&7&7&6")).setScore(1);

        bal = scoreboard.registerNewTeam("bal");
        bal.addEntry(Colors.format("&2"));

        onl = scoreboard.registerNewTeam("onl");
        onl.addEntry(Colors.format("&1"));

        ip = scoreboard.registerNewTeam("ip");
        ip.addEntry(Colors.format("&0"));
        ip.setPrefix(Colors.format("   play.IngeniaMC.net", MessageType.BACKGROUND));
        objective.getScore(Colors.format("&0")).setScore(0);

    }

    public void showBoard(){
        player.getPlayer().setScoreboard(scoreboard);
    }

    public void startUpdating(){
        new BukkitRunnable(){

            @Override
            public void run() {

                bal.setPrefix(Colors.format("#f4b734 » Golden Stars: &r" + player.getBal() + ":gs:"));
                objective.getScore(Colors.format("&2")).setScore(3);

                onl.setPrefix(Colors.format("#f4b734 » Online Players: &r" + Bukkit.getOnlinePlayers().size()));
                objective.getScore(Colors.format("&1")).setScore(2);

            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
    }

    public void hideBoard(){

    }

}
