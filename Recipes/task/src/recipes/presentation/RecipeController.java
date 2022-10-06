package recipes.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.business.Recipe;
import recipes.business.RecipeService;
import recipes.business.User;
import recipes.business.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@Controller
@Validated
public class RecipeController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder bCryptEncoder;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/api/register")
    public ResponseEntity<String> registerNewUser(@Valid @RequestBody User newUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseBodyObject = mapper.createObjectNode();

        if (userService.existsByEmail(newUser.getEmail())) {
            try {
                responseBodyObject.put("error", "This user is already registered!");
                return ResponseEntity.status(400).headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        } else {
            newUser.setPassword(bCryptEncoder.encode(newUser.getPassword()));
            newUser.setRole("USER");
            User addedUser = userService.save(newUser);
            responseBodyObject.put("id", addedUser.getId());
            responseBodyObject.put("role", addedUser.getRole());

            try {
                return ResponseEntity.ok().headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        }
    }

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<String> getRecipeById(@PathVariable(value = "id") @Min(1) int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseBodyObject = mapper.createObjectNode();

        if (recipeService.existsById(id)) {
            try {
                return ResponseEntity.ok().headers(headers)
                        .body(mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(convertRecipeToJsonObject(recipeService.findRecipeById(id), mapper)));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        } else {
            try {
                responseBodyObject.put("error", String.format("Recipe with id %d was not found!", id));
                return ResponseEntity.status(404).headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        }
    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<String> getRecipeByCategoryOrName(@RequestParam(value = "category", required = false) String category,
                                                            @RequestParam(value = "name", required = false) String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseBodyObject = mapper.createObjectNode();

        if ((category == null && name == null) || (category != null && name != null)) {
            try {
                responseBodyObject.put("error", "Request should contain only one search parameter!");
                return ResponseEntity.status(400).headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        } else {
            List<Recipe> recipes;

            if (category != null) {
                BiFunction<Recipe, String, Boolean> categoryFilterFunction =
                        (recipe, categoryName) -> recipe.getCategory().equalsIgnoreCase(categoryName);
                recipes = filterAndSortRecipes(categoryFilterFunction, category);
            } else {
                BiFunction<Recipe, String, Boolean> nameFilterFunction =
                        (recipe, recipeName) -> recipe.getName().toLowerCase().contains(recipeName.toLowerCase());
                recipes = filterAndSortRecipes(nameFilterFunction, name);
            }

            try {
                ArrayNode root = mapper.createArrayNode();
                for (Recipe recipe : recipes) {
                    root.add(convertRecipeToJsonObject(recipe, mapper));
                }
                return ResponseEntity.ok().headers(headers)
                        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        }
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<String> addNewRecipe(@Valid @RequestBody Recipe newRecipe, @AuthenticationPrincipal UserDetails userDetails) {
        //User owner = userService.findUserByEmail(userDetails.getUsername());
        newRecipe.setDate(LocalDateTime.now());
        newRecipe.setOwner(userService.findUserByEmail(userDetails.getUsername()));
        long newRecipeId = recipeService.save(newRecipe).getId();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseBodyObject = mapper.createObjectNode();
        responseBodyObject.put("id", newRecipeId);

        try {
            return ResponseEntity.ok().headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Error occurred during json processing!");
        }
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<String> updateRecipe(@Valid @RequestBody Recipe updatedRecipe,
                                               @PathVariable(value = "id") @Min(1) int id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if (recipeService.existsById(id)) {
            Recipe existingRecipe = recipeService.findRecipeById(id);

            if (existingRecipe.getOwner().getEmail().equals(userDetails.getUsername())) {
                updatedRecipe.setDate(LocalDateTime.now());
                updatedRecipe.setId(recipeService.findRecipeById(id).getId());
                updatedRecipe.setOwner(userService.findUserByEmail(userDetails.getUsername()));
                recipeService.save(updatedRecipe);
                return ResponseEntity.status(204).headers(headers).body("Recipe was successfully updated!");
            } else {
                return ResponseEntity.status(403).headers(headers).body("You should be owner of recipe to be able to update it!");
            }
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode responseBodyObject = mapper.createObjectNode();
                responseBodyObject.put("error", String.format("Recipe with id %d was not found!", updatedRecipe.getId()));
                return ResponseEntity.status(404).headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<String> deleteRecipeById(@PathVariable(value = "id") @Min(1) int id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseBodyObject = mapper.createObjectNode();

        if (recipeService.existsById(id)) {
            Recipe existingRecipe = recipeService.findRecipeById(id);

            if (existingRecipe.getOwner().getEmail().equals(userDetails.getUsername())) {
                recipeService.deleteById(id);
                responseBodyObject.put("message", String.format("Recipe with id %d was successfully deleted!", id));

                try {
                    return ResponseEntity.status(204).headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
                } catch (IOException e) {
                    return ResponseEntity.internalServerError().body("Error occurred during json processing!");
                }
            } else {
                return ResponseEntity.status(403).headers(headers).body("You should be owner of recipe to be able to update it!");
            }
        } else {
            try {
                responseBodyObject.put("error", String.format("Recipe with id %d was not found!", id));
                return ResponseEntity.status(404).headers(headers).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseBodyObject));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error occurred during json processing!");
            }
        }
    }

    private ObjectNode convertRecipeToJsonObject(Recipe recipe, ObjectMapper mapper) {
        ObjectNode element = mapper.createObjectNode();
        element.put("name", recipe.getName());
        element.put("description", recipe.getDescription());
        ArrayNode ingredients = mapper.createArrayNode();
        Stream.of(recipe.getIngredients()).forEach(ingredients::add);
        element.set("ingredients", ingredients);
        ArrayNode directions = mapper.createArrayNode();
        Stream.of(recipe.getDirections()).forEach(directions::add);
        element.set("directions", directions);
        element.put("category", recipe.getCategory());
        element.put("date", recipe.getDate().format(FORMATTER).replace(' ', 'T'));
        return element;
    }

    private List<Recipe> filterAndSortRecipes(BiFunction<Recipe, String, Boolean> filterFunction, String filterParameter) {
        List<Recipe> recipes = new ArrayList<>();
        recipeService.findAllRecipes().forEach(recipes::add);
        return recipes.stream()
                .filter(recipe -> filterFunction.apply(recipe, filterParameter))
                .sorted(Comparator.comparing(Recipe::getDate).reversed())
                .toList();
    }
}
