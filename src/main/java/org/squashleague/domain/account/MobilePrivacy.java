package org.squashleague.domain.account;

import org.codehaus.plexus.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jamesdbloom
 */
public enum MobilePrivacy {
    SECRET,
    SHOW_OPPONENTS,
    SHOW_ALL;

    public static Map<String, String> enumToFormOptionMap() {
        Map<String, String> names = new HashMap<>();
        for (MobilePrivacy mobilePrivacy : values()) {
            names.put(mobilePrivacy.name(), StringUtils.capitaliseAllWords(mobilePrivacy.name().toLowerCase().replace('_', ' ')));
        }
        return names;
    }
}
