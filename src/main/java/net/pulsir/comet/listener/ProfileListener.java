package net.pulsir.comet.listener;

import net.pulsir.comet.Comet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class ProfileListener implements Listener {

    @EventHandler
    public void onAsync(AsyncPlayerPreLoginEvent event) {
        Comet.getInstance().getDatabase().loadPlayer(event.getUniqueId());
    }
}
