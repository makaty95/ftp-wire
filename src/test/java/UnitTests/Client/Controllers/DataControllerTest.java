package UnitTests.Client.Controllers;

import com.makaty.code.Client.Controllers.DataController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


public class DataControllerTest {



    @Tag("unit")
    @ParameterizedTest
    @DisplayName("try resolving the proper representation size for some memory chunks")
    @CsvSource({
            "13212909, MB",
            "199990, KB",
            "2893, KB",
            "80, B"
    })
    public void resolveProperUnit_test(
            long file_size,
            String expected_unit
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        DataController dataController = DataController.getInstance();
        Method method = DataController.class.getDeclaredMethod("resolveProperUnit", Long.class);
        method.setAccessible(true);

        String res = (String) method.invoke(dataController, file_size);
        assertEquals(expected_unit, res);
    }


    @Tag("unit")
    @ParameterizedTest
    @DisplayName("try resolve the size based on the unit")
    @CsvSource({
            "13212909, MB, 12.6",
            "199990, KB, 195.3",
            "2893, KB, 2.8",
            "80, B, 80"
    })
    public void resolveProperSize_test(
            long file_size,
            String unit,
            double expected_size
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        DataController dataController = DataController.getInstance();
        Method method = DataController.class.getDeclaredMethod("resolveProperSize", Long.class, String.class);
        method.setAccessible(true);

        double res = (double) method.invoke(dataController, file_size, unit);
        assertTrue(Math.abs(res - expected_size) < 0.1);
    }


}
