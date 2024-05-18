package net.pulsir.comet.database;

import java.util.UUID;

public interface IDatabase {

    void loadPlayer(UUID uuid);
    void savePlayers();
}
