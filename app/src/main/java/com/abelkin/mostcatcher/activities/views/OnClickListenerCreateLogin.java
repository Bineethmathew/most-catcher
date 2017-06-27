package com.abelkin.mostcatcher.activities.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.abelkin.mostcatcher.R;

/**
 * Created by abelkin on 27.06.2017.
 */
public class OnClickListenerCreateLogin implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        Context context = view.getRootView().getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.login_input_form, null, false);

        final EditText editTextLogin = (EditText) formElementsView.findViewById(R.id.login_input_form_login);
        final EditText editTextPassword = (EditText) formElementsView.findViewById(R.id.login_input_form_password);
        final EditText editTextPhone = (EditText) formElementsView.findViewById(R.id.login_input_form_phone);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Добавить логин")
                .setPositiveButton("Добавить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }

                        }).show();
    }
}