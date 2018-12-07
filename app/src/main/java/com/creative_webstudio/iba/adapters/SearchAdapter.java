package com.creative_webstudio.iba.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;


import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductSearchDelegate;
import com.creative_webstudio.iba.vieholders.SearchViewHolder;
import com.creative_webstudio.iba.datas.vos.NamesVo;

public class SearchAdapter extends BaseRecyclerAdapter<SearchViewHolder, ProductVO> {

    private ProductSearchDelegate mproductDelegate;


    public SearchAdapter(Context context, ProductSearchDelegate productDelegate) {
        super(context);
        this.mproductDelegate = productDelegate;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mLayoutInflator.inflate(R.layout.view_holder_search_list, parent, false);
        return new SearchViewHolder(view, mproductDelegate);
    }

}
