package net.pulsir.comet.database.impl;

import net.pulsir.comet.Comet;
import net.pulsir.comet.database.IDatabase;
import net.pulsir.comet.punishment.Punishment;
import net.pulsir.comet.utils.wrapper.impl.PunishmentWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FlatFile implements IDatabase {

    @Override
    public void loadPlayers() {
        if (Comet.getInstance().getPunishments().getConfiguration().getConfigurationSection("player") == null) return;
        for (final String player : Objects.requireNonNull(Comet.getInstance().getPunishments().getConfiguration().getConfigurationSection("player")).getKeys(false)) {
            Comet.getInstance().getPunishmentManger().getPunishments()
                    .put(UUID.fromString(Objects.requireNonNull(Comet.getInstance().getPunishments().getConfiguration().getString("player." + player + ".uuid"))),
                            toPunishment(Comet.getInstance().getPunishments().getConfiguration().getStringList("player." + player + ".punishments")));
        }
    }

    @Override
    public void savePlayers() {
        Comet.getInstance().getPunishmentManger().getPunishments().forEach((uuid, punishment) -> {
            List<String> punishmentList = new ArrayList<>();
            punishment.forEach(playerPunishment -> punishmentList.add(new PunishmentWrapper().toString(playerPunishment)));

            Comet.getInstance().getPunishments().getConfiguration().set("player." + uuid.toString() + ".uuid", uuid.toString());
            Comet.getInstance().getPunishments().getConfiguration().set("player." + uuid + ".punishments", punishmentList);
            Comet.getInstance().getPunishments().save();
        });
    }

    private List<Punishment> toPunishment(List<String> stringPunishments) {
        List<Punishment> punishments = new ArrayList<>();
        stringPunishments.forEach(stringPunishment -> punishments.add(new PunishmentWrapper().to(stringPunishment)));
        return punishments;
    }
}
