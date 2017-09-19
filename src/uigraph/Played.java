/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uigraph;

import java.io.Serializable;

/**
 *
 * @author fc.corporation
 */
@SuppressWarnings("serial")
public class Played implements Serializable{
    static final long serialVersionUID = 123456;
    private Integer posicao;
    public Played(Integer posicao){
        this.posicao = posicao;
    }
    public String toString(){
        return this.posicao+"";
    }
}
