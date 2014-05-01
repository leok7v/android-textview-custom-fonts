package sample.app;

import android.os.*;
import android.text.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import custom.fonts.*;

public class EditNameDialog extends CustomFontDialogFragment implements TextView.OnEditorActionListener {

    public interface Ender {
        void onFinishEditDialog(String inputText);
    }

    private EditText editor;

    public EditNameDialog() {
        // Empty constructor required for DialogFragment
    }

    public View onCreateView(LayoutInflater li, ViewGroup container, Bundle savedInstanceState) {
        View view = li.inflate(R.layout.fragment_edit_name, container);
        if (view != null && getDialog() != null) {
            editor = (EditText) view.findViewById(R.id.txt_your_name);
            getDialog().setTitle("Hello");
            editor.requestFocus(); // Show soft keyboard automatically
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            editor.setOnEditorActionListener(this);
        }
        return view;
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId &&
            getActivity() instanceof Ender && editor != null) {
            // Return input text to activity
            Ender activity = (Ender) getActivity();
            Editable text = editor.getText();
            activity.onFinishEditDialog("" + text);
            this.dismiss();
            return true;
        }
        return false;
    }

}