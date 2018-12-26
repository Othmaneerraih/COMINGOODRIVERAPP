package com.comingoo.user.comingoo.ViewModel;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.model.place;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivityVM {
    private Context mContext;
    private EditText etSearchStartAddress, etSearchDestination;
    private int driverSearchingState = 0;

    public MapsActivityVM(Context context, EditText etSearchStartAddress, EditText etSearchDestination) {
        this.mContext = context;
        this.etSearchStartAddress = etSearchStartAddress;
        this.etSearchDestination = etSearchDestination;
    }

    public void setSearchFunc() {

//        findViewById(R.id.coverButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectStart.setVisibility(View.GONE);
//                findViewById(R.id.shadow).setVisibility(View.GONE);
//                bottomMenu.setVisibility(View.GONE);
//                selectedOp.setVisibility(View.GONE);
//                selectDest.setVisibility(View.GONE);
//                findViewById(R.id.coverButton).setVisibility(View.GONE);
//                state = -1;
//                showFavoritsAndRecents();
//            }
//        });


//        etSearchStartAddress.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                rvSearchResult.setVisibility(View.VISIBLE);
//                return true;
//            }
//        });

        etSearchStartAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
//                     hideSearchAddressStartUI();
                    etSearchStartAddress.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                            txt = this;
                            if (etSearchStartAddress.getText().toString().length() >= 3) {
//                                lookForAddress();
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
//                    etSearchStartAddress.removeTextChangedListener(txt);
                }
            }
        });

        etSearchDestination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
//                     hideSearchAddressStartUI();
                    etSearchDestination.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                            txtDest = this;
                            if (etSearchDestination.getText().toString().length() >= 3) {
//                                lookForAddress();
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
//                    etSearchDestination.removeTextChangedListener(txtDest);
                }
            }
        });

//        etSearchStartAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_DONE) {
//                    hideKeyboard(MapsActivityNew.this);
//                    etSearchStartAddress.clearFocus();
//                    etSearchStartAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
////                    lookForAddress();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        etSearchDestination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_DONE) {
//                    hideKeyboard(MapsActivityNew.this);
//                    etSearchDestination.clearFocus();
//                    //lookForAddress();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        etSearchDestination.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideKeyboard(MapsActivityNew.this);
//                etSearchDestination.clearFocus();
////                searchButtonDest.setVisibility(View.GONE);
////                searchProgBarDest.setVisibility(View.VISIBLE);
//                lookForAddress();
//            }
//        });


    }


//    public void lookForAddress() {
//        etSearchStartAddress.clearFocus();
//        etSearchDestination.clearFocus();
//        //hideKeyboard(MapsActivity.this);
//        if ((etSearchStartAddress.getText().toString().length() == 0 && driverSearchingState == 0)
//                || (etSearchDestination.getText().toString().length() == 0 && driverSearchingState == 1)) {
//            showSearchAddressStartUI();
//            return;
//        }
////        if (driverSearchingState == 1) {
////            rlStartPoint.setVisibility(View.INVISIBLE);
////        }
//        new LookForAddressTask().execute();
//    }



//    public void showSearchAddressStartUI() {
//        ivDrawerToggol.setVisibility(View.VISIBLE);
//        state = 0;
//        placeDataList.clear();
//        placeAdapter.notifyDataSetChanged();
//        rlStartPoint.setVisibility(View.VISIBLE);
//        etSearchStartAddress.clearFocus();
//        etSearchStartAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
////        searchDestEditText.clearFocus();
//
//        AnimateConstraint.animate(mContext, etSearchStartAddress, 1, 1, 1);
//
////        if (driverSearchingState == 0) {
////            selectStart.setVisibility(View.VISIBLE);
////            bottomMenu.setVisibility(View.VISIBLE);
////            selectedOp.setVisibility(View.VISIBLE);
////            shadowBg.setVisibility(View.VISIBLE);
////        }
////        if (driverSearchingState == 1) {
////            selectDest.setVisibility(View.VISIBLE);
////        }
//    }
//
//
//    private class LookForAddressTask extends AsyncTask<String, Integer, String> {
//        boolean finished;
//        String searchText;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            placeDataList.clear();
//            finished = false;
//            if (driverSearchingState == 0)
//                searchText = etSearchStartAddress.getText().toString();
//            if (driverSearchingState == 1)
//                searchText = etSearchDestination.getText().toString();
//        }
//
//        // This is run in a background thread
//        @Override
//        protected String doInBackground(String... params) {
//
//            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                    .setCountry("MA")
//                    .build();
//
//            mGeoDataClient.getAutocompletePredictions(searchText + " " + searchLoc, null,
//                    typeFilter).addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
//                @Override
//                public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
//                    if (task.isSuccessful() && task.getResult().getCount() > 0) {
//                        finished = true;
//                        AutocompletePredictionBufferResponse aa = task.getResult();
//                        for (final AutocompletePrediction ap : aa) {
//                            mGeoDataClient.getPlaceById(ap.getPlaceId()).addOnCompleteListener(
//                                    new OnCompleteListener<PlaceBufferResponse>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
//                                            if (task.isSuccessful() && task.getResult().getCount() > 0) {
//                                                for (Place gotPlace : task.getResult()) {
//                                                    place Place = new place(gotPlace.getName().toString(),
//                                                            gotPlace.getAddress().toString(), "" + gotPlace.getLatLng().latitude,
//                                                            "" + gotPlace.getLatLng().longitude, R.drawable.lieux_proches);
//                                                    placeDataList.add(Place);
//                                                }
//                                                placeAdapter.notifyDataSetChanged();
//                                                finished = true;
//                                            } else {
//                                                finished = true;
//                                            }
//                                        }
//                                    });
//                        }
//                    } else {
//                        finished = true;
//                    }
//                }
//            });
//            return "this string is passed to onPostExecute";
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            int Height = 67 * placeDataList.size();
//            AnimateConstraint.animate(getApplicationContext(), rvSearchResult, HeightAbsolute, HeightAbsolute, 1);
//            AnimateConstraint.animate(getApplicationContext(), rlFavouritePlace, 1, 1, 1);
//            if (driverSearchingState == 0) {
////                searchButton.setVisibility(View.VISIBLE);
//                rvSearchResult.setVisibility(View.VISIBLE);
//                //if(Height > (dpHeight - (270)))
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
////                rlStartPoint.setVisibility(View.GONE);
////                ivWheel.setVisibility(View.GONE);
////                ivShadow.setVisibility(View.GONE);
//            }
//
//            if (driverSearchingState == 1) {
//                btnConfirmDestination.setVisibility(View.VISIBLE);
//                rvSearchResult.setVisibility(View.VISIBLE);
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//                llConfirmDestination.setVisibility(View.GONE);
//            }
////            searchProgBar.setVisibility(View.GONE);
////            searchProgBarDest.setVisibility(View.GONE);
//        }
//    }
//
//    public void showFavoritsAndRecents() {
//        rPlaceDataList.add(getRecentPlaces(getApplicationContext()));
//        placeAdapter.notifyDataSetChanged();
//        AnimateConstraint.animate(MapsActivityNew.this, rvFavPlaces, HeightAbsolute, 1, 100);
//
//        findViewById(R.id.x).setVisibility(View.VISIBLE);
////        findViewById(R.id.my_position).setVisibility(View.GONE);
////        findViewById(R.id.adress_result).setVisibility(View.INVISIBLE);
//    }

}
