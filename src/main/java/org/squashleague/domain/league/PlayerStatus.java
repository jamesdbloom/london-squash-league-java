package org.squashleague.domain.league;

import org.codehaus.plexus.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jamesdbloom
 */
public enum PlayerStatus {
    ACTIVE,
    INACTIVE;

    public static Map<String, String> enumToFormOptionMap() {
        Map<String, String> names = new HashMap<>();
        for (PlayerStatus playerStatus : values()) {
            names.put(playerStatus.name(), StringUtils.capitaliseAllWords(playerStatus.name().toLowerCase().replace('_', ' ')));
        }
        return names;
    }
}
