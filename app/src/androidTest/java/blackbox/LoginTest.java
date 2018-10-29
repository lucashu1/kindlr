package blackbox;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.team19.kindlr.LoginActivity;
import com.example.team19.kindlr.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.team19.kindlr.R.id.username;
import static android.support.test.espresso.action.ViewActions.click;

//import android.support.test.espresso.ViewAction.typeText;
//import android.support.test.espresso.action.ViewActions.typeText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private String loginInput;
    private String loginPassword;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        loginInput = "asdf";
        loginPassword = "asdf";



    }

    @Test
    public void testLogin(){
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));

        onView(withId(R.id.sign_in_button)).perform(click());

    }




}
