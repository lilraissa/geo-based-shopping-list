package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import shoppinglist.de.fh_dortmund.com.shoppinglist.R;

public class AddListUserInputDialogActivity extends AppCompatActivity {
    private TextView textView;
    final Context c = this;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_list);

        textView= (TextView) findViewById(R.id.addList_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_create_list, null);
               final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                            }
                        })

                        .setNegativeButton("Abbrechen",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

    }
}
