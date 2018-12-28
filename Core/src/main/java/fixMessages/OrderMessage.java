package fixMessages;

import lombok.Setter;

public class OrderMessage extends Message {
    @Setter private String  typeOrder;
    @Setter private String  countProduct;
    @Setter private String  priceProduct;
    @Setter private String  typeProduct;

    @Override
    public String getFormedMessage() {
        return formMessage();
    }

    @Override
    public int getIntLengthMessage() {
        return super.getIntLengthMessage()
                + typeOrder.length()
                + countProduct.length()
                + priceProduct.length()
                + typeProduct.length();
    }

    @Override
    protected String formMessage() {
        String message;

        message = super.formMessage() + typeOrder + typeProduct + countProduct + priceProduct;
        message = concatCheckSum(message);

        return message;
    }
}
