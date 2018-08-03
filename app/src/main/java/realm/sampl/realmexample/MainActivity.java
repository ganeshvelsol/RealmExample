package realm.sampl.realmexample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import realm.sampl.realmexample.supporters.ModelData;
import realm.sampl.realmexample.supporters.Sample;

public class MainActivity extends AppCompatActivity {
    EditText name,age;
    Realm realm;
    ArrayList<ModelData> al;
    ListView mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(EditText)findViewById(R.id.name);
        age=(EditText)findViewById(R.id.age);
        mlist=(ListView)findViewById(R.id.mlist);
        realm=Realm.getDefaultInstance();
    }
    public void save(View v)
    {
        //read data from edittets and upload into realm

        writeToDb(name.getText().toString(),age.getText().toString());
    }
    public void writeToDb(final String Mname, final String Mage)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Sample s = realm.createObject(Sample.class);
                s.setPersonName(Mname);
                s.setPersonAge(Mage);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess()
            {
                //once the data is success
                Log.e("realm Db","data inserted");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error)
            {
                //once the data is error
                Log.e("real db","data insertion failed");
            }
        });
    }
    public void views(View v)
    {
        RealmResults<Sample> result2 = realm.where(Sample.class).findAll();
        realm.beginTransaction();
        if (result2.isEmpty())
        {
          Log.e("reading ","no datafound");
        }
        else
        {
            al=new ArrayList();


            for(int i=0; i<result2.toArray().length; i++)
            {
                al.add(new ModelData(result2.get(i).getPersonName(),result2.get(i).getPersonAge()));
            }
            MyBaseAdapter aa=new MyBaseAdapter(al,MainActivity.this);
            mlist.setAdapter(aa);
           // Log.e("list data",""+al);
//            for(int j=0;j<al.size();j++)
//            {
//                Toast.makeText(this, ""+al.get(j), Toast.LENGTH_SHORT).show();
//            }

            //Log.e("reading","data found"+result2.toString());
            //Log.e("lenght",""+result2.toArray().length);
            /*result2.load();
            String output="";
            for(Sample information:result2)
            {
                output+=information.toString();
                //Log.e("out put",""+output);
                //Log.e("out put2",""+output.indexOf(1));



            }*/
        }

    }
    public class MyBaseAdapter extends BaseAdapter
    {

        List<ModelData> al;
        Context context;

        public MyBaseAdapter(List<ModelData> al, Context context) {
            this.al = al;
            this.context = context;
        }

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View mview = getLayoutInflater().inflate(R.layout.cost_items, null, false);

            ModelData employee = al.get(i);
            TextView text1=(TextView)mview.findViewById(R.id.text1);
            TextView text2=(TextView)mview.findViewById(R.id.text2);

            text1.setText(""+employee.getName());
            text2.setText(""+employee.getAge());
            return mview;
        }
    }

}
