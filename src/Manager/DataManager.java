package Manager;

import Authentication.Account;
import Authentication.Admin;
import Authentication.Customer;
import OrderManagement.ShoppingCart;
import StockManagement.Category;
import StockManagement.Item;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class DataManager {
    private JSONObject data;
    public DataManager(){
        loadData();
    }
    private JSONObject toJsonObj(Customer acc){
        JSONObject user = new JSONObject();
        user.put("id",acc.getAccountID());
        user.put("name",acc.getName());
        user.put("password",acc.getPassword());
        user.put("phone",acc.getPhone());
        user.put("email",acc.getEmail());
        user.put("isAdmin",acc.isAdmin());
        user.put("cart",toJsonObj(acc.getCart()));
        user.put("address",acc.getAddress());
        return user;
    }
    private JSONObject toJsonObj(ShoppingCart Cart){
        JSONObject cart = new JSONObject();
        for( Map.Entry<Item, Integer> entry: Cart.getCartItems().entrySet()) {
            cart.put(entry.getKey().getId(),entry.getValue().toString());
        }
        return cart;
    }
    private JSONObject toJsonObj(Item it){
        JSONObject item = new JSONObject();
        item.put("id",it.getId());
        item.put("name",it.getName());
        item.put("loyaltyPoints",it.getLoyaltyPoints());
        item.put("category",it.getCategory());
        item.put("price",it.getPrice());
        item.put("timesOrdered",it.getTimesOrdered());
        return item;
    }
    private void loadData(){
        JSONParser parser = new JSONParser();
        try {
            data = (JSONObject) parser.parse(new FileReader("Data.json"));
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void saveData(){
        try {
            FileWriter file = new FileWriter("Data.json");
            file.write(data.toJSONString());
            file.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ShoppingCart getCart(JSONObject CartJson){
        ShoppingCart cart = new ShoppingCart();
        for(Object entry: CartJson.keySet()){
            System.out.print(CartJson.get(entry.toString()));
            cart.addItem(getItem(entry.toString()) , Integer.parseInt((String)CartJson.get(entry.toString())));
        }
        return cart;
    }
    public Customer getCustomer(String id){
        JSONObject customerJson =(JSONObject)((JSONObject)(data.get("users"))).get(id);
        return new Customer(
                (String) customerJson.get("id"),
                (String) customerJson.get("name"),
                (String) customerJson.get("password"),
                (String) customerJson.get("phone"),
                (String) customerJson.get("email"),
                (boolean)  customerJson.get("isAdmin"),
                getCart((JSONObject)customerJson.get("cart")),
                (String) customerJson.get("address"));
    }
    public Customer getCustomer(JSONObject customerJson){
        return new Customer(
                (String) customerJson.get("id"),
                (String) customerJson.get("name"),
                (String) customerJson.get("password"),
                (String) customerJson.get("phone"),
                (String) customerJson.get("email"),
                (boolean)  customerJson.get("isAdmin"),
                getCart((JSONObject)customerJson.get("cart")),
                (String) customerJson.get("address"));
    }
    public Admin getAdmin(JSONObject customerJson){
        return new Admin(
                (String) customerJson.get("id"),
                (String) customerJson.get("name"),
                (String) customerJson.get("password"),
                (String) customerJson.get("phone"),
                (String) customerJson.get("email")
        );
    }
    public Item getItem(String id){
        if(data.get("items") == null){
            System.out.print("No items found");
//            return new Item();
        }
        JSONObject item =(JSONObject)((JSONObject)(data.get("items"))).get(id);
        return new Item(
                (String) item.get("id"),
                (String) item.get("name"),
                (String) item.get("category"),
                (double) item.get("price"));

    }
    public Item getItem(JSONObject item){
        if(data.get("items") == null){
            System.out.print("No items found");
//            return new Item();
        }
        return new Item(
                (String) item.get("id"),
                (String) item.get("name"),
                (String) item.get("category"),
                (double) item.get("price"));

    }
    public void setCustomer(Customer customer){
        if(data.get("users") == null){ data.put("users",new JSONObject());}
        ((JSONObject)(data.get("users"))).put(customer.getAccountID(),toJsonObj(customer));
        saveData();
    }
    public void setItem(Item item){
        if(data.get("items") == null){ data.put("items",new JSONObject());}
        ((JSONObject)(data.get("items"))).put(item.getId(),toJsonObj(item));
        saveData();
    }
    public Account checkAuth(String email , String pass){
        JSONObject users = (JSONObject) data.get("users");
        Account acc = null;
        if(users!=null){
            for(Object user : users.values()){

                if(((JSONObject)user).get("email").toString().equals(email)
                && ((JSONObject)user).get("password").toString().equals(pass)){

                    if((boolean)((JSONObject)user).get("isAdmin"))acc = getAdmin((JSONObject)user);
                    else acc = getCustomer((JSONObject)user);

                }
            }
        }
        return acc;
    }
    public Vector<Item> getCatalog(){
        Vector<Item> getCatalog = new Vector<Item>();
        JSONObject items = (JSONObject) data.get("items");
        if(items!=null){
            for(Object item : items.values()){
                getCatalog.add(getItem((JSONObject) item));
            }
        }
        return getCatalog;
    }
    public void print(){
        System.out.print(data.toString());
    }
}
