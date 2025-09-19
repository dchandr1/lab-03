package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    public interface AddCityDialogListener {
        void onAddCity(City city);
        void onEditCity(int position, String newName, String newProvince);
    }

    private static final String ARG_CITY     = "city";
    private static final String ARG_POSITION = "position";

    private AddCityDialogListener listener;

    public static AddCityFragment newInstance() {
        return new AddCityFragment();
    }

    public static AddCityFragment newInstance(City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CITY, city);      // City must implement Serializable
        args.putInt(ARG_POSITION, position);
        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_add_city, null);
        EditText editCityName     = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        Bundle args     = getArguments();
        boolean isEdit  = args != null && args.containsKey(ARG_CITY);
        int position    = isEdit ? args.getInt(ARG_POSITION) : -1;
        City editTarget = isEdit ? (City) args.getSerializable(ARG_CITY) : null;

        if (isEdit && editTarget != null) {
            editCityName.setText(editTarget.getName());
            editProvinceName.setText(editTarget.getProvince());
        }

        AlertDialog.Builder b = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(isEdit ? "Edit city" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEdit ? "OK" : "Add", (d, which) -> {
                    String name = editCityName.getText().toString().trim();
                    String prov = editProvinceName.getText().toString().trim();
                    if (isEdit) {
                        listener.onEditCity(position, name, prov);
                    } else {
                        listener.onAddCity(new City(name, prov));
                    }
                });

        return b.create();
    }
}
