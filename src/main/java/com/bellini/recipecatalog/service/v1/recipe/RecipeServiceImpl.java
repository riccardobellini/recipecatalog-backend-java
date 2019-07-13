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
import com.bellini.recipecatalog.exception.recipe.NotExistingRecipeException;
import com.bellini.recipecatalog.model.v1.Book;
import com.bellini.recipecatalog.model.v1.DishType;
import com.bellini.recipecatalog.model.v1.Ingredient;
import com.bellini.recipecatalog.model.v1.Publication;
import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.model.v1.dto.recipe.RecipeCreationDTO;

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
        // TODO list of ingredients, dish types, book and publication should be retrieved from method parameter instead of saved object
        final Recipe saved = repo.save(rec);
        final Long recipeId = saved.getId();
        Collection<Ingredient> ingredients = rec.getIngredients();
        for (Ingredient ingr : ingredients) {
            ingredientRepo.attachToRecipe(ingr.getId(), recipeId);
        }
        Collection<DishType> dishtypes = rec.getDishtypes();
        for (DishType dt : dishtypes) {
            dishTypeRepo.attachToRecipe(dt.getId(), recipeId);
        }
        Book book = rec.getBook();
        if (book != null) {
            bookRepo.attachToRecipe(book.getId(), recipeId);
        }
        Publication publication = rec.getPublication();
        if (publication != null) {
            pubRepo.attachToRecipe(publication.getId(), recipeId);
        }
        return saved;
    }

    @Override
    public Recipe get(Long id) {
        final Optional<Recipe> optRec = repo.findById(id);
        if (!optRec.isPresent()) {
            throw new NotExistingRecipeException(id);
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

    @Override
    public Recipe create(RecipeCreationDTO recipeDto) {
        if (recipeDto == null) {
            throw new IllegalArgumentException("Null creation object not allowed");
        }
        Publication pub = null;
        // check if publication is present, retrieve id if found, create publication otherwise
        if (recipeDto.getVolume() != null && recipeDto.getYear() != null) {
            Optional<Publication> pubOpt = pubRepo.findByVolumeAndYear(recipeDto.getVolume(), recipeDto.getYear());
            if (!pubOpt.isPresent()) {
                // create publication
                Publication pubToCreate = new Publication();
                pubToCreate.setVolume(recipeDto.getVolume());
                pubToCreate.setYear(recipeDto.getYear());
                pub = pubRepo.save(pubToCreate);
            } else {
                pub = pubOpt.get();
            }
        }
        Recipe recipeToCreate = new Recipe();
        recipeToCreate.setTitle(recipeDto.getTitle());
        final Recipe saved = repo.save(recipeToCreate);
        final Long recipeId = saved.getId();
        Collection<Long> ingredients = recipeDto.getIngredients();
        for (Long ingrId : ingredients) {
            ingredientRepo.attachToRecipe(ingrId, recipeId);
        }
        Collection<Long> dishtypes = recipeDto.getDishtypes();
        for (Long dtId : dishtypes) {
            dishTypeRepo.attachToRecipe(dtId, recipeId);
        }
        Long bookId = recipeDto.getBook();
        if (bookId != null) {
            bookRepo.attachToRecipe(bookId, recipeId);
        }
        if (pub != null) {
            pubRepo.attachToRecipe(pub.getId(), recipeId);
        }
        return get(recipeId);
    }

}
