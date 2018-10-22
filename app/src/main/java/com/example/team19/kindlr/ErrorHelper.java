package com.example.team19.kindlr;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorHelper {

    public static void displayError(String title, String msg, Context context) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

}
