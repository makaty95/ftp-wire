package com.makaty.Client.LoggersTest;

import com.makaty.code.Client.Models.LoggerManager;
import com.makaty.code.clientCLI.ClientCLILogger;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CLILoggerTest {

    @Test
    public void tes_cli_logger() throws IOException, InterruptedException {
        Terminal terminal = TerminalBuilder.builder().jna(false).jansi(true).system(true).build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
        ClientCLILogger logger = new ClientCLILogger(reader, terminal);

        double size = 29009;
        double loadedBytes = 0;
        for(int i = 0; i<10; i++) {
            Thread.sleep(300);
            loadedBytes += 190;
            double progress = (100.0 * loadedBytes) / size;
            logger.fileProgress(progress);
        }
    }
}
