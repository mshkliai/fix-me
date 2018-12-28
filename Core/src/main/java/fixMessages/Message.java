package fixMessages;

import defines.PrefixMessage;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Message {
    private String                  time = new SimpleDateFormat("52=YYYYMMdd-HH:mm:ss.SSS\1").format(new Date());

    @Setter private String          lengthMessage;
    @Setter private String          typeMessage;
    @Setter private String          numMessage;
    @Setter private String          sender;
    @Setter private String          receiver;
    @Getter @Setter private String  checkSum;

    public int  getIntLengthMessage() {
        return time.length()
                + typeMessage.length()
                + numMessage.length()
                + sender.length()
                + receiver.length();
    }

    protected String formMessage() {
        String fixFormat = "8=FIX.4.2\1";

        return fixFormat
                + lengthMessage
                + typeMessage
                + numMessage
                + sender
                + receiver
                + time;
    }

    protected String concatCheckSum(String message) {
        int sum = 0;

        for (byte b : message.getBytes()) {
            sum += b;
        }

        return message + PrefixMessage.checkSum + (sum & 255) + '\1';
    }

    /**************** Abstract Method **********************/

    public abstract String getFormedMessage();
}