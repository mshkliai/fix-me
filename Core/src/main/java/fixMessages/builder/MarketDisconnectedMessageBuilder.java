package fixMessages.builder;

import defines.PrefixMessage;
import fixMessages.Message;
import fixMessages.NewsMessage;

public class MarketDisconnectedMessageBuilder {
    private static MarketDisconnectedMessageBuilder builder = null;

    public static MarketDisconnectedMessageBuilder getBuilder() {
        if (builder == null) {
            builder = new MarketDisconnectedMessageBuilder();
        }

        return builder;
    }

    public Message getBuiltMessage(String idReceiver) {
        NewsMessage message = new NewsMessage();

        message.setTypeMessage(PrefixMessage.type + "5\1");
        message.setBodyMessage(PrefixMessage.body + idReceiver + "\1");

        return message;
    }
}
