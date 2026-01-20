package UnitTests.Client.Models;

import com.makaty.code.Client.Models.ConnectionManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnectionManagerTest {

    //TODO: complete this class tests

    @Test
    @DisplayName("Check singleton pattern, should return the same instance")
    public void getInstanceTest() {
        ConnectionManager cm1 = ConnectionManager.getInstance();
        ConnectionManager cm2 = ConnectionManager.getInstance();
        assertEquals(cm1, cm2);
        ConnectionManager cm3 = ConnectionManager.getInstance();
        assertEquals(cm1, cm3);
    }

}
