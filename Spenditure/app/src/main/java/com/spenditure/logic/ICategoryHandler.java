package com.spenditure.logic;

import com.spenditure.logic.exceptions.InvalidCategoryException;
import com.spenditure.object.MainCategory;
import com.spenditure.object.SubCategory;

import java.util.List;

public interface ICategoryHandler {

    /*
        Clean up methods to get new category stub database for the shake of testing
     */
    public void cleanup(boolean getSubDB);

    /*
       getAllCategory
       Returns a list of all Categories.
    */
    public List<MainCategory> getAllCategory(int userID);

    /*
        getCategoryByID
        Given an ID, returns the corresponding category.
     */
    public MainCategory getCategoryByID(int id) throws InvalidCategoryException;

    /*
        addCategory
        Given the information for a new Category, creates it, validates it, then
        sends it to the DB to be added.
     */
    public MainCategory addCategory(String newCategoryName,int userID);

    /*
        deleteCategory
        Given the ID for a Category, deletes the corresponding category.
     */
    public void deleteCategory(int id);


    //SubCategory functions are for potential future implementation of subcategories

    /*
        addSubCategory
        Given the information for a new SubCategory, creates it, validates it, then
        sends it to the DB to be added.
     */
    public SubCategory addSubCategory(int parentCategoryID, String newSubCategory);

    /*
        Given the ID for a SubCategory, deletes the corresponding sub-category.
     */
    public void deleteSubCategory(int parentCategoryID,int subCategoryID);

    /*
        getAllSubCategoriesFromParent
        Given a parent category, returns all corresponding SubCategories.
     */
    public List<SubCategory> getAllSubCategoriesFromParent(int parentCategoryID);

    /*
        getSubCategoryFromParent
        Given an ID for both a parent and sub-Category, returns the corresponding subcategory.
     */
    public SubCategory getSubCategoryFromParent(int parentCategoryID,int subCategoryID);
}
