package com.abelkin.mostcatcher.activities;

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
public class OnLongClickListenerLogin implements View.OnLongClickListener {

    Context context;
    String id;

    @Override
    public boolean onLongClick(View view) {

        context = view.getContext();
        id = view.getTag().toString();

        final CharSequence[] items = { "Edit", "Delete" };

        new AlertDialog.Builder(context).setTitle("Логин")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {
                            editRecord(Integer.parseInt(id));
                        }
                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    public void editRecord(final int loginId) {
        final LoginsController loginsController = new LoginsController(context);
        Login login = loginsController.readSingleRecord(loginId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.login_input_form, null, false);

        final EditText editTextLogin = (EditText) formElementsView.findViewById(R.id.login_input_form_login);
        final EditText editTextPassword = (EditText) formElementsView.findViewById(R.id.login_input_form_password);
        final EditText editTextPhone = (EditText) formElementsView.findViewById(R.id.login_input_form_phone);

        editTextLogin.setText(login.getLogin());
        editTextPassword.setText(login.getPassword());
        editTextPhone.setText(login.getPhone());

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Редактирование")
                .setPositiveButton("Сохранить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Login loginObj = new Login();
                                loginObj.setId(loginId);
                                loginObj.setLogin(editTextLogin.getText().toString());
                                loginObj.setPassword(editTextPassword.getText().toString());
                                loginObj.setPhone(editTextPhone.getText().toString());

                                boolean updateSuccessful = loginsController.update(loginObj);

                                if(updateSuccessful){
                                    Toast.makeText(context, "Успешно обновлено.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Ошибка при обновлении.", Toast.LENGTH_SHORT).show();
                                }

                                ((LoginsActivity) context).readRecords();

                                dialog.cancel();
                            }

                        }).show();
    }

}
