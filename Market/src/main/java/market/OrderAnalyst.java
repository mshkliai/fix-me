package market;

import models.Product;

public class OrderAnalyst {
    private final Market                market;

    public OrderAnalyst(Market market) {
        this.market = market;
    }

    public boolean analyzeOrder(char type, String typeProduct, int count, int price) {
        if (type == '2') {
            return analyzePurchase(typeProduct, count, price);
        }

        return analyzeSale(typeProduct, count, price);
    }

    private boolean analyzeSale(String typeProduct, int count, int price) {
        Product product = market.getProducts().get(typeProduct);
        boolean result = (
                product != null
                && product.getCount() >= count
                && product.getPrice() <= price
        );

        if (result) {
            product.setCount( product.getCount() - count );
            market.setMoney( market.getMoney() + count * price );
        }

        return result;
    }

    private boolean analyzePurchase(String typeProduct, int count, int price) {
        Product product = market.getProducts().get(typeProduct);
        boolean result = (
                product != null
                && product.getCount() < 400
                && market.getMoney() >= count * price
        );

        if (result) {
            product.setCount( product.getCount() + count );
            market.setMoney( market.getMoney() - count * price );
        }

        return result;
    }
}