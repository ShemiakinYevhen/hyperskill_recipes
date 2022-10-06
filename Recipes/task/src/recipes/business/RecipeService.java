package recipes.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistence.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe findRecipeById(long id){
        return recipeRepository.findById(id).orElse(new Recipe());
    }

    public boolean existsById(long id) {
        return recipeRepository.existsById(id);
    }

    public Recipe save(Recipe recipeToSave) {
        return recipeRepository.save(recipeToSave);
    }

    public void deleteById(long id) {
        recipeRepository.deleteById(id);
    }

    public Iterable<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }
}
