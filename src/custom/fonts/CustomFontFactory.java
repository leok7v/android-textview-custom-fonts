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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import static custom.fonts.util.trace;

public class CustomFontFactory implements LayoutInflater.Factory2 {

    private static final String DEFAULT_TYPEFACE = "baroque_script"; // default application font
    private static final String[] extensions = {"ttf", "otf"};
    private static final String[] classPrefixes = {"android.widget.", "android.webkit.", "android.view."};
    private static final Handler handler = new Handler();
    private final HashMap<String, Typeface> cache = new HashMap<String, Typeface>(16);
    private static CustomFontFactory instance;

    private CustomFontFactory() {
    }

    public static CustomFontFactory getInstance() {
        if (instance == null) {
            instance = new CustomFontFactory();
        }
        return instance;
    }

    public View setFontFamily(View view, Context context, AttributeSet attrs) {
        if (context.getTheme() == null) {
            trace("context.getTheme() == null");
        }
        if (view instanceof TextView && context.getTheme() != null) {
            final TextView v = (TextView)view;
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextView, 0, 0);
            if (a != null) {
                try {
                    String fontFamily = a.getString(R.styleable.TextView_fontFamily);
                    if (fontFamily == null) {
                        fontFamily = DEFAULT_TYPEFACE;
                    }
                    if (!v.isInEditMode() && !TextUtils.isEmpty(fontFamily)) {
                        final Typeface typeface = loadTypeface(context, fontFamily);
                        if (typeface != null) {
                            handler.post(new Runnable() {
                                public void run() {
                                    v.setTypeface(typeface);
                                }
                            });
                        }
                    }
                } finally {
                    a.recycle();
                }
            } else {
                trace("obtainStyledAttributes() == null");
            }
        }
        return view;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return createView(name, context, attrs);
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return createView(name, context, attrs);
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
