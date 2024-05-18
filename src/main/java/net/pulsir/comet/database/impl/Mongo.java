package net.pulsir.comet.database.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.pulsir.comet.Comet;
import net.pulsir.comet.database.IDatabase;
import net.pulsir.comet.mongo.MongoManager;
import net.pulsir.comet.punishment.Punishment;
import net.pulsir.comet.utils.wrapper.impl.PunishmentWrapper;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mongo implements IDatabase {

    private final MongoManager mongoManager = new MongoManager();

    @Override
    public void loadPlayer(UUID uuid) {
        FindIterable<Document> iterable = mongoManager.getPunishments().find();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if (Comet.getInstance().getPunishmentManger().getPunishments().get(uuid) == null) {
                    List<Punishment> punishmentList = new ArrayList<>(List.of(new PunishmentWrapper().unwrap(document)));
                    Comet.getInstance().getPunishmentManger().getPunishments().put(uuid, punishmentList);
                } else {
                    Comet.getInstance().getPunishmentManger().getPunishments().get(uuid).add(new PunishmentWrapper().unwrap(document));
                }
            }
        }
    }

    @Override
    public void savePlayers() {
        Comet.getInstance().getPunishmentManger().getPunishments().forEach((uuid, punishment) -> {
            punishment.forEach(playerPunishment -> mongoManager.getPunishments().replaceOne(Filters.eq("uuid", playerPunishment.getUuid().toString()),
                    new PunishmentWrapper().wrap(playerPunishment), new ReplaceOptions().upsert(true)));
        });
    }
}
