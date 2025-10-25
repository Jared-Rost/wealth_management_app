/**
 * TransactionsListViewActivity.java
 *
 * COMP3350 SECTION A02
 *
 * @author Bao
 * @date Tuesday, February 7, 2024
 *
 * PURPOSE:
 *  Tests related to main category object
 **/

package com.spenditure.objects;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.spenditure.object.MainCategory;

public class MainCategoryTest {

    @Before
    public void setup(){
        System.out.println("Starting test for Category");
    }

    @Test
    public void testCreateCategory(){
        System.out.println("Starting create Category");
        MainCategory category = new MainCategory("tuition fee",1,1);
        assertNotNull(category);
        assertEquals("tuition fee",category.getName());
        assertEquals(1,category.getCategoryID());

        System.out.println("Finished testCreateCategory");
    }
}
