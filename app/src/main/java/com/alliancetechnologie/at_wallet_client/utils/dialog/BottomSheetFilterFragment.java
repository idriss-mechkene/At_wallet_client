package com.alliancetechnologie.at_wallet_client.utils.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.utils.FilterConditions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BottomSheetFilterFragment extends BottomSheetDialogFragment implements View.OnClickListener, onDatePickerInterface, TextWatcher {

    private static final String ARG_PARAM = "FILTER";
    Button btnFilter;
    RangeSlider rangeSlider;
    TextInputEditText inputMinPrice, inputMaxPrice, inputMaxDate, inputMinDate;
    FilterConditions filterConditions;
    OnFilterInterface onFilterInterface;
    TextInputLayout layoutMinPrice, layoutMaxPrice, layoutMaxDate, layoutMinDate;
    TextView errorDate,errorPrice;


    public BottomSheetFilterFragment() {
    }

    public static BottomSheetFilterFragment newInstance(String Tag) {
        BottomSheetFilterFragment fragment = new BottomSheetFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, Tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Tag = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet_history_filter, container, false);
        rangeSlider = view.findViewById(R.id.price_slider);
        inputMaxPrice = view.findViewById(R.id.input_max_price);
        inputMinPrice = view.findViewById(R.id.input_min_price);
        inputMinDate = view.findViewById(R.id.input_min_date);
        inputMaxDate = view.findViewById(R.id.input_max_date);
        layoutMinDate = view.findViewById(R.id.input_layout_min_date);
        layoutMaxDate = view.findViewById(R.id.input_layout_max_date);
        layoutMinPrice = view.findViewById(R.id.input_layout_min_price);
        layoutMaxPrice = view.findViewById(R.id.input_layout_max_price);
        errorDate = view.findViewById(R.id.txt_error_date);
        errorPrice = view.findViewById(R.id.txt_error_price);





        btnFilter = view.findViewById(R.id.btn_filter);

        inputMinDate.setOnClickListener(this);
        inputMaxDate.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        inputMaxPrice.addTextChangedListener(this);
        inputMinPrice.addTextChangedListener(this);
        inputMinDate.addTextChangedListener(this);
        inputMaxDate.addTextChangedListener(this);
        initFilter();




        return view;
    }
    @SuppressLint("DefaultLocale")
    private void initFilter(){
        if(filterConditions.getMaxDate()!= null){
            inputMaxDate.setText(filterConditions.getMaxDate());
        }
        if(filterConditions.getMinDate()!= null){
            inputMaxDate.setText(filterConditions.getMinDate());
        }
        if(filterConditions.getMaxPrice()!= null){
            inputMaxPrice.setText(String.format("%d",filterConditions.getMaxPrice()));
        }
        if(filterConditions.getMinPrice()!= null){
            inputMinPrice.setText(String.format("%d",filterConditions.getMinPrice()));
        }
    }
    public void setFilterFragmentInterfaceAndFilter(OnFilterInterface onFilterInterface, FilterConditions filterConditions){
        this.onFilterInterface=onFilterInterface;
        this.filterConditions=filterConditions;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                inputMinPrice.setText(String.valueOf(slider.getValues().get(0).longValue()));
                inputMaxPrice.setText(String.valueOf(slider.getValues().get(1).longValue()));
                filterConditions.setMinPrice(slider.getValues().get(0).longValue());
                filterConditions.setMaxPrice(slider.getValues().get(1).longValue());

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_min_date:
                BottomSheetDatePrickerFragment bottomSheetDatePrickerFragmentMin = BottomSheetDatePrickerFragment.newInstance(inputMinDate.getId());
                bottomSheetDatePrickerFragmentMin.setDatePickerInterface(this);
                bottomSheetDatePrickerFragmentMin.show(getParentFragmentManager(), bottomSheetDatePrickerFragmentMin.getTag());
                break;
            case R.id.input_max_date:
                BottomSheetDatePrickerFragment bottomSheetDatePrickerFragmentMax = BottomSheetDatePrickerFragment.newInstance(inputMaxDate.getId());
                bottomSheetDatePrickerFragmentMax.setDatePickerInterface(this);
                bottomSheetDatePrickerFragmentMax.show(getParentFragmentManager(), bottomSheetDatePrickerFragmentMax.getTag());
                break;
            case R.id.btn_filter:
                if(Valider()){
                    onFilterInterface.onConfirmFilter(filterConditions);
                    dismiss();
                }

                break;
        }
    }

    @Override
    public void onConfirmDate(String date, int id) {
        if (id == inputMinDate.getId()) {
            inputMinDate.setText(date);
        } else if (id == inputMaxDate.getId()) {
            inputMaxDate.setText(date);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals("") && !s.toString().equals(".") && s.toString().equals(inputMaxPrice.getText().toString())) {
                filterConditions.setMaxPrice(Long.parseLong(s.toString()));
            } else{
                filterConditions.setMaxPrice(null);

            }
            if (!s.toString().equals("") && !s.toString().equals(".") && s.toString().equals(inputMinPrice.getText().toString())) {
                filterConditions.setMinPrice(Long.parseLong(s.toString()));

            } else {
                filterConditions.setMinPrice(null);

            }
            if (!s.toString().equals("") && s.toString().equals(inputMinDate.getText().toString())) {
                filterConditions.setMinDate(s.toString());

            } else{
                filterConditions.setMinDate(null);

            }
            if (!s.toString().equals("") && s.toString().equals(inputMaxDate.getText().toString())) {
                filterConditions.setMaxDate(s.toString());
            } else {
                filterConditions.setMaxDate(null);

            }

    }
    private boolean Valider() {
        boolean valide = true;
        String minprice = inputMinPrice.getText().toString();
        String maxprice = inputMaxPrice.getText().toString();
        String maxdate = inputMaxDate.getText().toString();
        String mindate = inputMinDate.getText().toString();


        if (!maxprice.isEmpty() && !minprice.isEmpty()
                && Long.parseLong(maxprice) <Long.parseLong(minprice) ) {
            layoutMinPrice.setError(" ");
            layoutMaxPrice.setError(" ");
            errorPrice.setVisibility(View.VISIBLE);
            valide = false;
        } else {
            layoutMinPrice.setError(null);
            layoutMaxPrice.setError(null);
            errorPrice.setVisibility(View.INVISIBLE);

        }
        try {
            if (!maxdate.isEmpty() && !mindate.isEmpty()
                    && new SimpleDateFormat("dd/MM/yyyy").parse(maxdate).compareTo( new SimpleDateFormat("dd/MM/yyyy").parse(mindate)) < 0) {
                layoutMinDate.setError(" ");
                layoutMaxDate.setError(" ");
                errorDate.setVisibility(View.VISIBLE);


                valide = false;
            } else {
                layoutMinDate.setError(null);
                layoutMaxDate.setError(null);
                errorDate.setVisibility(View.INVISIBLE);

            }
        }catch (ParseException ex){
            Log.d("TAG", "Validate : " +ex);
        }


        return valide;
    }

    @Override
    public void afterTextChanged(Editable s) {



    }
}
