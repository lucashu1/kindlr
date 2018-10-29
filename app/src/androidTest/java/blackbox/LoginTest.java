package blackbox;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.team19.kindlr.LoginActivity;
import com.example.team19.kindlr.R;
import com.example.team19.kindlr.User;
import com.example.team19.kindlr.UserManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.team19.kindlr.R.id.username;
import static org.junit.Assert.assertEquals;

//import android.support.test.espresso.ViewAction.typeText;
//import android.support.test.espresso.action.ViewActions.typeText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private String loginInput;
    private String loginPassword;
    private String badLoginInput;
    private String badLoginPassword;
    private String wrongPassLoginInput;
    private String wrongPassword;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        loginInput = "andy";
        loginPassword = "asdf";
        badLoginInput = "bogus";
        badLoginPassword = "bogus";
        wrongPassLoginInput = "andy";
        wrongPassword = "1234";
    }

    @Test
    public void testValidLogin(){
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());

        User currUser = UserManager.getUserManager().getCurrentUser();
        String username = currUser.getUsername();
        String password = currUser.getHashedPassword();

        assertEquals(username, loginInput);

    }

    @Test
    public void testInvalidLogin(){
        onView(withId(username))
                .perform(typeText(badLoginInput));
        onView(withId(R.id.password)).perform(typeText(badLoginPassword));

        onView(withId(R.id.sign_in_button)).perform(click());

        int userCreated = 1;
        try{
            User currUser = UserManager.getUserManager().getCurrentUser();
            if(!(currUser.getUsername().equals(badLoginInput))){
                userCreated = 0;
            }
        }
        catch(NullPointerException e){

            userCreated = 0;
        }
        assertEquals(0, userCreated);


    }

    @Test
    public void testWrongPassword(){

        onView(withId(username))
                .perform(typeText(wrongPassLoginInput));
        onView(withId(R.id.password)).perform(typeText(wrongPassword));
        onView(withId(R.id.sign_in_button)).perform(click());

        int userCreated = 1;
        try{
            User currUser = UserManager.getUserManager().getCurrentUser();
            if(!(currUser.getUsername().equals(badLoginInput))){
                userCreated = 0;
            }
        }
        catch(NullPointerException e){

            userCreated = 0;
        }
        assertEquals(0, userCreated);

    }




}
