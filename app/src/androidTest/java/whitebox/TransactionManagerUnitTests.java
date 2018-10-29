package whitebox;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.TransactionManager;
import com.example.team19.kindlr.UserManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class TransactionManagerUnitTests {

    @BeforeClass
    public static void initManagers() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
        TransactionManager.getTransactionManager().initialize();

        UserManager.getUserManager().addUser("testUser1", "", "Mr", "Test", "Los Angeles", "California", "3333333333", "test1@usc.edu");
        UserManager.getUserManager().addUser("testUser2", "", "Mrs", "Test", "Los Angeles", "California", "4444444444", "test2@usc.edu");

    }

    @AfterClass
    public static void deleteTestUsers() {
        UserManager.getUserManager().deleteUser("testUser1");
        UserManager.getUserManager().deleteUser("testUser2");
    }
}
