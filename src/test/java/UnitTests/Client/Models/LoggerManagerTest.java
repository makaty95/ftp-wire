package UnitTests.Client.Models;

import com.makaty.code.Client.Loggers.ClientLogger;
import com.makaty.code.Client.Models.LoggerManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoggerManagerTest {



    @Test
    @DisplayName("Try logging some messages to a buffer using a logger")
    public void info_test_using_cli() throws IOException {
        // Mocking objects
        LoggerManager loggerManager = LoggerManager.getInstance();
        ClientLogger clientLogger_cli = mock(ClientLogger.class);


        // Redirect System.out to a buffer
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // keep old stdout
        System.setOut(new PrintStream(outContent));

        try {
            // Mocking functions behavior
            doAnswer(invocation -> {
                String msg = invocation.getArgument(0);
                System.out.println(msg);  // simulate console print
                return null;
            }).when(clientLogger_cli).info(anyString());

            // Add cli logger to loggerManager
            loggerManager.addLogger(clientLogger_cli);


            // Simulate info calls
            loggerManager.info("info-message1");
            loggerManager.info("info-message2");

            // Get output as string
            String output = outContent.toString().trim();


            // Check if the messages are printed as expected
            assertTrue(output.contains("info-message1"));
            assertTrue(output.contains("info-message2"));
            assertEquals("info-message1\ninfo-message2", output.replace("\r", ""));
        } finally {
            // Restore System.out
            System.setOut(originalOut);
            outContent.close();
        }



    }

    @Test
    @DisplayName("Try logging some messages to a file using a logger")
    public void info_test_using_file() throws IOException {
        LoggerManager loggerManager = LoggerManager.getInstance();
        ClientLogger clientLogger_file = mock(ClientLogger.class);
        Path tempFile = Files.createTempFile("tst", ".txt");

        BufferedWriter file_writer = Files.newBufferedWriter(tempFile);
        try {
            doAnswer(invocation -> {
                String msg = invocation.getArgument(0);
                // simulate file print
                file_writer.write(msg);
                file_writer.flush();
                return null;
            }).when(clientLogger_file).info(anyString());

            loggerManager.addLogger(clientLogger_file);

            // Simulate info calls
            loggerManager.info("info-message1");
            loggerManager.info("info-message2");

            String fileContent = Files.readString(tempFile);

            assertTrue(fileContent.contains("info-message1"));
            assertTrue(fileContent.contains("info-message2"));
            assertEquals("info-message1info-message2", fileContent);

        } finally {
            file_writer.close();
            Files.deleteIfExists(tempFile);
        }

    }


}
