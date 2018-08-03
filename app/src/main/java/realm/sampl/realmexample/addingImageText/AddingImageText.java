package realm.sampl.realmexample.addingImageText;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import realm.sampl.realmexample.MainActivity;
import realm.sampl.realmexample.R;
import realm.sampl.realmexample.supporters.ImageText;
import realm.sampl.realmexample.supporters.ModelData;
import realm.sampl.realmexample.supporters.Sample;

public class AddingImageText extends AppCompatActivity
{
    ImageView image;
    TextView select_photo;
    EditText name,dob;
    Bitmap bit=null;
    String images;
    public static final int CAM_REQ_CODE=123;
    public static final int GAL_REQ_CODE=321;

    ArrayList<ModelData> al;
    String choice[]={"CAMERA","GALLERY"};
    public static final int CAM_PERMISSION_ACCESS_CODE=111;
    public static final String CAM_PERMISSION_NAME[]= {android.Manifest.permission.CAMERA};
    public static final int GAL_PERMISSION_ACCESS_CODE=222;
    public static final String GAL_PERMISSION_NAME[]={android.Manifest.permission.READ_EXTERNAL_STORAGE};
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_image_text);
        image=(ImageView)findViewById(R.id.image);
        select_photo=(TextView)findViewById(R.id.select_photo);
        name=(EditText)findViewById(R.id.name);
        dob=(EditText)findViewById(R.id.dob);
        realm=Realm.getDefaultInstance();
        select_photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //code for selecting the image and display on imageview
//                Intent ss=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(ss,CAM_REQ_CODE);
                selctPhoto();
            }
        });
    }


    public void  selctPhoto()
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setIcon(android.R.drawable.sym_def_app_icon);
        adb.setTitle(" Select One ");
        adb.setItems(choice, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                switch (i)
                {
                    case 0:
                        int res= ContextCompat.checkSelfPermission(AddingImageText.this, android.Manifest.permission.CAMERA);
                        if(res== PackageManager.PERMISSION_GRANTED)
                        {
                            Intent cam=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cam,CAM_REQ_CODE);
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(AddingImageText.this,CAM_PERMISSION_NAME,CAM_PERMISSION_ACCESS_CODE);
                        }
                        break;
                    case 1:
                        int res1=ContextCompat.checkSelfPermission(AddingImageText.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
                        if(res1==PackageManager.PERMISSION_GRANTED)
                        {
                            Intent gal=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(gal,GAL_REQ_CODE);
                        }
                        else
                        {
                            ActivityCompat.requestPermissions(AddingImageText.this,GAL_PERMISSION_NAME,GAL_PERMISSION_ACCESS_CODE);
                        }

                        break;
                }
            }
        });
        adb.create().show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case CAM_PERMISSION_ACCESS_CODE:
                if(CAM_PERMISSION_NAME.equals(permissions[0]) && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Intent cam=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cam,CAM_REQ_CODE);
                }
                break;

            case GAL_PERMISSION_ACCESS_CODE:
                if(GAL_PERMISSION_NAME.equals(permissions[0]) && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Intent gal=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gal,GAL_REQ_CODE);
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CAM_REQ_CODE:
                if(resultCode == RESULT_OK)
                {
                    Bundle b=data.getExtras();
                    bit=(Bitmap)b.get("data");
                    image.setImageBitmap(bit);
                }
                break;

            case GAL_REQ_CODE:

                if(resultCode == RESULT_OK)
                {
                    Uri img=data.getData();
                    try
                    {
                        bit=MediaStore.Images.Media.getBitmap(this.getContentResolver(),img);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    image.setImageBitmap(bit);
                }
                break;
        }
    }

    public void saveData(View v)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm)
            {

                ByteArrayOutputStream bout=new ByteArrayOutputStream();
                bit.compress(Bitmap.CompressFormat.JPEG,100,bout);
                byte img[]=bout.toByteArray();
                String image=Base64.encodeToString(img,Base64.DEFAULT);

                ImageText s = realm.createObject(ImageText.class);
                s.setImage(image);
                s.setName(name.getText().toString().trim());
                s.setDate(dob.getText().toString().trim());
                Log.e("from execute",""+realm);
            }
        }, new Realm.Transaction.OnSuccess()
        {
            @Override
            public void onSuccess()
            {
                //Toast.makeText(AddingImageText.this, "succesfully inserted data", Toast.LENGTH_SHORT).show();
                Log.e("from execute","success");
            }
        }, new Realm.Transaction.OnError()
        {
            @Override
            public void onError(Throwable error)
            {
                //Toast.makeText(AddingImageText.this, "failed to  insert data", Toast.LENGTH_SHORT).show();
                Log.e("from execute",""+error);
            }
        });
    }
    public void viewDetails(View v)
    {
        //on button clicks open new layout and display the data
        startActivity(new Intent(AddingImageText.this,DisplayAct.class));
//        RealmResults<ImageText> result2 = realm.where(ImageText.class).findAll();
//        realm.beginTransaction();
//        if (result2.isEmpty())
//        {
//
//        }
//        else
//        {
//
//            al=new ArrayList<>();
//            for(int i=0; i<result2.toArray().length; i++)
//            {
//                al.add(new ModelData(result2.get(i).getName(),result2.get(i).getDate(),result2.get(i).getImage()));
//            }
//            MainActivity.MyBaseAdapter aa=new MainActivity.MyBaseAdapter(al,AddingImageText.this);
//            mlist.setAdapter(aa);
//        }
    }
}
