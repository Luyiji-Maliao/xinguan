package com.usci.xgcollectinfo.entity;

import javax.persistence.*;

@Entity
@Table(name = "fiveoneinfo")
public class Fiveoneinfo {
    private Integer id;
    private String fiveonenum;
    private String isruku;
    private String rukutime;
    private String rukuname;
    private String inputtime;
    private String inputname;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "fiveonenum", length = 55)
    public String getFiveonenum() {
        return fiveonenum;
    }

    public void setFiveonenum(String fiveonenum) {
        this.fiveonenum = fiveonenum;
    }

    @Column(name = "isruku", length = 55)
    public String getIsruku() {
        return isruku;
    }

    public void setIsruku(String isruku) {
        this.isruku = isruku;
    }

    @Column(name = "rukutime", length = 55)
    public String getRukutime() {
        return rukutime;
    }

    public void setRukutime(String rukutime) {
        this.rukutime = rukutime;
    }

    @Column(name = "rukuname", length = 55)
    public String getRukuname() {
        return rukuname;
    }

    public void setRukuname(String rukuname) {
        this.rukuname = rukuname;
    }
    @Column(name = "inputtime", length = 55)
    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }
    @Column(name = "inputname", length = 55)
    public String getInputname() {
        return inputname;
    }

    public void setInputname(String inputname) {
        this.inputname = inputname;
    }
}
