package fixMessages;

import lombok.Setter;

public class NewsMessage extends Message {
    @Setter private String bodyMessage;

    @Override
    public int getIntLengthMessage() {
        return super.getIntLengthMessage()
                + bodyMessage.length();
    }

    public String   getFormedMessage() {
        return formMessage();
    }

    @Override
    protected String  formMessage() {
        String message;

        message = super.formMessage() + bodyMessage;
        message = concatCheckSum(message);

        return message;
    }
}