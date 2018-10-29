package whitebox;

import com.example.team19.kindlr.BookManager;
import com.example.team19.kindlr.TransactionManager;
import com.example.team19.kindlr.UserManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class TransactionManagerUnitTests {

    UserManager um = UserManager.getUserManager();
    BookManager bm = BookManager.getBookManager();
    TransactionManager tm = TransactionManager.getTransactionManager();

    @BeforeClass
    public void initManagers() {
        UserManager.getUserManager().initialize();
        BookManager.getBookManager().initialize();
        TransactionManager.getTransactionManager().initialize();
    }

    @BeforeClass
    public void createTestUsers() {
        um.addUser("testUser1", "", "Mr", "Test", "Los Angeles", "California", "3333333333", "test1@usc.edu");
        um.addUser("testUser2", "", "Mrs", "Test", "Los Angeles", "California", "4444444444", "test2@usc.edu");
    }

    @AfterClass
    public void deleteTestUsers() {
        um.deleteUser("testUser1");
        um.deleteUser("testUser2");
    }
}
