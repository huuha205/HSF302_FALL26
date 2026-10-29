package fe.de201028.ch08javafx.b3;

import java.time.LocalDate;
import java.util.*;

public final class UserStore {
    private static final Map<String, User> USERS = new HashMap<>();

    static {
        add(new User("Quản trị viên", "admin@example.com", "admin", "Admin@123",
                "Khác", LocalDate.of(2000, 1, 1), "Kỹ thuật phần mềm", List.of()));
    }

    private UserStore() {}

    public static boolean exists(String username) {
        return USERS.containsKey(username.trim().toLowerCase());
    }

    public static void add(User u) {
        USERS.put(u.username().trim().toLowerCase(), u);
    }

    public static Optional<User> login(String username, String password) {
        return Optional.ofNullable(USERS.get(username.trim().toLowerCase()))
                .filter(u -> u.password().equals(password));
    }
}
