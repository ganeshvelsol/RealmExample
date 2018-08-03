package realm.sampl.realmexample.supporters;

import io.realm.RealmObject;

/**
 * Created by Velsol 170016 on 8/3/2018.
 */

public class ImageText extends RealmObject
{
    String image,name,date;

    @Override
    public String toString()
    {
        return "ImageText{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
