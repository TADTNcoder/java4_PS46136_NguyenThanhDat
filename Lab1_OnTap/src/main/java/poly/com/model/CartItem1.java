package poly.com.model;

public class CartItem1 
{
	private Item item;
    private int quantity;

    public CartItem1(Item item, int quantity) 
    {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() 
    {
        return (item.getPrice() - item.getDiscount()) * quantity;
    }
}
