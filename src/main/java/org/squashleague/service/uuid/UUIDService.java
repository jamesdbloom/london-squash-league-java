package org.squashleague.service.uuid;

import com.eaio.uuid.UUID;
import org.springframework.stereotype.Component;
import org.squashleague.domain.account.User;

import java.util.regex.Pattern;

/**
 * @author jamesdbloom
 */
@Component
public class UUIDService {

    // ad576200-c58a-11e2-b1ce-e4ce8f5b7472
    // 7a00d5a0-c565-11e2-a7e4-e4ce8f5b7472
    // 3efcf470-c565-11e2-a7e4-e4ce8f5b7472

    private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");

    public String generateUUID() {
        return new UUID().toString();
    }

    public boolean hasMatchingUUID(User user, String oneTimeToken) {
        boolean userTokenValid = user != null && user.getOneTimeToken() != null && UUID_PATTERN.matcher(user.getOneTimeToken()).matches();
        boolean matchingTokenValid = oneTimeToken != null && UUID_PATTERN.matcher(oneTimeToken).matches();
        return userTokenValid && matchingTokenValid && user.getOneTimeToken().equals(oneTimeToken);
    }
}
