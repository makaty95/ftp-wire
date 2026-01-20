package UnitTests.Client.Controllers;

import com.makaty.code.Client.Controllers.CommandController;
import com.makaty.code.Client.Models.CommandSender;
import com.makaty.code.Common.Models.Command;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommandControllerTest {



    @Test // Test sending commands
    @DisplayName("Verify sending commands works")
    public void sendCommand_test() {
        CommandSender mock_commandSender = mock(CommandSender.class);
        CommandController commandController = new CommandController(mock_commandSender);

        Command command = new Command("help");

        doAnswer(invocation -> {
            // this is a mock behaviour

            // release the latch (acting like the command reply was received)
            Command sentCommand = invocation.getArgument(0);
            if(sentCommand.getLatch() != null) {
                sentCommand.getLatch().countDown();
            }

            return null;
        }).when(mock_commandSender).sendCommand(any(Command.class));

        commandController.sendCommand(command);

        // check if the method inside command sender was invoked
        verify(mock_commandSender, times(1)).sendCommand(command);
        assertTrue(commandController.getPendingCommands().isEmpty(), "Pending commands should be empty here, after latch is released.");


    }

    @Test
    @DisplayName("Try sending command then checking pendingCommands HashMap")
    public void getPendingCommands_test() {
        CommandSender mock_commandSender = mock(CommandSender.class);
        CommandController commandController = new CommandController(mock_commandSender);

        Command command = new Command("help");

        doAnswer(invocation -> {
            // this is a mock behaviour

            assertEquals(0, commandController.getPendingCommands().size());

            // release the latch (acting like the command reply was received)
            Command sentCommand = invocation.getArgument(0);
            if(sentCommand.getLatch() != null) {
                sentCommand.getLatch().countDown();
            }

            return null;
        }).when(mock_commandSender).sendCommand(any(Command.class));

        commandController.sendCommand(command);
        assertTrue(commandController.getPendingCommands().isEmpty());

    }


    @Test
    @DisplayName("Verify concurrent latch works for command synchronization with cli")
    public void signalResponseReceived_test() {
        CommandController commandController = CommandController.getInstance();

        Command command = new Command("help");
        CountDownLatch latch = new CountDownLatch(1);
        command.setLatch(latch);

        assertEquals(1, latch.getCount());
        commandController.signalResponseReceived(command);
        assertEquals(0, latch.getCount());
    }



    // Test
}
