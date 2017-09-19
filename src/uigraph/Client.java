/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uigraph;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
public class Client {
    Socket client;
    public Client(String IP, int PORT) throws IOException, ClassNotFoundException {
        this.client = new Socket(IP, PORT); 
    }
    public void send(Object object) throws IOException, ClassNotFoundException {
        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        output.flush(); 
        output.writeObject(object); 
        output.flush();
    }
    public Serializable receive()throws IOException, ClassNotFoundException{
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());
        return (Serializable)input.readObject();

    }
}
