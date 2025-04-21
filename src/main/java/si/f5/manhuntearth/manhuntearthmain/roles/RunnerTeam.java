package si.f5.manhuntearth.manhuntearthmain.roles;

import org.bukkit.ChatColor;

public class RunnerTeam extends GameTeam {
    @Override
    String BUKKIT_TEAM_NAME() {
        return "runner";
    }

    @Override
    public ChatColor BUKKIT_TEAM_COLOR() {
        return ChatColor.AQUA;
    }

    @Override
    public String BUKKIT_TEAM_DISPLAY_NAME() {
        return "逃走者";
    }

    @Override
    public String titleOnStartDirection() {
        return "逃げろ";
    }

    @Override
    public String subtitleOnStartDirection() {
        return BUKKIT_TEAM_COLOR()+"逃走者になった まもなく鬼が放出される";
    }

    @Override
    public String[] winConditions() {
        return new String[]{"時間切れ(逃げ切る)","鬼全員が死ぬ(全員を倒す)"};
    }

    @Override
    public String titleOnReleaseHunter() {
        return "鬼が開放された";
    }

    @Override
    public String subtitleOnReleaseHunter() {
        return BUKKIT_TEAM_COLOR()+"鬼の追跡から逃れろ";
    }
}
