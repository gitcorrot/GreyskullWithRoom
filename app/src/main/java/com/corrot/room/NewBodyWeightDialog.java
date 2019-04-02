package com.corrot.room;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corrot.room.utils.MyTimeUtils;
import com.corrot.room.utils.PreferencesManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NewBodyWeightDialog extends AppCompatDialogFragment {

    private TextView dateTextView;
    private EditText bodyWeightEditText;
    private ImageButton changeDateButton;
    private Date date;

    private DatePickerDialog.OnDateSetListener dateListener;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_weight, null);

        bodyWeightEditText = view.findViewById(R.id.dialog_new_weight_edit_text);
        dateTextView = view.findViewById(R.id.dialog_new_weight_text_view);
        changeDateButton = view.findViewById(R.id.dialog_new_weight_button);

        date = Calendar.getInstance().getTime();
        dateTextView.setText(MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT));

        changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 23) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            v.getContext(),
                            dateListener,
                            Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.show();
                }
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth()
                );
                date = calendar.getTime();
                dateTextView.setText(MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT));
            }
        };

        builder.setView(view)
                .setTitle("Add body weight")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: handle exceptions
                        String bodyWeight = bodyWeightEditText.getText().toString();
                        String dateString = MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT);
                        PreferencesManager.addBodyWeight(bodyWeight, dateString);

                        Log.d("asdasd", "ADDING " + bodyWeight + dateString);
                        Toast.makeText(getContext(),
                                "Body weight added",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
