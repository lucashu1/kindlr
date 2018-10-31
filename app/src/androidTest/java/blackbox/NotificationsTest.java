package blackbox;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.R;
import com.example.team19.kindlr.Transaction;
import com.example.team19.kindlr.TransactionManager;
import com.example.team19.kindlr.UserManager;
import com.example.team19.kindlr.ViewNotificationsActivity;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.example.team19.kindlr.R.id.username;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NotificationsTest {

    private static UserManager um;
    private static TransactionManager tm;
    private static BookManager bm;
    private static String bookId1;
    private static String bookId2;
    private static String txId1;
    private static String txId2;
    private static ArrayList<Transaction> matches;

    private static String loginInput;
    private static String loginPassword;
    private static String loginInput2;
    private static String loginPassword2;

    @Rule
    public ActivityTestRule<ViewNotificationsActivity> mActivityRule
            = new ActivityTestRule<>(ViewNotificationsActivity.class);

    @BeforeClass
    public static void initManagers() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
        TransactionManager.getTransactionManager().initialize();
    }

    @Before
    public void initialize() {
        um = UserManager.getUserManager();
        tm = TransactionManager.getTransactionManager();
        bm = BookManager.getBookManager();

        loginInput = "andy";
        loginPassword = "asdf";
        loginInput2 = "bhahn";
        loginPassword2 = "benhahn";
    }

    @Test
    public void displayNotificationsOneMatch() {
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.like_button)).perform(click());
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.signout)).perform(click());
        onView(withId(username)).perform(typeText(loginInput2));
        onView(withId(R.id.password)).perform(typeText(loginPassword2));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.notifications)).perform(click());

        ArrayList<Transaction> matches = tm.getAllMatchedTransactionsForUser(um.getCurrentUser().getUsername());

        //assertEquals(matches.get(0).getOtherUserInTransaction().getUsername(), "andy");
    }

    @Test
    public void displayNotificationsNoMatches() {
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.notifications)).perform(click());

        ArrayList<Transaction> matches = tm.getAllMatchedTransactionsForUser(um.getCurrentUser().getUsername());

        //assertTrue(matches.size() == 0);
    }

    @Test
    public void displayNotificationDetails() {
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.like_button)).perform(click());
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.signout)).perform(click());
        onView(withId(username)).perform(typeText(loginInput2));
        onView(withId(R.id.password)).perform(typeText(loginPassword2));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.notifications)).perform(click());
        onView(withId(R.id.view_notification_1)).perform(click());
    }

    @Test
    public void notificationDetailsAreCorrect() {
        onView(withId(username))
                .perform(typeText(loginInput));
        onView(withId(R.id.password)).perform(typeText(loginPassword));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.like_button)).perform(click());
        onView(withId(R.id.profile_button)).perform(click());
        onView(withId(R.id.signout)).perform(click());
        onView(withId(username)).perform(typeText(loginInput2));
        onView(withId(R.id.password)).perform(typeText(loginPassword2));
        onView(withId(R.id.sign_in_button)).perform(click());
        onView(withId(R.id.notifications)).perform(click());
        onView(withId(R.id.view_notification_1)).perform(click());
    }

    @AfterClass
    public static void deleteTestVariables() {
        um.deleteUser("bhahntest");
        um.deleteUser("shahntest");
        bm.deleteBook(bookId1);
        bm.deleteBook(bookId2);
        tm.deleteExchangeTransaction(txId1);
        tm.deleteExchangeTransaction(txId2);
    }


}
