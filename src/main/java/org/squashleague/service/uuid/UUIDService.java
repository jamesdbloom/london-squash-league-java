package org.squashleague.service.uuid;

import com.eaio.uuid.UUID;
import org.springframework.stereotype.Component;

/**
 * @author jamesdbloom
 */
@Component
public class UUIDService {

    public String generateUUID() {
        return new UUID().toString();
    }
}
