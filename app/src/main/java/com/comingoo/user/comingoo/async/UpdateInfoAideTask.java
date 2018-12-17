package com.comingoo.user.comingoo.async;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateInfoAideTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private String userId;
    private Uri imageUri;
    private EditText message;
    private TextView selectImage;
    private Resources resources;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public UpdateInfoAideTask(Context context, String userId, Uri imageUri, EditText message, TextView selectImage){

        this.context = context;
        this.userId = userId;
        this.imageUri = imageUri;
        this.message = message;
        this.selectImage = selectImage;

        String language = context.getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        Context co = LocalHelper.setLocale(context, language);
        resources = co.getResources();

    }

    // This is run in a background thread
    @Override
    protected String doInBackground(String... params) {

        final Map<String, String> data = new HashMap<>();
        data.put("user", userId);
        data.put("message", message.getText().toString());
        if (imageUri != null) {

            final StorageReference filepath = FirebaseStorage.getInstance().getReference("DRIVERCONTACTUS").child(userId);
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            data.put("image", uri.toString());
                            FirebaseDatabase.getInstance().getReference("CONTACTUSCLIENT").push().setValue(data);
                        }
                    });
                }
            });

            data.put("image", imageUri.toString());

        } else {
            data.put("image", "");
            FirebaseDatabase.getInstance().getReference("CONTACTUSCLIENT").push().setValue(data);
        }

        return "this string is passed to onPostExecute";
    }

    // This is called from background thread but runs in UI
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        // Do things like update the progress bar
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Do things like hide the progress bar or change a TextView

        Toast.makeText(context, resources.getString(R.string.message_warning_txt), Toast.LENGTH_SHORT).show();
        message.setText("");
        selectImage.setText(resources.getString(R.string.image_warning_txt));
    }
}