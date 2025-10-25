package com.spenditure.utility;

import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Description: For testing purose, this supports to retrieve an element at index from
 * ViewPager(UI component)
 * @author Bao Ngo
 * @version 22 Mar 2024
 */
public abstract class ViewPagerSupporter {
    public static Matcher<View> getChildOf(final Matcher<View> parentMatcher, int index) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with first child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {

                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }
                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(index).equals(view);

            }
        };
    }
}
