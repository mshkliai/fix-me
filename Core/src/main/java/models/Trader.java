package models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.SplittableRandom;

public class Trader {
    @Getter @Setter private int                 money;
    @Getter private HashMap<String, Product>    products = new HashMap<>();

    public Trader() {
        SplittableRandom rand = new SplittableRandom();

        money = rand.nextInt(10000, 100000);

        products.put( "GOLD", new Product(rand.nextInt(100, 200)) );
        products.put( "SLVR", new Product(rand.nextInt(25, 50)) );
        products.put( "PLTN", new Product(rand.nextInt(300, 400)) );
        products.put( "OIL", new Product(rand.nextInt(15, 30)) );
        products.put( "AAPL", new Product(rand.nextInt(150, 200)) );
        products.put( "MSFT", new Product(rand.nextInt(75, 100)) );
        products.put( "AMZN", new Product(rand.nextInt(1100, 1400)) );
        products.put( "MCD", new Product(rand.nextInt(170, 200)) );
    }
}