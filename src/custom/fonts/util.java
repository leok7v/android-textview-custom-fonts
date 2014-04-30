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

import android.util.Log;

public class util {


    public static String getCallersCaller() {
        StackTraceElement st = Thread.currentThread().getStackTrace()[4];
        return forName(st.getClassName()).getSimpleName() + "." + st.getMethodName() +
                                ":" + st.getLineNumber() + " ";
    }

    public static Class<?> forName(String n) {
        try {
            return n == null ? null : Class.forName(n);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    public static void trace(String... params) {
        if (params != null && params.length > 0) {
            trace0(getCallersCaller(), params);
        }
    }


    public static void trace0(String caller, String... params) {
        if (params != null && params.length > 0) {
            StringBuilder sb = new StringBuilder(params[0].length() * 2);
            sb.append(caller).append(' ');
            for (String p : params) {
                if (p != null) {
                    sb.append(p).append(' ');
                }
            }
            String s = sb.toString().trim();
            Log.d("custom.font", s);
//          System.err.println(s);
        }
    }


}
