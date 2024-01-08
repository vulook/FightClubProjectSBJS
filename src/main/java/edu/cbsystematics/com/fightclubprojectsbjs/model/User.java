package edu.cbsystematics.com.fightclubprojectsbjs.model;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(name = "users")
@NamedEntityGraph(name = "User.roles", attributeNodes = @NamedAttributeNode("roles"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Username is required")
    @Size(min = 4, message = "NickName should be between 4 and 40 characters")
    @Pattern.List({
            @Pattern(regexp = "^[^\\s].*$", message = "NickName should not start with space"),
            @Pattern(regexp = "^.*[^\\s]$", message = "NickName should not end with space"),
            @Pattern(regexp = "^((?!  ).)*$", message = "NickName should not contain consecutive spaces"),
    })
    @Column(name = "user_name", nullable = false, columnDefinition = "varchar(40)")
    private String username;

    @NotNull(message = "Birth Date is required")
    @Past
    @Column(name = "birth_date", nullable = false, columnDefinition = "varchar(20)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;


    @NotNull(message = "Email is required")
    @Email
    @Column(name = "email", unique = true, nullable = false, columnDefinition = "varchar(50)")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern.List({
            @Pattern(regexp = ".*[0-9].*", message = "Password should contain at least one digit"),
            @Pattern(regexp = ".*[a-z].*", message = "Password should contain at least one lowercase letter"),
            @Pattern(regexp = ".*[A-Z].*", message = "Password should contain at least one uppercase letter"),
            @Pattern(regexp = ".*[!@#&()\\[\\]{}:;',?/*~$^+=<>].*", message = "Password should contain at least one special character"),
            @Pattern(regexp = "^[\\S]+$", message = "Password must not contain spaces")
    })
    @Column(name = "password", nullable = false, columnDefinition = "varchar(100)")
    private String password;

    @Column(name = "data_reg", nullable = false, columnDefinition = "varchar(30)")
    private LocalDateTime dataReg;

    @Column(name = "verification_code", columnDefinition = "varchar(80)")
    private String verificationCode;

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new ArrayList<>();


    public User(String username, String password, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(String username, LocalDate birthDate, String email, String password) {
        this.username = username;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
    }

    public User(String username, LocalDate birthDate, String email, String password, boolean enabled) {
        this.username = username;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String username, LocalDate birthDate, String email, String password, LocalDateTime dataReg, String verificationCode, boolean enabled) {
        this.username = username;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.dataReg = dataReg;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}
