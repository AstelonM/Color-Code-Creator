package com.mihai.colorcodecreator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FabDialogFragment extends DialogFragment {

    private TextView fabTarget;
    private TextView fabCod;
    private EditText fabInput;

    interface FabDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String input);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    FabDialogListener fabListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_fabdialog, null);
        builder.setView(v);
        fabCod = (TextView) v.findViewById(R.id.fabCod);
        fabInput = (EditText) v.findViewById(R.id.fabInput);
        fabTarget = (TextView) v.findViewById(R.id.fabTarget);
        builder.setPositiveButton(R.string.da, (dialog, id) -> fabListener.onDialogPositiveClick(FabDialogFragment.this, fabInput.getText().toString()));
        builder.setNegativeButton(R.string.nu, (dialog, id) -> fabListener.onDialogNegativeClick(FabDialogFragment.this));
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle b = this.getArguments();
        int culoare = b.getInt(MainActivity.FAB_CULOARE);
        String cod = b.getString(MainActivity.FAB_COD);
        fabTarget.setBackgroundColor(culoare);
        fabCod.setText(cod);
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fabListener = (FabDialogListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

}
