package OrderManagement;

import java.util.Date;

public class Order {
    private String orderID;
    private ShoppingCart cart;
    private boolean status;
    private Payment bill;
    private String shippingAddress;
    private Date orderDate;

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setBill(Payment bill) {
        this.bill = bill;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }


    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderID() {
        return orderID;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public boolean isStatus() {
        return status;
    }

    public Payment getBill() {
        return bill;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public Date getOrderDate() {
        return orderDate;
    }
}