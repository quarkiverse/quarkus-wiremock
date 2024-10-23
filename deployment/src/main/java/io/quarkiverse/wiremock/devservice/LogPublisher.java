package io.quarkiverse.wiremock.devservice;

import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Publish Logs
 */
public class LogPublisher extends SubmissionPublisher<String> {

    public void publishLog(String logMessage) {
        this.submit(logMessage);
    }

    public static class LogHandler extends Handler {
        private final LogPublisher logPublisher;

        public LogHandler(LogPublisher logPublisher) {
            this.logPublisher = logPublisher;
        }

        @Override
        public void publish(LogRecord record) {
            String message = record.getMessage();
            logPublisher.publishLog(message);
        }

        @Override
        public void flush() {
            // No-op
        }

        @Override
        public void close() throws SecurityException {
            // No-op
        }
    }
}