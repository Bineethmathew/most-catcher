package com.abelkin.mostcatcher.activities.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.data.LoginsController;
import com.abelkin.mostcatcher.models.Login;

/**
 * Created by abelkin on 27.06.2017.
 */
public class OnClickListenerCreateLogin implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        final Context context = view.getRootView().getContext();

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

                                String login = editTextLogin.getText().toString();
                                String password = editTextPassword.getText().toString();
                                String phone = editTextPhone.getText().toString();

                                Login loginObj = new Login();
                                loginObj.setLogin(login);
                                loginObj.setPassword(password);
                                loginObj.setPhone(phone);

                                boolean createSuccessful = new LoginsController(context).create(loginObj);

                                if(createSuccessful){
                                    Toast.makeText(context, "Логин добавлен.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Информация о логине не добавлена.", Toast.LENGTH_SHORT).show();
                                }

                                dialog.cancel();
                            }

                        }).show();
    }
}