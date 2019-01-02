package com.creative_webstudio.iba.datas.criterias;

import com.creative_webstudio.iba.datas.vos.CartVO;

import java.util.List;

public class OrderCriteria {

    private List<CartVO> cartVOS;

    public List<CartVO> getCartVOS() {
        return cartVOS;
    }

    public void setCartVOS(List<CartVO> cartVOS) {
        this.cartVOS = cartVOS;
    }
}
