package com.abelkin.mostcatcher.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.abelkin.mostcatcher.R;
import com.abelkin.mostcatcher.data.CityController;
import com.abelkin.mostcatcher.models.City;

/**
 * Created by abelkin on 28.06.2017.
 */
public class OnLongClickListenerCity implements View.OnLongClickListener {

    private Context context;
    private String id;

    @Override
    public boolean onLongClick(View view) {

        context = view.getContext();
        id = view.getTag().toString();

        final CharSequence[] items = { "Отметить в фильтре"};

        new AlertDialog.Builder(context).setTitle("Город")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        editRecord(Integer.parseInt(id));

                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    public void editRecord(final int cityId) {
        final CityController cityController = new CityController(context);
        City city = cityController.readSingleRecord(cityId);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.city_input_form, null, false);

        final TextView textViewName = (TextView) formElementsView.findViewById(R.id.city_input_form_name);
        final CheckBox checkBoxFrom = (CheckBox) formElementsView.findViewById(R.id.city_input_form_from_check);
        final CheckBox checkBoxTo = (CheckBox) formElementsView.findViewById(R.id.city_input_form_to_check);

        textViewName.setText(city.getName());
        checkBoxFrom.setChecked(city.getFromChecked() == 1);
        checkBoxTo.setChecked(city.getToChecked() == 1);

        new AlertDialog.Builder(context)
                .setView(formElementsView)
                .setTitle("Редактирование")
                .setPositiveButton("Сохранить",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                City cityObj = new City();
                                cityObj.setId(cityId);
                                cityObj.setFromChecked(checkBoxFrom.isChecked() ? 1 : 0);
                                cityObj.setToChecked(checkBoxTo.isChecked() ? 1 : 0);

                                boolean updateSuccessful = cityController.update(cityObj);

                                if(updateSuccessful){
                                    Toast.makeText(context, "Успешно обновлено.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Ошибка при обновлении.", Toast.LENGTH_SHORT).show();
                                }

                                ((CitiesActivity) context).readRecords();

                                dialog.cancel();
                            }

                        }).show();
    }

}
