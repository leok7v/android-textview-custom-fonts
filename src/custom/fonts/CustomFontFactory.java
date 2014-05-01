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

import android.app.*;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.*;
import android.widget.TextView;

import java.util.*;

import static custom.fonts.util.trace;

public class CustomFontFactory implements LayoutInflater.Factory2 {

    private static final String DEFAULT_FONT_FAMILY = "baroque_script"; // default application font
    private static final String[] extensions = {"ttf", "otf"};
    private static final String[] classPrefixes = {"android.widget.", "android.webkit.", "android.view."};
    private static final Handler handler = new Handler();
    private static CustomFontFactory instance;
    private final LinkedList<String> fontFamilies = new LinkedList<String>();
    private final HashMap<String, Typeface> cache = new HashMap<String, Typeface>(16);

    private CustomFontFactory() { }

    public static CustomFontFactory getInstance() {
        return instance != null ? instance : (instance = new CustomFontFactory());
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return createView(name, context, attrs);
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return createView(name, context, attrs);
    }

    public static void attach(Activity a) { attach(a.getLayoutInflater()); }

    public static void attach(Dialog d) { attach(d.getLayoutInflater()); }

    public static void attach(LayoutInflater li) {
        if (!(li.getFactory2() instanceof CustomFontFactory) && !(li.getFactory() instanceof CustomFontFactory)) {
            li.setFactory2(getInstance());
        }
    }

    public static void push(String defaultFontFamily) {
        getInstance().fontFamilies.addFirst(defaultFontFamily);
    }

    public static void pop() {
        getInstance().fontFamilies.removeFirst();
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View v = !name.contains(".") ? null : create(name, null, context, attrs);
        if (v == null) {
            for (String prefix : classPrefixes) {
                v = create(name, prefix, context, attrs);
                if (v != null) {
                    break;
                }
            }
        }
        if (v == null) {
            trace("failed to create " + name);
        } else {
            trace("created " + v.getClass().getCanonicalName());
        }
        return setFontFamily(v, context, attrs);
    }

    private static View create(String name, String prefix, Context context, AttributeSet attrs) {
        try {
            return LayoutInflater.from(context).createView(name, prefix, attrs);
        } catch (Throwable e) { // ClassNotFoundException
            return null;
        }
    }

    public View setFontFamily(final View v, final Context context, AttributeSet attrs) {
        if (context.getTheme() != null && v instanceof TextView && !v.isInEditMode()) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextView, 0, 0);
            try {
                final String ff = resolveFontFamily(
                        a != null ? a.getString(R.styleable.TextView_fontFamily) : null, context);
                final Typeface typeface = loadTypeface(context, ff);
                if (typeface != null) {
                    handler.post(new Runnable() {
                        public void run() { ((TextView)v).setTypeface(typeface); }
                    });
                }
            } finally {
                if (a != null) { a.recycle(); }
            }
        }
        return v;
    }

    private String resolveFontFamily(String ff, Context context) {
        if (ff == null && !fontFamilies.isEmpty()) {
            ff = fontFamilies.getFirst();
        }
        if (ff == null) {
            ff = context.getResources().getString(R.string.DEFAULT_FONT_FAMILY);
        }
        if (ff == null) {
            ff = DEFAULT_FONT_FAMILY;
        }
        return ff;
    }

    private Typeface loadTypeface(Context context, String fontFamily) {
        if (TextUtils.isEmpty(fontFamily)) {
            return null;
        }
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
