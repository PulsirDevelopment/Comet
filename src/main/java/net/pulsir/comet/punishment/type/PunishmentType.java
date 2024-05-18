package net.pulsir.comet.punishment.type;

import lombok.Getter;

@Getter
public enum PunishmentType {

    BLACKLIST("Blacklist"), BAN("Ban"), TEMP_BAN("TempBan"), MUTE("Mute"),
    TEMP_MUTE("TempMute"), WARN("Warn"), KICK("Kick");

    final String name;

    PunishmentType(String name) {
        this.name = name;
    }
}
