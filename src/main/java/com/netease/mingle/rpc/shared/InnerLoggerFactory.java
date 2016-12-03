package com.netease.mingle.rpc.shared;


import java.util.logging.*;

/**
 * Inner Logger Factory
 * Created by Michael Jiang on 2016/12/3.
 */
public class InnerLoggerFactory {
    private final static ConsoleHandler consoleHandler = new ConsoleHandler();

    static {
        consoleHandler.setFormatter(new SimpleFormatter());
    }

    public static Logger getLogger(String loggerName) {
        Logger logger = Logger.getLogger(loggerName);
        logger.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);
        return logger;
    }
}
