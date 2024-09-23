package prod.bookapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum Enums {
    SLOT_TYPE_FREE("free"),
    SLOT_TYPE_BUSY("busy"),

    APPOINTMENT_STATUS_UNCONFIRMED("unconfirmed"),
    APPOINTMENT_STATUS_CONFIRMED("confirmed"),
    APPOINTMENT_STATUS_REJECTED("rejected"),

    ONLINE_PROVIDER_ZOOM("ZOOM");

    private String value;

    public static List<String> getOnlineProviders() {
        return Arrays.stream(Enums.values())
                .filter(e -> e.name().startsWith("ONLINE_PROVIDER"))
                .map(Enums::getValue)
                .collect(Collectors.toList());
    }
}

