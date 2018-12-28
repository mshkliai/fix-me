package fixMessages.builder;

import defines.PrefixMessage;
import fixMessages.Message;
import fixMessages.NewsMessage;

public class MarketConnectedMessageBuilder {
    private static MarketConnectedMessageBuilder builder = null;

    public static MarketConnectedMessageBuilder getBuilder() {
        if (builder == null) {
            builder = new MarketConnectedMessageBuilder();
        }

        return builder;
    }

    public Message getBuiltMessage(String idReceiver) {
        NewsMessage message = new NewsMessage();

        message.setTypeMessage(PrefixMessage.type + "A\1");
        message.setBodyMessage(PrefixMessage.body + idReceiver + "\1");

        return message;
    }
}