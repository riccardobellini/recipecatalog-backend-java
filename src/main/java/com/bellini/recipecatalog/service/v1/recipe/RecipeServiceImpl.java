package com.bellini.recipecatalog.service.v1.recipe;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bellini.recipecatalog.dao.v1.book.BookRepository;
import com.bellini.recipecatalog.dao.v1.dishtype.DishTypeRepository;
import com.bellini.recipecatalog.dao.v1.ingredient.IngredientRepository;
import com.bellini.recipecatalog.dao.v1.publication.PublicationRepository;
import com.bellini.recipecatalog.dao.v1.recipe.RecipeRepository;
import com.bellini.recipecatalog.exception.ingredient.NotExistingIngredientException;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.model.v1.Publication;
import com.bellini.recipecatalog.model.v1.Recipe;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository repo;

    @Autowired
    private DishTypeRepository dishTypeRepo;

    @Autowired
    private IngredientRepository ingredientRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private PublicationRepository pubRepo;

    @Override
    public Recipe create(Recipe rec) {
        // TODO performs check
        final Recipe saved = repo.save(rec);
        final Long recipeId = saved.getId();
        Collection<Ingredient> ingredients = saved.getIngredients();
        for (Ingredient ingr : ingredients) {
            ingredientRepo.attachToRecipe(ingr.getId(), recipeId);
        }
        Collection<DishType> dishtypes = saved.getDishtypes();
        for (DishType dt : dishtypes) {
            dishTypeRepo.attachToRecipe(dt.getId(), recipeId);
        }
        Book book = saved.getBook();
        if (book != null) {
            bookRepo.attachToRecipe(book.getId(), recipeId);
        }
        Publication publication = saved.getPublication();
        if (publication != null) {
            pubRepo.attachToRecipe(publication.getId(), recipeId);
        }
        return saved;
    }

    @Override
    public Recipe get(Long id) {
        final Optional<Recipe> optRec = repo.findById(id);
        if (!optRec.isPresent()) {
            throw new NotExistingIngredientException(id); // FIXME change exception
        }
        final Recipe rec = optRec.get();
        rec.setDishtypes(dishTypeRepo.findByRecipeId(rec.getId()));
        rec.setIngredients(ingredientRepo.findByRecipeId(rec.getId()));
        Optional<Book> optBook = bookRepo.findByRecipeId(rec.getId());
        if (optBook.isPresent()) {
            rec.setBook(optBook.get());
        }
        Optional<Publication> optPub = pubRepo.findByRecipeId(rec.getId());
        if (optPub.isPresent()) {
            rec.setPublication(optPub.get());
        }
        return optRec.get();
    }

    @Override
    public Page<Recipe> getAll(Pageable page) {
        final Page<Recipe> result = repo.findAll(page);
        for (Recipe rec : result) {
            rec.setDishtypes(dishTypeRepo.findByRecipeId(rec.getId()));
            rec.setIngredients(ingredientRepo.findByRecipeId(rec.getId()));
            Optional<Book> optBook = bookRepo.findByRecipeId(rec.getId());
            if (optBook.isPresent()) {
                rec.setBook(optBook.get());
            }
            Optional<Publication> optPub = pubRepo.findByRecipeId(rec.getId());
            if (optPub.isPresent()) {
                rec.setPublication(optPub.get());
            }
        }
        return result;
    }

}
