package UnitTests.Client.ReplyHandlers;

import com.makaty.code.Client.Controllers.DataController;
import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.Client.Models.Reply;
import com.makaty.code.Client.ReplyHandlers.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;


import static org.mockito.Mockito.*;

public class AllReplyHandlersTest {

    @Nested
    class CWD_ReplyHandlerTest {

        @ParameterizedTest
        @CsvSource({
                "relative/path1",
                "rel/path2",
                "a/b/xyz"
        })
        @DisplayName("try handling CWD replies")
        void handle_test(String relativePath) throws NoSuchFieldException, IllegalAccessException {

            // Mock a reply instance
            Reply reply = mock(Reply.class);
            when(reply.getStrings()).thenReturn(List.of(relativePath, "dummyText"));

            // Mock a connection manager instance
            ConnectionManager connectionManager = mock(ConnectionManager.class);

            // Reflect the current Conn.Manager on the singleton object
            Field instanceField = ConnectionManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, connectionManager);


            CWD_ReplyHandler handler = new CWD_ReplyHandler();
            handler.handle(reply);

            // verify the call
            verify(connectionManager).setWorkingDir(relativePath);

        }

    }

    @Nested
    class FILE_INFO_ReplyHandler_test {

        @ParameterizedTest
        @CsvSource({
                "file1.txt, 1024",
                "image.png, 2048",
                "document.pdf, 5120",
                "arch.zip, 5164000"
        })
        @DisplayName("try handling FILE_INFO replies")
        void handle_test(String fileName, long fileSize) throws IOException, NoSuchFieldException, IllegalAccessException {
            // Mock Reply
            Reply reply = mock(Reply.class);
            when(reply.getStrings()).thenReturn(List.of(fileName));
            when(reply.getLongs()).thenReturn(List.of(fileSize));

            // Mock DataController
            DataController dataController = mock(DataController.class);
            // Replace singleton instance via reflection
            Field instanceField = DataController.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, dataController);

            FILE_INFO_ReplyHandler handler = new FILE_INFO_ReplyHandler();
            handler.handle(reply);

            // Verify that receiveFile was called with correct parameters
            verify(dataController).receiveFile(fileName, "./storage/", fileSize);
        }
    }


    @Nested
    class MESSAGE_ReplyHandlerTest {

        @ParameterizedTest
        @ValueSource(strings = {
                "Hello World",
                "Test Message",
                "Another message"
        })
        @DisplayName("try handling MESSAGE replies")
        void handle_test(String message) throws Exception {
            // Mock Reply
            Reply reply = mock(Reply.class);
            when(reply.getStrings()).thenReturn(List.of(message));

            // Mock LoggerManager
            LoggerManager logger = mock(LoggerManager.class);
            Field instanceField = LoggerManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, logger);

            MESSAGE_ReplyHandler handler = new MESSAGE_ReplyHandler();
            handler.handle(reply);

            verify(logger).RemoteInfo(message);
        }
    }


    @Nested
    class PWD_ReplyHandlerTest {

        @ParameterizedTest
        @CsvSource({
                "relative/path1, /absolute/path1",
                "folder/subfolder, /abs/folder/subfolder"
        })
        @DisplayName("try handling PWD replies")
        void handle_test(String relativePath, String absolutePath) throws Exception {
            Reply reply = mock(Reply.class);
            when(reply.getStrings()).thenReturn(List.of(relativePath, absolutePath));

            LoggerManager logger = mock(LoggerManager.class);
            Field instanceField = LoggerManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, logger);

            PWD_ReplyHandler handler = new PWD_ReplyHandler();
            handler.handle(reply);

            verify(logger).RemoteInfo(relativePath);
        }
    }

    @Nested
    class QUIT_AFFIRM_ReplyHandlerTest {

        @ParameterizedTest
        @ValueSource(strings = {"session1", "session2"})
        @DisplayName("try handling QUIT_AFFIRM replies")
        void handle_test(String sessionId) throws Exception {
            Reply reply = mock(Reply.class);
            when(reply.getStrings()).thenReturn(List.of(sessionId));

            ConnectionManager connectionManager = mock(ConnectionManager.class);
            when(connectionManager.isConnected()).thenReturn(true);

            Field instanceField = ConnectionManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, connectionManager);

            QUIT_AFFIRM_ReplyHandler handler = new QUIT_AFFIRM_ReplyHandler();
            handler.handle(reply);

            verify(connectionManager).terminateConnection();
        }
    }
}
