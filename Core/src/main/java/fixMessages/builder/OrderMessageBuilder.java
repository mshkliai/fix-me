package fixMessages.builder;

import defines.PrefixMessage;
import defines.TypeMessage;
import fixMessages.Message;
import fixMessages.OrderMessage;

public class OrderMessageBuilder {
    private static OrderMessageBuilder builder = null;

    public static OrderMessageBuilder getBuilder() {
        if (builder == null) {
            builder = new OrderMessageBuilder();
        }

        return builder;
    }

    public Message getBuiltMessage(char typeOrder, String typeProduct, int count, int price) {
        OrderMessage message = new OrderMessage();

        message.setTypeOrder(PrefixMessage.typeOrder + typeOrder + "\1");

        typeOrder = (typeOrder == '1' || typeOrder == '2' ? TypeMessage.order : typeOrder); // crutch =(

        message.setTypeMessage(PrefixMessage.type + typeOrder + "\1");
        message.setTypeProduct(PrefixMessage.typeProduct + typeProduct + "\1");
        message.setCountProduct(PrefixMessage.countProduct + count + "\1");
        message.setPriceProduct(PrefixMessage.price + price + "\1");

        return message;
    }

}