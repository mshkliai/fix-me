package fixMessages.builder;

import defines.PrefixMessage;
import defines.TypeMessage;
import fixMessages.Message;

public class Builder {
    private static long     numMessage = 0;
    private static Builder  builder = null;

    private Builder() {}

    public static Builder getBuilder() {
        if (builder == null) {
            builder = new Builder();
        }

        return builder;
    }

    public Message getMessage(char type, String idReceiver) {
        Message message = null;

        switch (type) {
            case TypeMessage.acceptConnection:
                message = ConnectionAcceptedMessageBuilder.getBuilder().getBuiltMessage(idReceiver);
                break;
            case TypeMessage.marketConnected:
                message = MarketConnectedMessageBuilder.getBuilder().getBuiltMessage(idReceiver);
                break;
            case TypeMessage.marketDisconnected:
                message = MarketDisconnectedMessageBuilder.getBuilder().getBuiltMessage(idReceiver);
                break;
        }

        this.setDefaultFields("ROUTER", idReceiver, message);
        return message;
    }

    public Message getMessage(char type, String idSender, String idReceiver, String typeProduct, int count, int price) {
        Message message = OrderMessageBuilder.getBuilder().getBuiltMessage(type, typeProduct, count, price);

        this.setDefaultFields(idSender, idReceiver, message);

        return message;
    }

    private void setDefaultFields(String idSender, String idReceiver, Message message) {
        message.setNumMessage(PrefixMessage.num + (++numMessage) + "\1");
        message.setSender(PrefixMessage.sender + idSender + "\1");
        message.setReceiver(PrefixMessage.receiver + idReceiver + "\1");
        message.setLengthMessage(PrefixMessage.length + message.getIntLengthMessage() + "\1");
    }
}