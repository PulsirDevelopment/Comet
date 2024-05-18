package net.pulsir.comet.punishment.manager;

import lombok.Getter;
import net.pulsir.comet.punishment.Punishment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class PunishmentManger {

    private final Map<UUID, List<Punishment>> punishments = new HashMap<>();
}
