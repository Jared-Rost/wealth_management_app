package com.spenditure.business.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.spenditure.logic.CategoryHandler;
import com.spenditure.object.MainCategory;
import com.spenditure.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CategoryHandlerIT {

    private CategoryHandler categoryHandler;

    private File tempDB;

    @Before
    public void setup() throws IOException {
        this.tempDB = TestUtils.copyDB();
        this.categoryHandler = new CategoryHandler(false);
        categoryHandler.cleanup(false);
    }
    @After
    public void tearDown(){
        this.categoryHandler = null;
        this.tempDB = null;
    }

    @Test
    public void testCategorySet() {
        int expectedSize = 3;

        List<MainCategory> categoryList = categoryHandler.getAllCategory(1);
        assertEquals(expectedSize, categoryList.size());

        MainCategory category1 = categoryHandler.getCategoryByID(1);
        MainCategory category2 = categoryHandler.getCategoryByID(2);
        MainCategory category3 = categoryHandler.getCategoryByID(3);

        assertEquals("Grocery", category1.getName());
        assertEquals("Food", category2.getName());
        assertEquals("Hang out", category3.getName());
    }



    @Test
    public void testInsert(){
        int expectedSize = 3;
        int userID = 1;
//        MainCategory bills = new MainCategory("bills", 99, 1);
        assertEquals(expectedSize, categoryHandler.getAllCategory(1).size());


        categoryHandler.addCategory("bills",  userID);
        assertEquals(expectedSize + 1,categoryHandler.getAllCategory(userID).size());
        assertEquals("Grocery",categoryHandler.getCategoryByID(userID).getName());
        assertEquals("bills",categoryHandler.getCategoryByID(4).getName());
    }

    @Test
    public void testDelete(){
        int expectedSize = 3;

        assertEquals(categoryHandler.getAllCategory(1).size(),expectedSize);
        assertEquals("Grocery",categoryHandler.getCategoryByID(1).getName());
        assertEquals("Food",categoryHandler.getCategoryByID(2).getName());
        assertEquals("Hang out",categoryHandler.getCategoryByID(3).getName());

        categoryHandler.deleteCategory(1);

        int count = categoryHandler.getAllCategory(1).size();
        //assertEquals(2 ,count);
        assertEquals("Food",categoryHandler.getCategoryByID(2).getName());
        assertEquals("Hang out",categoryHandler.getCategoryByID(3).getName());
    }

}
