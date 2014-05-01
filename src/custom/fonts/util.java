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

import java.io.*;

public class util {

    public static String getCallersCaller(int n) {
        StackTraceElement st = Thread.currentThread().getStackTrace()[n];
        return forName(st.getClassName()).getSimpleName() + "." + st.getMethodName() +
                                ":" + st.getLineNumber() + " ";
    }

    public static String getCallersPackage(int n) {
        StackTraceElement st = Thread.currentThread().getStackTrace()[n];
        return forName(st.getClassName()).getPackage().getName();
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
            trace0(getCallersCaller(4), getCallersPackage(4), params);
        }
    }

    public static void trace0(String callerClass, String callerPackage, String... params) {
        if (params != null && params.length > 0) {
            StringBuilder sb = new StringBuilder(params[0].length() * 2);
            sb.append(callerClass).append(' ');
            for (String p : params) {
                if (p != null) {
                    sb.append(p).append(' ');
                }
            }
            String s = sb.toString().trim();
            Log.d(callerPackage, s);
        }
    }

    private static String lineSeparator = System.getProperty("line.separator");
    private static int lineSeparatorChar = lineSeparator.charAt(0);

    public static void redirectSystemStreams() {
        if (lineSeparator.length() != 1) {
            throw new Error("it might not work on Windows CR/LF");
        }
        System.setOut(new LogPrintStream(new LogOutputStream(), System.out));
        System.setErr(new LogPrintStream(new LogOutputStream(), System.err));
        System.err.println("Hello World");
        System.err.println("System.err=" + System.err.hashCode());
    }

    private static class LogPrintStream extends PrintStream {

        private PrintStream second;

        public LogPrintStream(OutputStream out, PrintStream was) {
            super(out);
            second = was;
        }

        public void write(int ch)  {
            super.write(ch);
            second.write(ch);
        }

    }

    private static class LogOutputStream extends OutputStream {

        private CharArrayWriter saw = new CharArrayWriter(1024);

        public LogOutputStream() { }

        public void write(int ch) throws IOException {
            if (ch != lineSeparatorChar) {
                saw.write(ch);
            } else {
                int n = 4; // variable depth of calls inside java.io.* packages
                StackTraceElement[] s = Thread.currentThread().getStackTrace();
                for (int i = n; i < s.length; i++) {
                    if ("java.io".equals(forName(s[i-1].getClassName()).getPackage().getName()) &&
                       !"java.io".equals(forName(s[i].getClassName()).getPackage().getName())) {
                        n = i;
                        break;
                    }
                }
                StackTraceElement st = Thread.currentThread().getStackTrace()[n];
                Class cls = forName(st.getClassName());
                String pkg = cls.getPackage().getName();
                String caller = cls.getSimpleName() + "." + st.getMethodName() + ":" + st.getLineNumber() + " ";
                trace0(caller, pkg, saw.toString());
                saw.reset();
            }
        }

    }

}
