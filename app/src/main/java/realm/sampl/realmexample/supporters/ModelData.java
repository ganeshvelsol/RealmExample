package realm.sampl.realmexample.supporters;

/**
 * Created by Velsol 170016 on 8/2/2018.
 */

public class ModelData
{
    String name,age,mImage;

    public ModelData() {
    }

    public ModelData(String name, String age, String mImage) {
        this.name = name;
        this.age = age;
        this.mImage = mImage;
    }

    public ModelData(String name, String age) {
        this.name = name;
        this.age = age;
    }


    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
