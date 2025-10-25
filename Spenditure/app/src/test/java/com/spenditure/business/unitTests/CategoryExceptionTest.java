package com.spenditure.business.unitTests;

import org.junit.Test;
import static org.junit.Assert.*;

import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.exceptions.InvalidCategoryException;
import com.spenditure.logic.exceptions.InvalidSubCategoryException;
import com.spenditure.object.MainCategory;

/**
 * Exception unit tests
 * @author Bao Ngo
 * @version 06 Feb 2024
 */
public class CategoryExceptionTest {
    @Test
    public void testInvalidCategory(){
        CategoryHandler categoryHandler = new CategoryHandler(true);

        MainCategory food = new MainCategory("Food", 1, 1);

        //Get non-exist ID category
        boolean caught = false;
        try {
            categoryHandler.getCategoryByID(99);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Add category already exists in DB
        caught = false;
        try {
            categoryHandler.addCategory("Food",1);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Add null category already exists in DB
        caught = false;
        try {
            categoryHandler.addCategory(null,1);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Add blank category already exists in DB
        caught = false;
        try {
            categoryHandler.addCategory("",1);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Delete non-exist category
        caught = false;
        try {
            categoryHandler.deleteCategory(99);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Add sub-category to category not exist
        caught = false;
        try {
            categoryHandler.addSubCategory(99,"sub test");
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Delete sub-category to category not exist
        caught = false;
        try {
            categoryHandler.deleteSubCategory(99,1);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Get all sub-categories to category not exist
        caught = false;
        try {
            categoryHandler.getAllSubCategoriesFromParent(99);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Get sub-category to category not exist
        caught = false;
        try {
            categoryHandler.getSubCategoryFromParent(99,1);
        }catch (InvalidCategoryException e){
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testInvalidSubCategory(){
        boolean caught;
        CategoryHandler categoryHandler = new CategoryHandler(true);

        //Add sub-category that already exists
        caught = false;
        try {
            categoryHandler.addSubCategory(1,"sub test");
            categoryHandler.addSubCategory(1,"sub test");
        }catch (InvalidSubCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Add null sub-category that already exists
        caught = false;
        try {
            categoryHandler.addSubCategory(1,null);
        }catch (InvalidSubCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Add blank sub-category that already exists
        caught = false;
        try {
            categoryHandler.addSubCategory(1,"");
        }catch (InvalidSubCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Add more than 5 sub-category
        caught = false;
        try {
            categoryHandler.addSubCategory(3,"Friend");
            categoryHandler.addSubCategory(3,"Family");
            categoryHandler.addSubCategory(3,"Date");
            categoryHandler.addSubCategory(3,"Celebration");
            categoryHandler.addSubCategory(3,"Travelling");
            categoryHandler.addSubCategory(3,"Harry owns a dinner");
        }catch (InvalidSubCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Delete sub-category not exists
        caught = false;
        try {
            categoryHandler.deleteSubCategory(1,22);
        }catch (InvalidSubCategoryException e){
            caught = true;
        }
        assertTrue(caught);

        //Get sub-category not exist
        caught = false;
        try {
            categoryHandler.getSubCategoryFromParent(1,99);
        }catch (InvalidSubCategoryException e){
            caught = true;
        }
        assertTrue(caught);

    }
}
