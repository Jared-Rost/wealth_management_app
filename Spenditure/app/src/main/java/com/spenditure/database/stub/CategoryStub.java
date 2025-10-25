package com.spenditure.database.stub;

import com.spenditure.database.CategoryPersistence;
import com.spenditure.logic.exceptions.InvalidCategoryException;
import com.spenditure.object.MainCategory;

import java.util.*;

/**
 * Category stub database
 * @author Bao Ngo
 * @version 06 Feb 2024
 * PURPOSE: Stub database store list of category. For testing purpose, there are three Category are initialized and added
 * to list in the constructor.
 */

public class CategoryStub implements CategoryPersistence {
    //Attributes
    private List<MainCategory> categoryList;
    private int currentCategoryID = 1;


    //Constructors
    public CategoryStub(){
        this.categoryList = new ArrayList<>();
        this.categoryList.add(new MainCategory("Grocery",generateUniqueCategoryID(),1));
        this.categoryList.add(new MainCategory("Food",generateUniqueCategoryID(),1));
        this.categoryList.add(new MainCategory("Hang out",generateUniqueCategoryID(),1));
    }

    //Business methods
    @Override
    public List<MainCategory> getAllCategory(int userID) {
        List<MainCategory> categories = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            if(categoryList.get(i).getUserID() == userID) {
                categories.add(categoryList.get(i));
            }
        }
        return categories;
    }

    @Override
    public MainCategory addCategory(String newCategoryName,int userID) throws InvalidCategoryException{
        if (validateUnique(newCategoryName)){
            MainCategory newCategory = new MainCategory(newCategoryName,generateUniqueCategoryID(),userID);
            this.categoryList.add(newCategory);
            return newCategory;
        }else{
            throw new InvalidCategoryException("Category's ID not exist");
        }


    }

    @Override
    public void deleteCategoryByID(int categoryID) throws InvalidCategoryException {
        Iterator<MainCategory> iterator = this.categoryList.iterator();
        boolean found = false;
        while (iterator.hasNext() && !found) {
            MainCategory curr = iterator.next();
            if(curr.getCategoryID() == categoryID){
                iterator.remove();
                found = true;
            }
        }
        if(!found) throw new InvalidCategoryException("Category ID: " + categoryID + " does not exist.");
    }

    @Override
    public MainCategory getCategoryByID(int categoryID) throws InvalidCategoryException {
        for(MainCategory category : this.categoryList){
            if(category.getCategoryID() == categoryID){
                return  category;
            }
        }
        throw new InvalidCategoryException("Category ID: "+ categoryID + " does not exist.");
    }

    //Support methods
    private  int generateUniqueCategoryID(){
        return currentCategoryID++;
    }


    private boolean validateUnique(String categoryName){
        for(MainCategory currCategory : this.categoryList){
            if (categoryName.equalsIgnoreCase(currCategory.getName()))
                return false;
        }
        return true;
    }

}
