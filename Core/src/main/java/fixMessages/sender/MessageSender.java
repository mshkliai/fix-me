package fixMessages.sender;

import Client.Client;
import defines.PrefixMessage;
import defines.TypeMessage;
import fixMessages.Message;
import fixMessages.OrderMessage;
import fixMessages.builder.Builder;
import io.netty.channel.Channel;

public class MessageSender {
    private static MessageSender  sender = null;
    private final  Builder        builder = Builder.getBuilder();

    private MessageSender() {}

    public static MessageSender getSender() {
        if (sender == null) {
            sender = new MessageSender();
        }

        return sender;
    }

    public void newsFromRouter(Channel receiver, char typeMessage, String idReceiver) {
        Message message = builder.getMessage(typeMessage, idReceiver);

        receiver.writeAndFlush( message.getFormedMessage() );
    }

    public void connectionAccepted(Channel receiver, String idReceiver) {
        Message message = builder.getMessage(TypeMessage.acceptConnection, idReceiver);

        receiver.writeAndFlush( message.getFormedMessage() );
    }

    public void order(Client sender, char typeOrder, String idReceiver, String typeProduct, int count, int price) {
        Message message = builder.getMessage(typeOrder, sender.getUniqueId(), idReceiver, typeProduct, count, price);

        sender.getChannel().writeAndFlush( message.getFormedMessage() );
    }

    public void formedMessage(Channel receiver, String formedMessage) {
        receiver.writeAndFlush(formedMessage);
    }

    public void orderResponse(Client sender, String[] messageToRemake, char typeMessage) {
        String  typeProduct = messageToRemake[8].replaceFirst(PrefixMessage.typeProduct, "");
        String  receiverId = messageToRemake[4].replaceFirst(PrefixMessage.sender, "");

        int     count = Integer.parseInt( messageToRemake[9].replaceFirst(PrefixMessage.countProduct, "") );
        int     price = Integer.parseInt( messageToRemake[10].replaceFirst(PrefixMessage.price, "") );

        OrderMessage message = (OrderMessage)builder.getMessage(typeMessage, sender.getUniqueId(), receiverId, typeProduct, count, price);
        message.setTypeOrder(PrefixMessage.typeOrder + messageToRemake[7] + '\1');

        sender.getChannel().writeAndFlush(message.getFormedMessage());
    }
}