package com.creative_webstudio.iba.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.view.View;

import com.creative_webstudio.iba.R;

import org.mmtextview.components.MMProgressDialog;
import org.mmtextview.components.MMTextView;


public class CustomDialog {

    public static void messageDialog(Context context,String title,String message,Runnable function,Boolean useTwoBtn){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        if(useTwoBtn == true) {
            builder.setPositiveButton("Yes", (dialog, which) -> {
                function.run();
                dialog.dismiss();
            });
            builder.setPositiveButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
        }
        builder.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog productDialog = builder.create();
        productDialog.show();
    }

    public static MMProgressDialog loadingDialog(Context context,String title,String message){
        MMProgressDialog progressDialog = new MMProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static AlertDialog loadingDialog2(Context context,String title,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View inflate_dialog = AlertDialogLayout.inflate(context,R.layout.dialog_loading,null);
        MMTextView tvMessage = inflate_dialog.findViewById(R.id.loadingMessage);
        tvMessage.setText(message);
        builder.setTitle(title);
        builder.setView(inflate_dialog);
        AlertDialog productDialog = builder.create();
        productDialog.setCanceledOnTouchOutside(false);
        return productDialog;
    }

}
