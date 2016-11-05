package app.hoangcuong.com.mycontacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class addContacts extends AppCompatActivity {
    private FloatingActionButton fbCamera;
    private EditText edtName, edtPhone, edtEmail, edtAddress, edtNote;
    private ImageView imgPerson;
    private DBAdapter db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        AnhXa();
        db = new DBAdapter(getApplicationContext());
        fbCamera.setOnClickListener(onClickListener);

    }

    private void AddContact(){
        db.open();
        String name1 = edtName.getText().toString();
        String phone1 = edtPhone.getText().toString();
        String email1 = edtEmail.getText().toString();
        String address1 = edtAddress.getText().toString();
        String note1 = edtNote.getText().toString();
        byte[] image1 = null;
        Drawable background = imgPerson.getBackground();
        if(background != null){
            image1 = ImageView_To_Byte(imgPerson);
        }
        if(db.insertContact(name1,phone1,email1,address1,note1,image1)>=0){
            Toast.makeText(this, "Add successful.", Toast.LENGTH_LONG).show();
        }
        db.close();
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.fabCamera){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 100);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgPerson.setImageBitmap(photo);
        }
    }

    private void AnhXa(){
        fbCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        imgPerson = (ImageView) findViewById(R.id.imgPerson);
        edtName = (EditText) findViewById(R.id.edtName);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtNote = (EditText) findViewById(R.id.edtNote);
    }
    private byte[] ImageView_To_Byte(ImageView img){
            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            Bitmap bmp = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
         return byteArray;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.SaveContacts){
            AddContact();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contacts, menu);
        return true;
    }
}
