package com.corrot.room;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Date date;
    private PreferencesManager pm;

    private DatePickerDialog.OnDateSetListener dateListener;

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = View.inflate(getContext(), R.layout.dialog_add_weight, null);
        pm = PreferencesManager.getInstance();

        bodyWeightEditText = view.findViewById(R.id.dialog_new_weight_edit_text);
        dateTextView = view.findViewById(R.id.dialog_new_weight_text_view);
        ImageButton changeDateButton = view.findViewById(R.id.dialog_new_weight_button);

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
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();

        // This code is needed to override positive button listener to don't close dialog.
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String bodyWeightString = bodyWeightEditText.getText().toString();
                        if (bodyWeightString.isEmpty()) {
                            bodyWeightEditText.requestFocus();
                            bodyWeightEditText.setError("Please put body weight first!");
                        } else {
                            String dateString = MyTimeUtils.parseDate(date, MyTimeUtils.MAIN_FORMAT);
                            pm.addBodyWeight(bodyWeightString, dateString);
                            Toast.makeText(getContext(),
                                    "Body weight added",
                                    Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }
}
