package org.squashleague.domain.account;

import org.codehaus.plexus.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jamesdbloom
 */
public enum Role {
    ROLE_ANONYMOUS,
    ROLE_USER,
    ROLE_ADMIN;

    public static Map<String, String> enumToFormOptionMap() {
        Map<String, String> names = new HashMap<>();
        for(Role role : values()) {
            names.put(role.name(), StringUtils.capitaliseAllWords(role.name().toLowerCase().replace('_', ' ')));
        }
        return names;
    }
}
