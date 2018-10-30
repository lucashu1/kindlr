package blackbox;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.team19.kindlr.LoginActivity;
import com.example.team19.kindlr.R;
import com.example.team19.kindlr.UserManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.team19.kindlr.R.id.username;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignOutTest {

    private String loginInput;
    private String loginPassword;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {

        UserManager.getUserManager().addUser("andy", "asdf", "andrew", "szot", "la", "ca", "77777777", "u@me.com");
        // Specify a valid string.
        loginInput = "andy";
        loginPassword = "asdf";
    }

    @Test
    public void testSignOut() {
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withId(R.id.profile_button)).perform(click());

        onView(withId(R.id.signout)).perform(click());






    }

    @After
    public void cleanUp(){
        UserManager.getUserManager().deleteUser(loginInput);
    }
}
