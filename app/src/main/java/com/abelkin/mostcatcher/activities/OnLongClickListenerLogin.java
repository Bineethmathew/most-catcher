package com.abelkin.mostcatcher.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.data.LoginController;
import com.abelkin.mostcatcher.models.Login;
import com.abelkin.mostcatcher.tasks.MainTask;

import java.util.List;

/**
 * Created by abelkin on 27.06.2017.
 */
public class OnLongClickListenerLogin implements AdapterView.OnItemLongClickListener {

    Context context;
    final List<Login> logins;

    public OnLongClickListenerLogin(List<Login> logins) {
        this.logins = logins;
    }

    public void editRecord(final long loginId) {
        final LoginController loginController = new LoginController(context);
        Login login = loginController.readSingleRecord(loginId);

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
                                loginObj.setId(Integer.parseInt(Long.toString(loginId)));
                                loginObj.setLogin(editTextLogin.getText().toString());
                                loginObj.setPassword(editTextPassword.getText().toString());
                                loginObj.setPhone(editTextPhone.getText().toString());

                                boolean updateSuccessful = loginController.update(loginObj);

                                MainTask.putLoginToHashMap(loginObj);

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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        context = view.getContext();
        final Login login = logins.get(position);

        final CharSequence[] items = { "Редактировать", "Удалить" };

        new AlertDialog.Builder(context).setTitle("Логин")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {
                            editRecord(login.getId());
                        } else if (item == 1) {

                            boolean deleteSuccessful = new LoginController(context).delete(login.getId());

                            if (deleteSuccessful){
                                Toast.makeText(context, "Запись была удалена.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Запись не удалось удалить.", Toast.LENGTH_SHORT).show();
                            }

                            ((LoginsActivity) context).readRecords();

                        }

                        dialog.dismiss();

                    }
                }).show();

        return false;
    }
}
