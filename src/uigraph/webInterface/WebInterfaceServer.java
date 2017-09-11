/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uigraph.webInterface;

/**
 *
 * @author fc.corporation
 */
public interface WebInterfaceServer {
    
    public void sendPosition(Object obj);
    
    public void sendMaps(Object obj);
    
    public void sendCards(Object obj);
}
