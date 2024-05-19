package net.pulsir.comet.utils.wrapper.impl;

import net.pulsir.comet.punishment.Punishment;
import net.pulsir.comet.punishment.type.PunishmentType;
import net.pulsir.comet.utils.wrapper.IWrapper;
import org.bson.Document;

import java.util.Date;
import java.util.UUID;

public class PunishmentWrapper implements IWrapper<Document, Punishment> {

    @Override
    public Document wrap(Punishment punishment) {
        Document document = new Document();
        document.put("uuid", punishment.getUuid().toString());
        document.put("type", punishment.getType().getName());
        document.put("createdAt", punishment.getCreatedAt());
        document.put("expiresAt", punishment.getExpiresAt());
        document.put("reason", punishment.getReason());
        document.put("bannedBy", punishment.getBannedBy());
        return document;
    }

    @Override
    public Punishment unwrap(Document document) {
        return new Punishment(UUID.fromString(document.getString("uuid")),
                PunishmentType.valueOf(document.getString("type").toUpperCase()),
                document.getDate("createdAt"),
                document.getDate("expiresAt"),
                document.getString("reason"),
                document.getString("bannedBy"));
    }

    @Override
    public String toString(Punishment punishment) {
        String uuid = punishment.getUuid().toString();
        String type = punishment.getType().getName();
        long createdAt = punishment.getCreatedAt().getTime();
        long expiresAt = punishment.getExpiresAt().getTime();
        String reason = punishment.getReason();
        String bannedBy = punishment.getBannedBy();

        return uuid + "<splitter>" + type + "<splitter>" + createdAt + "<splitter>" + expiresAt + "<splitter>" + reason + "<splitter>" + bannedBy;
    }

    @Override
    @SuppressWarnings("ALL")
    public Punishment to(String string) {
        UUID uuid = UUID.fromString(string.split("<splitter>")[0]);
        PunishmentType punishmentType = PunishmentType.valueOf(string.split("<splitter>")[1].toUpperCase());
        Date createdAt = new Date(Long.parseLong(string.split("<splitter>")[2]));
        Date expiresAt = new Date(Long.parseLong(string.split("<splitter>")[3]));
        String reason = string.split("<splitter>")[4];
        String bannedBy = string.split("<splitter>")[5];

        return new Punishment(uuid, punishmentType, createdAt, expiresAt, reason, bannedBy);
    }
}
