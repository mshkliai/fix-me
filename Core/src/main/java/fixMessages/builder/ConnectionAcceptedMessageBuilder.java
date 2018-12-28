package fixMessages.builder;

import defines.PrefixMessage;
import fixMessages.NewsMessage;
import fixMessages.Message;

public class ConnectionAcceptedMessageBuilder {
    private static ConnectionAcceptedMessageBuilder builder = null;

    private ConnectionAcceptedMessageBuilder() {}

    public static ConnectionAcceptedMessageBuilder getBuilder() {
        if (builder == null) {
            builder = new ConnectionAcceptedMessageBuilder();
        }

        return builder;
    }

    public Message getBuiltMessage(String idReceiver) {
        NewsMessage message = new NewsMessage();

        message.setTypeMessage(PrefixMessage.type + "1\1");
        message.setBodyMessage(PrefixMessage.body + idReceiver + "\1");

        return message;
    }
}