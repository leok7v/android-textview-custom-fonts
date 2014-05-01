package custom.fonts;

import android.app.*;
import android.os.*;
import android.util.*;

public class CustomFontListFragment extends ListFragment {

    public void onInflate(Activity a, AttributeSet attrs, Bundle savedInstanceState) {
        CustomFontFactory.attach(a);
        super.onInflate(a, attrs, savedInstanceState);
    }

    public void onAttach(Activity a) {
        CustomFontFactory.attach(a);
        super.onAttach(a);
    }

}
