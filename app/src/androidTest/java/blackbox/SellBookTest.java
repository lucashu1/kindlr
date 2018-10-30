package blackbox;


import android.support.test.espresso.NoMatchingViewException;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.team19.kindlr.LoginActivity;
import com.example.team19.kindlr.R;
import com.example.team19.kindlr.UserManager;

import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.team19.kindlr.R.id.username;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SellBookTest {

    private String loginInput;
    private String loginPassword;
    private String testTitle;
    private String testIsbn;
    private String testAuthor;
    private String testGenre;
    private String testPageCount;



    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {

        UserManager.getUserManager().addUser("andy","asdf","andrew","szot","la","ca","77777777","u@me.com");
        // Specify a valid string.
        loginInput = "andy";
        loginPassword = "asdf";
        testTitle = "Test Title";
        testIsbn = "978-3-16-148410-0";
        testAuthor = "Test Author";
        testGenre = "Test Genre";
        testPageCount = "500";
    }

    @Test
    public void testSellBook(){
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withId(R.id.profile_button)).perform(click());

        onView(withId(R.id.sell_book_button)).perform(click());

        onView(withId(R.id.title)).perform(typeText(testTitle));

        onView(withId(R.id.isbn)).perform(typeText(testIsbn));
        onView(withId(R.id.author)).perform(typeText(testAuthor));
        onView(withId(R.id.genre)).perform(typeText(testGenre));
        onView(withId(R.id.pagecount)).perform(typeText(testPageCount));

        onView(withId(R.id.post)).perform(scrollTo(), click());
        assertTrue(checkSellButtonExists());
    }

    @Test
    public void testInvalidSellBook()
    {
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withId(R.id.profile_button)).perform(click());

        onView(withId(R.id.sell_book_button)).perform(click());

        onView(withId(R.id.title)).perform(typeText(testTitle));

        onView(withId(R.id.isbn)).perform(typeText(testIsbn));
        onView(withId(R.id.pagecount)).perform(typeText(testPageCount));

//        onView(withId(R.id.post)).perform(click());
        onView(withId(R.id.post)).perform(scrollTo(), click());
        assertFalse(checkSellButtonExists());
    }

    private boolean checkSellButtonExists() {
        try {
            onView(withId(R.id.sell_book_button)).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {
            return false;
        }
        catch (NoMatchingViewException e) {
            return false;
        }
        return true;
    }

    @After
    public void cleanUp()
    {
        UserManager.getUserManager().deleteUser("andy");
    }



}
