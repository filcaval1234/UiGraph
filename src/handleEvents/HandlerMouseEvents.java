/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handleEvents;
import java.util.Iterator;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import uigraph.Maps;
/**
 *
 * @author fc.corporation
 */
public class HandlerMouseEvents implements EventHandler<MouseEvent>{
    private final Group tempGroup;
    private final int lengthX;
    private final int lengthY;
    private final int lenLine;
    private final Color[] colorsOfStations;
    private final int lenGroup;
    private final int standartRadius;
    private final Maps maps;
    private static int lastStation;
    private static int iterador = 0;
    
    public HandlerMouseEvents(int lengthX, int lengthY, Group tempGroup, int lenLine,
            Color[] colorsOfStations, int lenGroup, int standartRadius, Maps maps){
        this.tempGroup = tempGroup;
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.lenLine = lenLine;
        this.colorsOfStations = colorsOfStations;
        this.lenGroup = lenGroup;
        this.standartRadius = standartRadius;
        this.maps = maps;
    }
    @Override
    public void handle(MouseEvent event) {
        double[] coordAproximate = this.approximateCoordinates(event.getSceneX(), event.getSceneY());
        int transformada =(int) this.transformacaoLinear(coordAproximate[0], coordAproximate[1], lenLine);
        if(HandlerMouseEvents.iterador == 0){
            System.err.println(HandlerMouseEvents.lastStation+"----"+transformada);
            System.out.println(this.maps.movimentation(HandlerMouseEvents.lastStation, transformada));
            this.movimentation(transformada);
        }
        else if(this.maps.movimentation(HandlerMouseEvents.lastStation, transformada)){
            this.movimentation(transformada);
        }
    }
    public void movimentation(int transformada){
         try{
            Circle tempCircle =(Circle) tempGroup.getChildren().get(transformada);
            this.returnedStandartColor();
            tempCircle.setFill(Color.PINK);
            tempCircle.setRadius(8);
            HandlerMouseEvents.lastStation = transformada;
            HandlerMouseEvents.iterador++;
        }catch(IndexOutOfBoundsException ioe){}
    }
    public double[] approximateCoordinates(double x, double y){
        double[] coord = {this.aproximate(x)/this.lengthX,this.aproximate(y)/this.lengthY};
        //System.err.println(coord[0]+", "+ coord[1]);
        return coord;
    }
    public double aproximate(double axis){
        if(axis%10 > 5){
            axis += 10 - axis%10;
        }
        else{
            axis -= axis%10;
        }
        return axis;
    }
    public double transformacaoLinear(double x, double y, int lenLine){
        return (lenLine*y + x - lenLine)-1;
    }
    public void returnedStandartColor(){
        final int CASE0 = 2;//caso resultante que há ligações de ônibus
        final int CASE1 = 0;//caso resultante que não há nemhuma ligação
        final int CASE2 = 1;//caso resultante que há ligações de trem
        for(int i=0;i < lenGroup;i++){
            Circle tempCircle =(Circle) this.tempGroup.getChildren().get(i);
            int result = this.maps.incideArestaInStationBusOrTrain(i);
            switch(result){
                case CASE0: tempCircle.setFill(this.colorsOfStations[0]);
                    tempCircle.setRadius(this.standartRadius);
                    break;
                case CASE1: tempCircle.setFill(this.colorsOfStations[1]); 
                    tempCircle.setRadius(this.standartRadius);
                    break;
                case CASE2: tempCircle.setFill(this.colorsOfStations[2]); 
                    tempCircle.setRadius(this.standartRadius);
                    break;
            }
        }
    }
}
