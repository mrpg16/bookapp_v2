package prod.bookapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "app_venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String fullAddress;
    private String phone;
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;
    private boolean deleted = false;
    private boolean online;
    private String onlineProvider;
    private String link;
}
