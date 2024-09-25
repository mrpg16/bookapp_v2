package prod.bookapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "app_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String provider;
    private String password;
    private String firstName;
    private String lastName;
    private String companyName;
    private String phoneNumber;
    private String shortDescription;
    private String description;
    private LocalDateTime firstLogin;
    private LocalDateTime lastLogin;
    private boolean emailVerified = false;
    private String ip;
    private String oauth2AccessToken;
    private String oauth2RefreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}