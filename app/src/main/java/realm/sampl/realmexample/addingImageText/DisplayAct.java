package realm.sampl.realmexample.addingImageText;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import realm.sampl.realmexample.R;
import realm.sampl.realmexample.supporters.ImageText;
import realm.sampl.realmexample.supporters.ModelData;

public class DisplayAct extends AppCompatActivity
{
    GridView mgrid;
    Realm realm;
    ArrayList<ModelData> al;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        mgrid=(GridView)findViewById(R.id.mgrid);
        realm=Realm.getDefaultInstance();
        RealmResults<ImageText> result2 = realm.where(ImageText.class).findAll();
        realm.beginTransaction();
        if (result2.isEmpty())
        {

        }
        else
        {
            al=new ArrayList<>();
            for(int i=0; i<result2.toArray().length; i++)
            {
                al.add(new ModelData(result2.get(i).getName(),result2.get(i).getDate(),result2.get(i).getImage()));
            }
            MyBaseAdapter aa=new MyBaseAdapter(al,DisplayAct.this);
            mgrid.setAdapter(aa);

        }
        realm.close();
    }
    class MyBaseAdapter extends BaseAdapter
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
        public View getView(final int i, View view, ViewGroup viewGroup)
        {
            View v=getLayoutInflater().inflate(R.layout.cost_items, null, false);
            final ModelData employee = al.get(i);

            ImageView image_external=(ImageView)v.findViewById(R.id.image_external);
            TextView text1=(TextView)v.findViewById(R.id.text1);
            TextView text2=(TextView)v.findViewById(R.id.text2);
            LinearLayout linear=(LinearLayout)v.findViewById(R.id.linear);
            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder al=new AlertDialog.Builder(context);
                    al.setTitle("are sure to delete");
                    al.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            realm.beginTransaction();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm)
                                {

                                    try
                                    {
                                        RealmResults<ImageText> result = realm.where(ImageText.class).equalTo("ganesh","ganesh").findAll();
                                        result.deleteAllFromRealm();
                                        Log.e("from delete",""+realm);
                                        realm.close();
                                    }catch (Exception e)
                                    {
                                        Log.e("error",""+e);
                                    }
                                }
                            });
                            dialogInterface.dismiss();
                        }
                    });
                    al.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                        }
                    });
                    al.show();
                    Toast.makeText(DisplayAct.this, "position is"+i+" name "+employee.getName(), Toast.LENGTH_SHORT).show();

                }
            });

            text1.setText(""+employee.getName());
            text2.setText(""+employee.getAge());

            String image=(String)employee.getmImage();
            byte img[]= Base64.decode(image,Base64.DEFAULT);
            Bitmap bit= BitmapFactory.decodeByteArray(img,0,img.length);
            image_external.setImageBitmap(bit);
            return v;
        }
    }
}
