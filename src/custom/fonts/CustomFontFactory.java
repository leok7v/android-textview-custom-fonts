/*  Copyright (c) 2014, Leo Kuznetsov
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this
      list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

    * Neither the name of the {organization} nor the names of its
      contributors may be used to endorse or promote products derived from
      this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
    FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
    DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
    CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
    OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
    OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package custom.fonts;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

public class CustomFontFactory implements LayoutInflater.Factory2 {

    private static final String[] extensions = {"ttf", "otf"};
    private static final String[] classPrefixes = {"android.widget.", "android.webkit."};
    private final HashMap<String, Typeface> cache = new HashMap<String, Typeface>(16);
    private final LayoutInflater inflater;

    public CustomFontFactory(Context context) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.setFactory2(this);
    }

    public LayoutInflater getLayoutInflater() {
        return inflater;
    }

    public View setFontFamily(View view, Context context, AttributeSet attrs) {
        if (view instanceof TextView) {
            TextView v = (TextView)view;
            String fontFamily = null;
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextView, 0, 0);
            try {
                fontFamily = a.getString(R.styleable.TextView_fontFamily);
                if (!v.isInEditMode() && !TextUtils.isEmpty(fontFamily)) {
                    Typeface typeface = loadTypeface(context, fontFamily);
                    if (typeface != null) {
                        v.setTypeface(typeface);
                    }
                }
            } finally {
                a.recycle();
            }
        }
        return view;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return createView(parent, name, context, attrs);
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return createView(null, name, context, attrs);
    }

    private View createView(View parent, String name, Context context, AttributeSet attrs) {
        View v = !name.contains(".") ? null : createView(inflater, name, null, attrs);
        if (v == null) {
            for (String prefix : classPrefixes) {
                v = createView(inflater, name, prefix, attrs);
                if (v != null) {
                    break;
                }
            }
        }
        return setFontFamily(v, context, attrs);
    }

    private static View createView(LayoutInflater inflater, String name, String prefix, AttributeSet attrs) {
        try {
            return inflater.createView(name, prefix, attrs);
        } catch (Throwable e) { // ClassNotFoundException
            return null;
        }
    }

    private Typeface loadTypeface(Context context, String fontFamily) {
        Typeface typeface = cache.get(fontFamily);
        if (typeface == null) {
            for (String ext : extensions) {
                try {
                    typeface = Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s.%s", fontFamily, ext));
                } catch (Throwable t) {
                    // ignore
                }
            }
        }
        if (typeface != null) {
            cache.put(fontFamily, typeface);
        }
        return typeface;
    }

}
