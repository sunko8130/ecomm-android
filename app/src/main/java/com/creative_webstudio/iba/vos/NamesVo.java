package com.creative_webstudio.iba.vos;


import java.util.ArrayList;
import java.util.List;

public class NamesVo {
    private String name;
    private List<NamesVo> names = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NamesVo> getNames() {
        return names;
    }

    public void setNames(List<NamesVo> names) {
        this.names = names;
    }

    private NamesVo() {

    }

    public NamesVo(String hello) {
        NamesVo vo1 = new NamesVo();
        vo1.setName("Ao Co Coffee");

        NamesVo vo2 = new NamesVo();
        vo2.setName("Elorets");

        NamesVo vo3 = new NamesVo();
        vo3.setName("Clorets");

        NamesVo vo4 = new NamesVo();
        vo4.setName("Dlorets");

        NamesVo vo5 = new NamesVo();
        vo5.setName("Blorets");

        NamesVo vo6 = new NamesVo();
        vo6.setName("Clorets");

        NamesVo vo7 = new NamesVo();
        vo7.setName("florets");
        names.add(vo1);
        names.add(vo2);
        names.add(vo3);
        names.add(vo4);
        names.add(vo5);
        names.add(vo6);
        names.add(vo7);
    }


}
