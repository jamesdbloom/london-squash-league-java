package org.squashleague.domain.account;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.plexus.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author squashleague
 */
public enum MobilePrivacy {
    SECRET,
    SHOW_OPPONENTS,
    SHOW_ALL;

    public static Map<String, String> enumToFormOptionMap() {
        Map<String, String> names = new HashMap<>();
        for(MobilePrivacy mobilePrivacy : values()) {
            names.put(mobilePrivacy.name(), StringUtils.capitaliseAllWords(mobilePrivacy.name().toLowerCase().replace('_', ' ')));
        }
        return names;
    }
}
