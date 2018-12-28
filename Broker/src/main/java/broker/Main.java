package broker;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        new Broker().run();
    }
}
