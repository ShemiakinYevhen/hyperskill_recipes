package recipes.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Pattern(regexp = "\\S+[@]\\S+[.]\\S+")
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private String role;


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Recipe> recipes = new ArrayList<>();

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
