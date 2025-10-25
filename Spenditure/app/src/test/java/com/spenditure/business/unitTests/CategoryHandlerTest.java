package com.spenditure.business.unitTests;

import com.spenditure.logic.CategoryHandler;
import com.spenditure.object.MainCategory;
import com.spenditure.object.SubCategory;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

/**
 * Category handler unit tests
 * @author Bao Ngo
 * @version 06 Feb 2024
 */

public class CategoryHandlerTest {
    private CategoryHandler categoryHandler;
    @Before
    public void setup(){
        this.categoryHandler = new CategoryHandler(true);
        categoryHandler.cleanup(true);
    }
    @After
    public void tearDown(){
        this.categoryHandler = null;
    }

    @Test
    public void testCategorySet(){
        int expectedSize = 3;

        assertEquals(categoryHandler.getAllCategory(1).size(),expectedSize);
        assertEquals("Grocery",categoryHandler.getCategoryByID(1).getName());
        assertEquals("Food",categoryHandler.getCategoryByID(2).getName());
        assertEquals("Hang out",categoryHandler.getCategoryByID(3).getName());
    }

    @Test
    public void testInsert(){
        int expectedSize = 3;
//        MainCategory bills = new MainCategory("bills", 99, 1);
        assertEquals(categoryHandler.getAllCategory(1).size(),expectedSize);


        categoryHandler.addCategory("bills",  1);
        assertEquals(expectedSize + 1,categoryHandler.getAllCategory(1).size());
        assertEquals("Grocery",categoryHandler.getCategoryByID(1).getName());
//        assertEquals("bills",categoryHandler.getCategoryByID(1).getName());
    }

    @Test
    public void testDelete(){
        int expectedSize = 3;

        assertEquals(categoryHandler.getAllCategory(1).size(),expectedSize);
        assertEquals("Grocery",categoryHandler.getCategoryByID(1).getName());
        assertEquals("Food",categoryHandler.getCategoryByID(2).getName());
        assertEquals("Hang out",categoryHandler.getCategoryByID(3).getName());

        categoryHandler.deleteCategory(1);

        assertEquals(expectedSize - 1 ,categoryHandler.getAllCategory(1).size());
        assertEquals("Food",categoryHandler.getCategoryByID(2).getName());
        assertEquals("Hang out",categoryHandler.getCategoryByID(3).getName());
    }

    @Test
    public void testAddSubCategory(){
        assertEquals(0,categoryHandler.getAllSubCategoriesFromParent(1).size());
        SubCategory newSubCategoryKid =categoryHandler.addSubCategory(1,"For kids");
        assertEquals(1,categoryHandler.getAllSubCategoriesFromParent(1).size());
        SubCategory newSubFromListKid = categoryHandler.getSubCategoryFromParent(1,newSubCategoryKid.getCategoryID());
        assertNotNull(newSubFromListKid);
        assertEquals(newSubCategoryKid.getCategoryID(),newSubFromListKid.getCategoryID());
        assertEquals(newSubCategoryKid.getName(),newSubFromListKid.getName());

        SubCategory newSubCategoryAdult =categoryHandler.addSubCategory(1,"For adult");
        assertEquals(2,categoryHandler.getAllSubCategoriesFromParent(1).size());
        SubCategory newSubFromListAdult = categoryHandler.getSubCategoryFromParent(1,newSubCategoryAdult.getCategoryID());
        assertNotNull(newSubFromListKid);
        assertEquals(newSubCategoryAdult.getCategoryID(),newSubFromListAdult.getCategoryID());
        assertEquals(newSubCategoryAdult.getName(),newSubFromListAdult.getName());

        SubCategory newSubCategoryLunch = categoryHandler.addSubCategory(2,"For lunch");
        assertEquals(1,categoryHandler.getAllSubCategoriesFromParent(2).size());
        SubCategory newSubFromListLunch = categoryHandler.getSubCategoryFromParent(2,newSubCategoryLunch.getCategoryID());
        assertNotNull(newSubFromListLunch);
        assertEquals(newSubCategoryLunch.getCategoryID(),newSubFromListLunch.getCategoryID());
        assertEquals(newSubCategoryLunch.getName(),newSubFromListLunch.getName());
    }

    @Test
    public void testDeleteSubCategory(){
        assertEquals(0,categoryHandler.getAllSubCategoriesFromParent(1).size());
        SubCategory newSubCategoryKid =categoryHandler.addSubCategory(1,"For kids");
        SubCategory newSubCategoryAdult =categoryHandler.addSubCategory(1,"For adult");
        assertEquals(2,categoryHandler.getAllSubCategoriesFromParent(1).size());

        categoryHandler.deleteSubCategory(1,newSubCategoryKid.getCategoryID());
        assertEquals(1,categoryHandler.getAllSubCategoriesFromParent(1).size());
        SubCategory newSubFromListAdult = categoryHandler.getSubCategoryFromParent(1,newSubCategoryAdult.getCategoryID());
        assertEquals(newSubCategoryAdult.getCategoryID(),newSubFromListAdult.getCategoryID());
        assertEquals(newSubCategoryAdult.getName(),newSubFromListAdult.getName());
    }

}
