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
import com.bellini.recipecatalog.model.v1.Publication;
import com.bellini.recipecatalog.model.v1.Recipe;
import com.bellini.recipecatalog.model.v1.RecipeSearchCriteria;
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
        // TODO add month
        Publication pub = null;
        if (recipeDto.getVolume() != null || recipeDto.getYear() != null) {
            pub = new Publication();
            pub.setVolume(recipeDto.getVolume());
            pub.setYear(recipeDto.getYear());
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
            pubRepo.attachToRecipe(recipeId, pub);
        }
        return get(recipeId);
    }

    @Override
    public Page<Recipe> get(String title, Pageable pageable) {
        final Page<Recipe> result = repo.findByTitleIgnoreCaseContaining(title, pageable);
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
    public Page<Recipe> search(RecipeSearchCriteria searchCriteria, Pageable page) {
        if (searchCriteria == null) {
            return getAll(page);
        }
        return repo.search(searchCriteria, page);
    }

    @Override
    public int getCount() {
        return repo.getCount();
    }

}
