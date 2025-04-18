package app.ewarehouse.util;

import java.util.UUID;

public class GenerateUuidNumber {
    public static String generateUUIDNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
