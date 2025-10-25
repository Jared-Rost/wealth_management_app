package com.spenditure.object;

import com.spenditure.logic.exceptions.InvalidSubCategoryException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Main Category class
 * @author Bao Ngo
 * @version 06 Feb 2024
 */
public class MainCategory  {
    //Attributes
    private String name;
    private int categoryID;
    private int userID;
    private List<SubCategory> listSubCategories; //For future development
    private final int MAX_SUB_CATEGORIES = 5;
    private static int currentCategoryID = 1;
    private static int currentUserID = 1;

    //Constructors
    public MainCategory(String name, int categoryID, int userID){
        this.name = name;
        this.categoryID = categoryID;
        this.userID = userID;
        this.listSubCategories = new ArrayList<>();
    }

    //Business methods
    public String getName() {
        return this.name;
    }
    public int getCategoryID() {
        return this.categoryID;
    }
    public int getUserID() { return this.userID;}

    public List<SubCategory> getSubCategories() { //For future development
        return this.listSubCategories;
    }

    public SubCategory addSubCategory(String newSubCategoryName) throws InvalidSubCategoryException {
        if(this.listSubCategories.size() >= 5){
            throw new InvalidSubCategoryException("Only " + this.MAX_SUB_CATEGORIES + " sub-categories are allowed per Category");
        }else{
            if (checkUniqueSub(newSubCategoryName)){
                SubCategory newSubCategory = new SubCategory(newSubCategoryName, generateUniqueCategoryID());
                this.listSubCategories.add(newSubCategory);
                return newSubCategory;
            }else{
                throw new InvalidSubCategoryException(newSubCategoryName + " sub-category already exists");
            }
        }
    }

    public void removeSubCategory(int id) throws InvalidSubCategoryException{
        Iterator<SubCategory> iterator = this.listSubCategories.iterator();
        boolean found = false;
        while (iterator.hasNext() && !found) {
            SubCategory curr = iterator.next();
            if(curr.getCategoryID() == id){
                iterator.remove();
                found = true;
            }
        }
        if(!found) throw new InvalidSubCategoryException("Sub-category's ID not exist");
    }

    public SubCategory getSubCategoryByID(int id) throws InvalidSubCategoryException {
        for(SubCategory currSub : this.listSubCategories){
            if(currSub.getCategoryID() == id) return currSub;
        }
        throw new InvalidSubCategoryException("Sub-category ID not exist");
    }

    /*
     * Support methods
     */
    private boolean checkUniqueSub(String newSubCategoryName){
        for( SubCategory currSubCategory : this.listSubCategories){
            if( currSubCategory.getName().equalsIgnoreCase(newSubCategoryName)) return false;
        }
        return true;
    }

    private static int generateUniqueCategoryID(){
        return currentCategoryID++;
    }

}
