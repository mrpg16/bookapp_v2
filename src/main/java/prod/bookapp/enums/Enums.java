package prod.bookapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum Enums {
    SLOT_TYPE_FREE("free"),
    SLOT_TYPE_BUSY("busy"),
    APPOINTMENT_STATUS_UNCONFIRMED("unconfirmed"),
    APPOINTMENT_STATUS_CONFIRMED("confirmed"),
    APPOINTMENT_STATUS_REJECTED("rejected");

    private String value;

}

