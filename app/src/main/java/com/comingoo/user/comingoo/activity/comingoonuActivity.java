package com.comingoo.user.comingoo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class comingoonuActivity extends AppCompatActivity {

    private int selectedScreen = 0;
    private ConstraintLayout parametreLayout, profileLayout, changePasswordLayout, portFeuilleLayout;
    private ImageView paramsImage, profilImage, portFImage;
    private ConstraintLayout pA, pR, pO;
    private ConstraintLayout changePasswordButton;
    private ImageButton backChangePassword;
    private TextView todayEarnings, userName, phoneNumber, emailAddress;
    private Button changePassBtn;
    private TextView langVal, depart, minimum, routePerKM, perHour;
    private ConstraintLayout tarifs;
    private ConstraintLayout tarifsLayout;
    private ConstraintLayout lang;
    private ImageButton useC;
    private int useCV = 0;
    private SharedPreferences sharedPreferences;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comingoonu);

        parametreLayout = findViewById(R.id.parametres_layout);
        profileLayout = findViewById(R.id.profil_layout);
        changePasswordLayout = findViewById(R.id.change_password);
        portFeuilleLayout = findViewById(R.id.porte_feuille_layout);

        paramsImage = findViewById(R.id.imageView13);
        profilImage = findViewById(R.id.imageView17);
        portFImage = findViewById(R.id.imageView16);

        useC = findViewById(R.id.use_credit);

        langVal = findViewById(R.id.lang_value);

        pA = findViewById(R.id.paLayout);
        pR = findViewById(R.id.prLayout);
        pO = findViewById(R.id.poLayout);

        depart = findViewById(R.id.textView66);
        minimum = findViewById(R.id.textView73);
        routePerKM = findViewById(R.id.textView74);
        perHour = findViewById(R.id.textView75);

        changePasswordButton = findViewById(R.id.change_password_button);
        backChangePassword = findViewById(R.id.back_select_password);

        sharedPreferences = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE);
        language = sharedPreferences.getString("language", "fr");

        lang = findViewById(R.id.lang);

        todayEarnings = findViewById(R.id.earnings_value);
        userName = findViewById(R.id.name_value);
        phoneNumber = findViewById(R.id.phone_value);
        emailAddress = findViewById(R.id.email_value);


        userName.setText(getIntent().getStringExtra("name"));
        phoneNumber.setText(getIntent().getStringExtra("phone"));
        emailAddress.setText(getIntent().getStringExtra("email"));

        changePassBtn = findViewById(R.id.change_password_btn);

        tarifs = findViewById(R.id.tarifs);
        tarifsLayout = findViewById(R.id.tarifsLayout);

        selectedScreen = 0;
        updateUI();


        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLengo();
            }
        });

        tarifs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScreen = 6;
                updateUI();
            }
        });


        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        final String userId = prefs.getString("userID", null);


        FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("SOLDE").getValue(String.class) != null)
                    todayEarnings.setText(dataSnapshot.child("SOLDE").getValue(String.class) + " MAD");
                else todayEarnings.setText("0 MAD");

                useCV = Integer.parseInt(dataSnapshot.child("USECREDIT").getValue(String.class));

                if (useCV == 0) {
                    useC.setBackgroundResource(R.drawable.unchecked_cnu);
                } else {
                    useC.setBackgroundResource(R.drawable.checked_cnu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("PRICES").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                routePerKM.setText((new DecimalFormat("##.##").format(Double.parseDouble(dataSnapshot.child("km").getValue(String.class)))) + " MAD");
                perHour.setText((new DecimalFormat("##.##").format(Double.parseDouble(dataSnapshot.child("att").getValue(String.class)))) + " MAD");
                depart.setText(dataSnapshot.child("base").getValue(String.class) + " MAD");
                minimum.setText(dataSnapshot.child("minimum").getValue(String.class) + " MAD");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        useC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (useCV == 0) {
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("USECREDIT").setValue("1");
                } else {
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("USECREDIT").setValue("0");
                }
            }
        });


        String Month;
        String day;
        Month = getDateMonth(GetUnixTime());
        day = getDateDay(GetUnixTime());
        if (Month.equals("01")) Month = "Janvier";
        if (Month.equals("02")) Month = "Février";
        if (Month.equals("03")) Month = "Mars";
        if (Month.equals("04")) Month = "Avril";
        if (Month.equals("05")) Month = "Mai";
        if (Month.equals("06")) Month = "Juin";
        if (Month.equals("07")) Month = "Juillet";
        if (Month.equals("08")) Month = "Aout";
        if (Month.equals("09")) Month = "Septembre";
        if (Month.equals("10")) Month = "Octobre";
        if (Month.equals("11")) Month = "Novembre";
        if (Month.equals("12")) Month = "Décembre";

        TextView d = findViewById(R.id.date);
        d.setText(Month + " " + day);


        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView oldPass = findViewById(R.id.oldPass);
                TextView newPass = findViewById(R.id.newPass);
                TextView confirmPass = findViewById(R.id.confirmPass);
                updatePassword(oldPass.getText().toString(), newPass.getText().toString(), confirmPass.getText().toString());
            }
        });
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScreen = 3;
                updateUI();
            }
        });
        backChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedScreen = 1;
                updateUI();
            }
        });


        pA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    if(selectedScreen == 1 || selectedScreen == 5){
                selectedScreen = 0;
                updateUI();
                //   }
            }
        });

        pR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if(selectedScreen == 0 || selectedScreen == 5){
                selectedScreen = 1;
                updateUI();
                // }
            }
        });

        pO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   if(selectedScreen == 1 || selectedScreen == 0){
                selectedScreen = 5;
                updateUI();
                //  }
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateViews();
    }

    private void hideEverything() {
        parametreLayout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.GONE);
        changePasswordLayout.setVisibility(View.GONE);
        portFeuilleLayout.setVisibility(View.GONE);
        tarifsLayout.setVisibility(View.GONE);
    }

    private void toolBarUI() {

        if (selectedScreen == 0 || selectedScreen == 6) {
            paramsImage.setBackgroundResource(R.drawable.parametres_selected);
            profilImage.setBackgroundResource(R.drawable.profil_cnu);
            portFImage.setBackgroundResource(R.drawable.porte_feuille_unselected);

        } else if (selectedScreen == 1 || selectedScreen == 2 || selectedScreen == 3 || selectedScreen == 4) {
            paramsImage.setBackgroundResource(R.drawable.parametre_cnu);
            profilImage.setBackgroundResource(R.drawable.profil_selected);
            portFImage.setBackgroundResource(R.drawable.porte_feuille_unselected);
        } else {
            paramsImage.setBackgroundResource(R.drawable.parametre_cnu);
            profilImage.setBackgroundResource(R.drawable.profil_cnu);
            portFImage.setBackgroundResource(R.drawable.porte_feuille_selected);
        }

    }

    private void updateUI() {
        hideEverything();
        toolBarUI();
        switch (selectedScreen) {
            case 0:
                AnimateConstraint.fadeIn(comingoonuActivity.this, parametreLayout, 500, 10);
                break;
            case 1:
                AnimateConstraint.fadeIn(comingoonuActivity.this, profileLayout, 500, 10);
                break;
            case 2:
                break;
            case 3:
                AnimateConstraint.fadeIn(comingoonuActivity.this, changePasswordLayout, 500, 10);
                break;
            case 4:
                break;
            case 5:
                AnimateConstraint.fadeIn(comingoonuActivity.this, portFeuilleLayout, 500, 10);
                break;
            case 6:
                AnimateConstraint.fadeIn(comingoonuActivity.this, tarifsLayout, 500, 10);
                break;
        }

    }


    public void updateViews() {
        Context context;
        Resources resources;

        context = LocalHelper.setLocale(comingoonuActivity.this, language);
        resources = context.getResources();

        TextView textView19 = findViewById(R.id.textView19);
        TextView textView34 = findViewById(R.id.textView34);
        TextView textView25 = findViewById(R.id.textView25);
        TextView textView36 = findViewById(R.id.textView36);
        TextView textView38 = findViewById(R.id.textView38);
        TextView textView39 = findViewById(R.id.textView39);
        TextView textView40 = findViewById(R.id.textView40);
        TextView textView41 = findViewById(R.id.textView41);
        TextView textView42 = findViewById(R.id.textView42);
        TextView textView43 = findViewById(R.id.tv_user_email_text);
        TextView textView60 = findViewById(R.id.textView60);
        TextView textView55 = findViewById(R.id.textView55);
        TextView textView56 = findViewById(R.id.textView56);
        TextView textView57 = findViewById(R.id.textView57);
        TextView textView51 = findViewById(R.id.textView51);
        TextView textView54 = findViewById(R.id.textView54);
        TextView textView32 = findViewById(R.id.textView32);
        TextView textView3 = findViewById(R.id.textView3);
        TextView textView63 = findViewById(R.id.textView63);
        TextView textView64 = findViewById(R.id.textView64);
        TextView textView68 = findViewById(R.id.textView68);
        TextView textView69 = findViewById(R.id.textView69);
        TextView textView70 = findViewById(R.id.textView70);
        TextView textView71 = findViewById(R.id.textView71);
        TextView textView72 = findViewById(R.id.textView72);
        TextView textView76 = findViewById(R.id.textView76);


        //Set Texts
        langVal.setText(resources.getString(R.string.language_value));
        textView19.setText(resources.getString(R.string.Paramétres));
        textView34.setText(resources.getString(R.string.Profil));
        textView25.setText(resources.getString(R.string.Portefeuille));
        textView36.setText(resources.getString(R.string.Langue));
        textView38.setText(resources.getString(R.string.Tarifs));
        textView39.setText(resources.getString(R.string.Conditions));
        textView40.setText(resources.getString(R.string.version));
        textView41.setText(resources.getString(R.string.Nomcomplet));
        textView42.setText(resources.getString(R.string.Numérodetéléphone));
        textView43.setText(resources.getString(R.string.Changer));
        textView60.setText(resources.getString(R.string.Changer));
        textView55.setText(resources.getString(R.string.Motdepasseactuel));
        textView56.setText(resources.getString(R.string.Nouveaumotdepasse));
        textView57.setText(resources.getString(R.string.Confirmerlenouveaumotdepasse));
        textView51.setText(resources.getString(R.string.Aujourdhui));
        textView54.setText(resources.getString(R.string.Crédit));
        textView32.setText(resources.getString(R.string.Utiliserlecréditenpremier));
        textView3.setText(resources.getString(R.string.Tarifspromotionnels));
        textView63.setText(resources.getString(R.string.Départ));
        textView64.setText(resources.getString(R.string.Démarreraveccetarif));
        textView68.setText(resources.getString(R.string.Courseminimum));
        textView69.setText(resources.getString(R.string.EnRouteparKm));
        textView70.setText(resources.getString(R.string.TarificationparKm));
        textView71.setText(resources.getString(R.string.Enattenteparheure));
        textView72.setText(resources.getString(R.string.sapplique));
        textView76.setText(resources.getString(R.string.pénalité));
    }

    private void updatePassword(final String oldPassword, final String newPassword, final String confirmPassword) {
        changePassBtn.setVisibility(View.GONE);
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        final String userId = prefs.getString("userID", null);

        if (newPassword.length() != 0 && confirmPassword.length() != 0 && oldPassword.length() != 0) {
            if (newPassword.length() < 8) {
                Toast.makeText(comingoonuActivity.this, "Mot de passe doit avoir 8 lettres.", Toast.LENGTH_SHORT).show();
                changePassBtn.setVisibility(View.VISIBLE);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            } else if (newPassword.length() >= 8 && confirmPassword.length() >= 8) {

                FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(dataSnapshot.child("email").getValue(String.class), oldPassword);


                            FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            selectedScreen = 1;
                                                            updateUI();
                                                        } else {
                                                            Toast.makeText(comingoonuActivity.this, "Error!!! ", Toast.LENGTH_SHORT).show();
                                                            changePassBtn.setVisibility(View.VISIBLE);
                                                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(comingoonuActivity.this, "Error!!! ", Toast.LENGTH_SHORT).show();
                                                changePassBtn.setVisibility(View.VISIBLE);
                                                findViewById(R.id.progressBar).setVisibility(View.GONE);
                                            }
                                        }
                                    });


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            } else {
                Toast.makeText(comingoonuActivity.this, "Tous les champs doivent étres remplis!!", Toast.LENGTH_SHORT).show();
                changePassBtn.setVisibility(View.VISIBLE);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }
    }


    private String getDateMonth(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        String date = DateFormat.format("MM", cal).toString();
        return date;
    }

    private String getDateDay(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        String date = DateFormat.format("dd", cal).toString();
        return date;
    }

    public int GetUnixTime() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        int utc = (int) (now / 1000);
        return (utc);

    }


    public void toggleLengo() {
        switch (language) {
            case "en":
                language = "fr";
                sharedPreferences.edit().putString("language", language).apply();
                break;
            case "fr":
                language = "en";
                sharedPreferences.edit().putString("language", language).apply();
                break;
            default:
                language = "en";
                sharedPreferences.edit().putString("language", language).apply();
                break;
        }
        updateViews();
    }

}
