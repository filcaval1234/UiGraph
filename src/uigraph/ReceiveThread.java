/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uigraph;

import java.util.ArrayList;

/**
 *
 * @author fc.corporation
 */
public class ReceiveThread implements Runnable{
    Client client;
    Integer[] referenceArray;
    public ReceiveThread (Client client, Integer[] reference){
        this.client = client;
        this.referenceArray = reference;
    }
    @Override
    public void run() {
        while(true){
            try {
                this.referenceArray =(Integer[]) this.client.receive();
                for(int i: referenceArray){
                    System.err.println(i);
                }
            } catch (Exception e) {}
        }
    }
}
