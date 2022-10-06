package recipes.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private long id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @Size(min = 1)
    @Column(name = "ingredients")
    private String[] ingredients;

    @NotNull
    @Size(min = 1)
    @Column(name = "directions")
    private String[] directions;

    @NotBlank
    @Column(name = "category")
    private String category;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    public Recipe(String name, String description, String[] ingredients, String[] directions, String category, LocalDateTime date) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
        this.category = category;
        this.date = date;
    }
}
