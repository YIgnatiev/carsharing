package com.carusselgroup.dwt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Car implements Parcelable {
    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        public Car createFromParcel(Parcel source) {
            return new Car(source);
        }

        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
    private int id;
    private String title;
    private Properties make;
    private Properties modelGroup;
    private ImageCar defaultImage;
    private ArrayList<ImageCar> images;
    private long importDate;
    private long licenseDate;
    private long price;
    private long discountPrice;
    private int mileageKm;
    private String status;

    public Car() {
    }

    private Car(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.make = in.readParcelable(Properties.class.getClassLoader());
        this.modelGroup = in.readParcelable(Properties.class.getClassLoader());
        this.defaultImage = in.readParcelable(ImageCar.class.getClassLoader());
        in.readTypedList(images, ImageCar.CREATOR);
        this.price = in.readLong();
        this.discountPrice = in.readLong();
        this.status = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Properties getMake() {
        return make;
    }

    public void setMake(Properties make) {
        this.make = make;
    }

    public Properties getModelGroup() {
        return modelGroup;
    }

    public void setModelGroup(Properties modelGroup) {
        this.modelGroup = modelGroup;
    }

    public ImageCar getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(ImageCar defaultImage) {
        this.defaultImage = defaultImage;
    }

    public ArrayList<ImageCar> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageCar> images) {
        this.images = images;
    }

    public int getMileageKm() {
        return mileageKm;
    }

    public void setMileageKm(int mileageKm) {
        this.mileageKm = mileageKm;
    }

    public String getCharacteristics() {
        if (title == null) {
            return "A 1.4 NET, 103 kW / 140 LE, FWD";
        }
        return title;
    }

    public long getImportDate() {
        return importDate;
    }

    public void setImportDate(long importDate) {
        this.importDate = importDate;
    }

    public long getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(long licenseDate) {
        this.licenseDate = licenseDate;
    }

    /*
    o   1010 – Hidden
    o   1020 – Active
    o   1030 – Deleted

     */
    public String getRawStatus() {
        return status;
    }

    public String getStatus() {
        String st = "No Status";
        switch (status) {
            case "1010":
                st = "Hidden";
                break;
            case "1020":
                st = "Active";
                break;
            case "1030":
                st = "Deleted";
                break;
        }
        return st;
    }

    public String getBrand() {
        return make.getTitle();
    }

    public void setBrand(String s) {
        make = new Properties();
        make.setTitle(s);
    }

    public String getModel() {
        return modelGroup.getTitle();
    }

    public void setModel(String model) {
        modelGroup = new Properties();
        modelGroup.setTitle(model);
    }

    public String getLocation() {
        return "No location";
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(long discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getJobNumber() {
        return "123";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeParcelable(this.make, 0);
        dest.writeParcelable(this.modelGroup, 0);
        dest.writeParcelable(this.defaultImage, 0);
        dest.writeTypedList(images);
        dest.writeLong(this.price);
        dest.writeLong(this.discountPrice);
        dest.writeString(this.status);
        dest.writeLong(this.importDate);
        dest.writeLong(this.licenseDate);
        dest.writeInt(this.mileageKm);

    }
}
/**
 {
 id: 44952,
 make: {
 id: 1,
 title: "Opel"
 },
 modelGroup: {
 id: 18,
 title: "Antara"
 },
 title: "Opel Antara Cosmo AWD, A 2.2 DZ, 120 kW / 163 LE, Start/Stop",
 defaultImage: {
 id: 224958,
 origUrl: "http://vacs.hu.opel.carusseldwt.com/imageRes?imgId=224958",
 thumbnailUrl: "http://vacs.hu.opel.carusseldwt.com/imageRes?imgId=224958&size=64",
 mediumUrl: "http://vacs.hu.opel.carusseldwt.com/imageRes?imgId=224958&size=300"
 },
 images: [
 {
 id: 224958,
 origUrl: "http://vacs.hu.opel.carusseldwt.com/imageRes?imgId=224958",
 thumbnailUrl: "http://vacs.hu.opel.carusseldwt.com/imageRes?imgId=224958&size=64",
 mediumUrl: "http://vacs.hu.opel.carusseldwt.com/imageRes?imgId=224958&size=300"
 }
 ]
 }
 **/