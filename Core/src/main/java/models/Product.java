package models;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class Product {
    @Getter @Setter private int price;
    @Getter @Setter private int count = new Random().nextInt(500);

    public Product(int price) {
        this.price = price;
    }
}