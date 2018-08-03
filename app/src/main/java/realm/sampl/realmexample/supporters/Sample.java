package realm.sampl.realmexample.supporters;

import io.realm.RealmObject;

/**
 * Created by Velsol 170016 on 8/2/2018.
 */

public class Sample extends RealmObject
{
 String personName,personAge;

    @Override
    public String toString()
    {
        return "Sample{" +
                "personName='" + personName + '\'' +
                ", personAge='" + personAge + '\'' +
                '}';
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonAge() {
        return personAge;
    }

    public void setPersonAge(String personAge) {
        this.personAge = personAge;
    }
}
