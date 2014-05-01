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

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import custom.fonts.*;

public class TitlesFragment extends CustomFontListFragment {

    private int index = 0;

    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
        if (savedState != null) {
            index = savedState.getInt("index", 0);
        }
        assert getActivity() != null;
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item_checked, Shakespeare.TITLES));
        onConfigurationChanged();
    }

    private boolean isDualPane() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void onConfigurationChanged() {
        if (getActivity() != null) {
            View detailsFrame = getActivity().findViewById(R.id.details);
            if (getListView() != null) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                if (isDualPane()) {
                    showDetails(index);
                } else {
                    detailsFrame.setVisibility(View.GONE);
                }
            }
        }
    }

    public void onConfigurationChanged(Configuration c) {
        super.onConfigurationChanged(c);
        onConfigurationChanged();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", index);
        super.onSaveInstanceState(outState);
    }

    public void onListItemClick(ListView l, View v, int pos, long id) {
        showDetails(pos);
    }

    private void showDetails(int index) {
        if (getListView() != null && getFragmentManager() != null) {
            this.index = index;
            if (isDualPane()) {
                getListView().setItemChecked(index, true);
                DetailsFragment details = (DetailsFragment)getFragmentManager().findFragmentById(R.id.details);
                if (details == null || details.getShownIndex() != index) {
                    details = DetailsFragment.newInstance(index);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.details, details);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            } else {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DetailsActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        }
    }

}