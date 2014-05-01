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
package sample.app;

import android.app.AlertDialog;
import android.content.*;
import android.view.*;
import custom.fonts.*;

import static custom.fonts.util.*;

public class Act extends BaseActivity {

    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirectSystemStreams();
        setContentView(R.layout.main);
        View v = findViewById(R.id.button);
        if (v != null) {
            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CustomFontFactory.push("baroque_script");
                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Act.this);
                        builder.setTitle("Title");
                        builder.setMessage("Alert Message");
                        builder.setPositiveButton("OK", null);
                        builder.create();
                        builder.show();
                    } finally {
                        CustomFontFactory.pop();
                    }
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragments: {
                Intent intent = new Intent();
                intent.setClass(this, Fac.class);
                startActivity(intent);
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

}
