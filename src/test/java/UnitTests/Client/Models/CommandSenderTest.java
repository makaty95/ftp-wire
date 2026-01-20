package UnitTests.Client.Models;

import com.makaty.code.Client.Models.CommandSender;
import com.makaty.code.Client.Models.ConnectionManager;
import com.makaty.code.Common.Models.Command;
import com.makaty.code.Common.Packets.Communication.CommandPacket;
import com.makaty.code.Common.Packets.IO.PacketWriter;
import com.makaty.code.Common.Types.PacketType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.channels.SocketChannel;

import static org.mockito.Mockito.*;

public class CommandSenderTest {

    @Test
    @DisplayName("try to send a command with connection ON, should proceed")
    void sendCommand$connection_on() throws Exception {
        // Mock Command
        Command command = mock(Command.class);

        // Mock Writer
        PacketWriter writer = mock(PacketWriter.class);


        // Inject mock writer into PacketType.COMMAND
        Field writerField = PacketType.COMMAND.getClass().getDeclaredField("writer");
        writerField.setAccessible(true);
        writerField.set(PacketType.COMMAND, writer); // override internal writer

        // Mock ConnectionManager singleton
        ConnectionManager cm = mock(ConnectionManager.class);
        when(cm.isConnected()).thenReturn(true);
        when(cm.getSessionId()).thenReturn("session1");

        // Mock SocketChannel
        SocketChannel mockChannel = mock(SocketChannel.class);
        when(cm.getCommandSocketChannel()).thenReturn(mockChannel);

        // Replace singleton via reflection
        Field instanceField = ConnectionManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, cm);

        // Act
        CommandSender sender = new CommandSender();
        sender.sendCommand(command);

        // Verify
        verify(writer).write(any(CommandPacket.class), eq(mockChannel));
    }


    @Test
    @DisplayName("try to send a command with no connection, should do nothing and return immediately")
    void sendCommand$connection_off() throws Exception {
        // Mock Command
        Command command = mock(Command.class);

        // Mock Writer
        PacketWriter writer = mock(PacketWriter.class);


        // Inject mock writer into PacketType.COMMAND
        Field writerField = PacketType.COMMAND.getClass().getDeclaredField("writer");
        writerField.setAccessible(true);
        writerField.set(PacketType.COMMAND, writer); // override internal writer

        // Mock ConnectionManager singleton
        ConnectionManager cm = mock(ConnectionManager.class);
        when(cm.isConnected()).thenReturn(false);
        when(cm.getSessionId()).thenReturn(null);
        when(cm.getCommandSocketChannel()).thenReturn(null);

        // Replace singleton via reflection
        Field instanceField = ConnectionManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, cm);

        // Act
        CommandSender sender = new CommandSender();
        sender.sendCommand(command); // will just return when no connection
    }

}
