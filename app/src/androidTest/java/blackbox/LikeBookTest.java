package blackbox;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.LoginActivity;
import com.example.team19.kindlr.R;
import com.example.team19.kindlr.UserManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static com.example.team19.kindlr.R.id.username;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LikeBookTest {

    private String loginInput;
    private String loginPassword;
    private String bookID;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {

        UserManager.getUserManager().addUser("andy","asdf","andrew","szot","la","ca","77777777","u@me.com");
        UserManager.getUserManager().addUser("jacob","asdf","jacob","dormuth","la","ca","777888999","jacob@jacob.com");
        loginInput = "andy";
        loginPassword = "asdf";
        List<String> tags = new ArrayList<String>();
        bookID = BookManager.getBookManager().postBookForExchange("Test Book","1222222","Jacob","mystery",500,tags,"jacob");





    }

    @Test
    public void testLikeBook(){

        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.like_button)).perform(closeSoftKeyboard());

        onView(withId(R.id.like_button)).perform(click());



    }

    @Test
    public void testDislikeBook(){
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());

        onView(withId(R.id.dislike_button)).perform(closeSoftKeyboard());
        onView(withId(R.id.dislike_button)).perform(click());





    }

    @After
    public void destroyVars(){
        UserManager.getUserManager().deleteItem("andy");
        UserManager.getUserManager().deleteItem("jacob");
        BookManager.getBookManager().deleteItem("bookID");
    }


}
