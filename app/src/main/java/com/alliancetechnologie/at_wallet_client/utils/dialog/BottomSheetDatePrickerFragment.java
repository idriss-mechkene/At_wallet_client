package com.alliancetechnologie.at_wallet_client.utils.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alliancetechnologie.at_wallet_client.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetDatePrickerFragment extends BottomSheetDialogFragment {

    private static final String ARG_PARAM = "DATE_PICKER";
    private onDatePickerInterface datePickerInterface;
    DatePicker datePicker;
    Button btnValidate;
    int IdInputDate;

    public BottomSheetDatePrickerFragment() {
    }

    public void setDatePickerInterface(onDatePickerInterface datePickerInterface) {
        this.datePickerInterface = datePickerInterface;
    }

    public static BottomSheetDatePrickerFragment newInstance(int IdInputDate) {
        BottomSheetDatePrickerFragment fragment = new BottomSheetDatePrickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, IdInputDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            IdInputDate = getArguments().getInt(ARG_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_date, container, false);
        datePicker = view.findViewById(R.id.date_picker);
        btnValidate = view.findViewById(R.id.btn_valider);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Log.e("TAG", "onCreateView: " + calendar.getTime());
        datePicker.setMaxDate(new Date().getTime());

        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 5);
        Log.e("TAG", "onCreateView: " + calendar.getTime());
        datePicker.setMinDate(calendar.getTimeInMillis());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnValidate.setOnClickListener(v -> {
            String dateSelected = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth()+1 )+ "/" + datePicker.getYear();
            datePickerInterface.onConfirmDate(dateSelected, IdInputDate);
            dismiss();
        });
    }
}
