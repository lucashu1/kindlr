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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//import android.support.test.espresso.ViewAction.typeText;
//import android.support.test.espresso.action.ViewActions.typeText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpTest {
    private String testUsername;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    private void toSignUp() {
        onView(withId(R.id.sign_up_button))
                .perform(click());
    }

    @Before
    public void initFillData() {
        // Specify a valid string.
        testUsername = "test_user";
    }

    @Before
    @After
    public void clearTestUser() {
        if (UserManager.getUserManager().doesUserExist(testUsername)) {
            UserManager.getUserManager().deleteUser(testUsername);
        }
    }

    private void testFill(String firstNameStr, String lastNameStr, String usernameStr,
                          String passwordStr, String cityStr, String stateStr, String phoneStr,
                          String emailStr) {

        onView(withId(R.id.first_name))
                .perform(typeText(firstNameStr));
        onView(withId(R.id.last_name))
                .perform(typeText(lastNameStr));
        onView(withId(R.id.username))
                .perform(typeText(usernameStr));
        onView(withId(R.id.password))
                .perform(scrollTo())
                .perform(typeText(passwordStr));
        onView(withId(R.id.city))
                .perform(scrollTo())
                .perform(typeText(cityStr));
        onView(withId(R.id.phone))
                .perform(scrollTo())
                .perform(typeText(phoneStr));
        onView(withId(R.id.email))
                .perform(scrollTo())
                .perform(typeText(emailStr));

        onView(withId(R.id.create_profile))
                .perform(scrollTo())
                .perform(click());
    }

    private boolean checkMainSwipingExists() {
        try {
            onView(withId(R.id.like_button)).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {
            return false;
        }
        catch (NoMatchingViewException e) {
            return false;
        }
        return true;
    }

    @Test
    public void testCorrectCreateUser() {
        toSignUp();
        testFill("TestUserFirst", "TestUserLast",
                testUsername, "thisisapassword", "Redmond",
                "WA", "4254639202", "szot@usc.edu");

        assertTrue(UserManager.getUserManager().doesUserExist(testUsername));

        assertTrue(checkMainSwipingExists());

        UserManager.getUserManager().deleteUser(testUsername);
    }

    @Test
    public void testCreateExistingUser() {
        UserManager.getUserManager().addUser(testUsername, "thisisapassword",
                "John", "David", "Redmond", "WA",
                "4254639202", "szot@usc.edu");

        toSignUp();
        testFill("TestUserFirst", "TestUserLast",
                testUsername, "thisisapassword", "Redmond",
                "WA", "4254639202", "szot@usc.edu");

        assertFalse(checkMainSwipingExists());
        UserManager.getUserManager().deleteUser(testUsername);
    }

    @Test
    public void testCreatePartiallyFilledOut() {
        toSignUp();
        testFill("TestUserFirst", "TestUserLast",
                testUsername, "", "Redmond",
                "WA", "4254639202", "szot@usc.edu");

        assertFalse(checkMainSwipingExists());
    }
}
