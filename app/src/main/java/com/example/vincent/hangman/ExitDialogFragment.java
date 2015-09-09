package com.example.vincent.hangman;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class ExitDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create the dialog.
        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());
        theDialog.setTitle(R.string.text_exit_dialog_title);
        theDialog.setMessage(R.string.text_exit_dialog_message);

        // The user
        theDialog.setPositiveButton(R.string.text_exit_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        theDialog.setNegativeButton(R.string.text_exit_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), R.string.text_exit_dialog_negative_2, Toast.LENGTH_SHORT).show();
            }
        });

        //return super.onCreateDialog(savedInstanceState);
        return theDialog.create();
    }
}
