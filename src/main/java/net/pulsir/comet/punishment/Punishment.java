package net.pulsir.comet.punishment;

import lombok.Getter;
import lombok.Setter;
import net.pulsir.comet.punishment.type.PunishmentType;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class Punishment {

    private UUID uuid;
    private PunishmentType type;
    private Date createdAt;
    private Date expiresAt;
    private String reason;
    private String bannedBy;

    public Punishment(UUID uuid, PunishmentType type, Date createdAt, Date expiresAt, String reason, String bannedBy) {
        this.uuid = uuid;
        this.type = type;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.reason = reason;
        this.bannedBy = bannedBy;
    }
}
