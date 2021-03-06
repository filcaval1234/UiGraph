/**
 * A classe HandlerMouseEvents encapsula os dados e os métodos necessarios para
 * o handler de eventos do mouse para o jogo emplementando a interface parametrizada
 * EventHandler de javafx.event.
 */
package handleEvents;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import uigraph.Maps;
import uigraph.Client;
import uigraph.ReceiveThread;
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
    private static int[] jogadas;
    private Label textLabel;
    private static Alert alertNoTickets;
    private Client client;
    private static Integer[] playeds;
    
    /**
     * O contrutor da classe que recebe argumentos necessarios para inicializar
     * os campos da mesma
     * @param lengthX distancia em pixels relativa ao eixo x
     * @param lengthY distancia em pixels relativa ao eixo y
     * @param tempGroup Group das estações de taxí do jogo
     * @param lenLine quantas estações ficaram na mesma linha
     * @param colorsOfStations lista das cores padrão das estações
     * @param lenGroup tamanho do grupo das estações de taxí
     * @param standartRadius raio para as estações de taxí
     * @param maps referencia ao mapa que foi criado em UiGraph
     */
    public HandlerMouseEvents(int lengthX, int lengthY, Group tempGroup, int lenLine,
            Color[] colorsOfStations, int lenGroup, int standartRadius, Maps maps, Label label, Client client){
        this.client = client;
        this.tempGroup = tempGroup;
        this.lengthX = lengthX;
        this.lengthY = lengthY;
        this.lenLine = lenLine;
        this.colorsOfStations = colorsOfStations;
        this.lenGroup = lenGroup;
        this.standartRadius = standartRadius;
        this.maps = maps;
        this.textLabel = label;
        //HandlerMouseEvents.playeds = new ArrayList<Integer>();
        HandlerMouseEvents.alertNoTickets = new Alert(Alert.AlertType.INFORMATION);
        HandlerMouseEvents.alertNoTickets.setHeaderText("Sem Passagens!!! :(");
    }
    /**
     * O método handle e nativo da interface HandlerEvent, então é há partir deste
     * método que executa-se o comportamento quando clica-se com o mouse em cima
     * de uma estação
     * @param event evento gerado que no caso é tipo MouseEvent
     */
    @Override
    public void handle(MouseEvent event) {
        Thread recerverThread = new Thread(new ReceiveThread(this.client, HandlerMouseEvents.playeds));
        recerverThread.start();
        double[] coordAproximate = this.approximateCoordinates(event.getSceneX(), event.getSceneY());
        int transformada =(int) this.transformacaoLinear(coordAproximate[0], coordAproximate[1], lenLine);
        //------------------ver isto-------------------------------
        
        //----------------------------------------------------------
        if(HandlerMouseEvents.iterador == 0){
            this.movimentation(transformada);
        }
        else if(this.maps.movimentation(HandlerMouseEvents.lastStation, transformada)){
            this.movimentation(transformada);
            int[] plays = this.maps.getPlays();
            textLabel.setText("passagens Taxi: "+plays[0]+"\n"
                    + "passagens trem: "+plays[1]+"\n"+
                    "passagens ônibus: "+plays[2]);
            //--------------e isto---------------------------------
            try{
                this.client.send((Integer)transformada);
            }catch(Exception ex){}
            //-----------------------------------------------------
            //this.showAdequateMessage(plays);
        }
        
    }
    //analisar esta função!!!!! DATTEBAYO!!!
    public void showAdequateMessage(int[] plays){
        final int INDEXTAXI = 0, INDEXTRAIN = 1, INDEXBUS = 2;
        if(plays[INDEXTAXI]==0){
            HandlerMouseEvents.alertNoTickets.setContentText("Você está sem passagens de TÁXI!!!");
            HandlerMouseEvents.alertNoTickets.showAndWait();
        }
        else if(plays[INDEXTRAIN] == 0){
            HandlerMouseEvents.alertNoTickets.setContentText("Você está sem passagens de TREM!!!");
            HandlerMouseEvents.alertNoTickets.showAndWait(); 
        }
        
        else if(plays[INDEXBUS] == 0){
            HandlerMouseEvents.alertNoTickets.setContentText("Você está sem passagens de ÔNIBUS!!!");
            HandlerMouseEvents.alertNoTickets.showAndWait();
        }
    }
    /**
     * O método movementation recebe um int que é uma tranformada linear da matriz
     * em vetor daí corrige a cor e o raio com o método returnedStandartColor
     * da estação passada e aumenta o raio para a estação atual.
     * @param transformada posição da estação na arvore de nos.
     */
    public void movimentation(int transformada){
        final int RADIUS = 8;
         try{
            Circle tempCircle =(Circle) tempGroup.getChildren().get(transformada);
            this.returnedStandartColor();
            tempCircle.setRadius(RADIUS);
            HandlerMouseEvents.lastStation = transformada;
            HandlerMouseEvents.iterador++;
        }catch(IndexOutOfBoundsException ioe){}
    }
    /**
     * O método approximateCoordinates recebe dois argumentos e daí aproxima as
     * coordenadas para multiplos de lenghtX e lengthY
     * @param x coordenada no eixo x
     * @param y coordenada no eixo y
     * @return retorna um vetor com as posições aproximadas para a transformada linear
     */
    public double[] approximateCoordinates(double x, double y){
        double[] coord = {this.aproximate(x)/this.lengthX,this.aproximate(y)/this.lengthY};
        //System.err.println(coord[0]+", "+ coord[1]);
        return coord;
    }
    /**
     * O método aproximate recebe como parâmetros uma coordenada daí aproxima ela
     * para um multiplo de 10
     * @param axis coordenada linear, ou seja apenas um eixo
     * @return retorna a coordenada já aproximada
     */
    public double aproximate(double axis){
        if(axis%10 > 5){
            axis += 10 - axis%10;
        }
        else{
            axis -= axis%10;
        }
        return axis;
    }
    /**
     * O método transformacaoLinear transforma uma acesso de matriz numa posição
     * do vetor
     * @param coordX coordenada x
     * @param coordY coordenada y
     * @param lenLine tamanho da linha da matriz
     * @return um double que será convertido num int que é a posição da estação 
     * em um vetor
     */
    public double transformacaoLinear(double coordX, double coordY, int lenLine){
        return (lenLine*coordY + coordX - lenLine)-1;
    }
    /**
     * O método returnedStandartColor retorna ao padão as cores de todas as estações
     * e o raio das mesmas.
     */
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
    public void showResearcher(){
        ArrayList<Integer> researcher = new ArrayList<>();
        final int RADIUS = 8;
        try{
            researcher = (ArrayList<Integer>) this.client.receive();
        }catch(Exception ex){}
        for(Integer position: researcher){
            Circle tempCircle = (Circle) this.tempGroup.getChildren().get(position);
            tempCircle.setRadius(RADIUS);
        }
    }
    public void AtualizaListView(int vertice){
        
    }
}
