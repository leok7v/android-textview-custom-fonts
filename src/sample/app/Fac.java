package sample.app;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import custom.fonts.*;

public class Fac extends Activity implements EditNameDialog.Ender { // Fragments Activity

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dialog: {
                showEditDialog();
                break;
            }
            case R.id.exit:
                finish();
                break;
            default: // unrecognized menu item
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        EditNameDialog editNameDialog = new EditNameDialog();
        editNameDialog.show(fm, "fragment_edit_name");
    }

    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }

}
