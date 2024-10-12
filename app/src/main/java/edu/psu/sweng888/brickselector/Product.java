package edu.psu.sweng888.brickselector;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private String description;
    private String seller;
    private double price;
    private byte[] image;

    public Product(int id, String name, String description, String seller, double price, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.image = image;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        seller = in.readString();
        price = in.readDouble();
        image = in.createByteArray();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(seller);
        dest.writeDouble(price);
        dest.writeByteArray(image);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public String getSeller() {
        return seller;
    }

    public double getPrice(){
        return price;
    }
    public byte[] getImage(){
        return image;
    }

    // and Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setImage(byte[] image){
        this.image = image;
    }
}


