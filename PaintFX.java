
import static com.sun.javafx.application.PlatformImpl.addListener;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.util.Duration;


public class PaintFX extends Application 
{
    Group group =  new Group(); //group olio johon kiinnitetään muut oliot
  
    
    Canvas canvas = new Canvas(1000, 600); //piirtoalue sama kuin ikkunan koko
    GraphicsContext piirto = canvas.getGraphicsContext2D();    
    
    double aloituspiste_x, aloituspiste_y ;
    
    Rectangle uusi_suorakaide = null ;
    Circle uusi_ympyra = null;
    Line viiva = null;
    boolean piirretaan = false ;
    
    static final Point2D ylapiste  =  new  Point2D( 0, 50 ) ;
    static final int leveys =  1000, korkeus  =  600 ; 
           
    
    //suorakaiteen määrittely; paikka ja koko
    void suorakaiteen_ominaisuudet( double aloituspiste_x,
                                     double aloituspiste_y,
                                     double lopetuspiste_x,
                                     double lopetuspiste_y,
                                     Rectangle piirretty )
   {
      piirretty.setX( aloituspiste_x ) ;
      piirretty.setY( aloituspiste_y ) ;
      piirretty.setWidth( lopetuspiste_x - aloituspiste_x ) ;
      piirretty.setHeight( lopetuspiste_y - aloituspiste_y ) ;

      if ( piirretty.getWidth() < 0 )
      {
         piirretty.setWidth( - piirretty.getWidth() ) ;
         piirretty.setX( piirretty.getX() - piirretty.getWidth() ) ;
      }

      if ( piirretty.getHeight() < 0 )
      {
         piirretty.setHeight( - piirretty.getHeight() ) ;
         piirretty.setY( piirretty.getY() - piirretty.getHeight() ) ;
      }
   }
    
    //ympyrän ominaisuuksien määrittely
    void ympyran_ominaisuudet( double aloituspiste_x,
                                     double aloituspiste_y,
                                     double lopetuspiste_x,
                                     double lopetuspiste_y,
                                     Circle piirretty )
    {
      piirretty.setCenterX( aloituspiste_x ) ;
      piirretty.setCenterY( aloituspiste_y ) ;
      piirretty.setRadius( lopetuspiste_x - aloituspiste_x +  lopetuspiste_y - aloituspiste_y) ;
      
      if ( piirretty.getRadius() < 0 )
      {
         piirretty.setRadius( - piirretty.getRadius() ) ;
         piirretty.setCenterX( piirretty.getCenterX() - piirretty.getRadius() ) ;
      }

    }
    
    //suoran viivan koko ja paikka
    void viivan_ominaisuudet( double aloituspiste_x,
                                     double aloituspiste_y,
                                     double lopetuspiste_x,
                                     double lopetuspiste_y,
                                     Line piirretty )
   {
      piirretty.setStartX( aloituspiste_x ) ;
      piirretty.setStartY( aloituspiste_y ) ;
      piirretty.setEndX( lopetuspiste_x - aloituspiste_x ) ;
      piirretty.setEndY( lopetuspiste_y - aloituspiste_y ) ;

   }
    
     
    public void start(Stage stage){      
             
                    
        stage.setTitle("Paint");//otsikko ikkunalle
        
        Scene scene = new Scene(group,  1000, 600);//scenen eli ikkunan koko
        scene.setFill( Color.WHITE );//taustavari
                
        //taivaan piirto
        Rectangle taivas = new Rectangle( ylapiste.getX(),
                                          ylapiste.getY(),
                                          leveys, korkeus / 3 ) ;
        
        taivas.setFill( Color.LIGHTSKYBLUE ) ;
        
        //meren piirto
        Rectangle meri = new Rectangle( ylapiste.getX(),
                         ylapiste.getY() + korkeus / 3,
                         leveys, korkeus/ 3 ) ;

        meri.setFill( Color.BLUE);
        
        //hiekan piirto
        Rectangle hiekka = new Rectangle( ylapiste.getX(),
                                          ylapiste.getY() + korkeus / 2,
                                          leveys, korkeus / 2 );
        hiekka.setFill(Color.SANDYBROWN);      
        
        //auringon piirto
        Circle aurinko = new Circle( ylapiste.getX() + 50,
                                      ylapiste.getY() + 60,
                                      50, Color.GOLD ) ;
        
        //auringon efekti ettei ole niin tarkkarajainen
        aurinko.setEffect(new BoxBlur(10, 10, 3));
        
        //hain piirto
        Polygon kolmio = new Polygon();
        kolmio.getPoints().addAll(new Double[]{
            570.0, 300.0,
            550.0, 300.0,
            555.0, 285.0 });
        kolmio.setStroke(Color.DARKGREY);
        kolmio.setFill(Color.DARKGREY);      
        
        piirto = canvas.getGraphicsContext2D();
        piirto.setStroke(Color.BLACK);
        piirto.setLineWidth(2);
                        
        Label label_viivan = new Label();
        label_viivan.setText("Viiva:");
        
        Label label_tayton = new Label();
        label_tayton.setText("Taytto:");
        
        Button button = new Button();
        button.setText("Suorakaide");
        //button.setLayoutX(20);
        //button.setLayoutY(10);
        
        Button button_ympyra = new Button();
        button_ympyra.setText("Ympyra");
        //button_ympyra.setLayoutX(100);
        //button_ympyra.setLayoutY(10);   
        
        Button button_kyna = new Button();
        button_kyna.setText("Kyna");
        //button_kyna.setLayoutX(100);
        //button_kyna.setLayoutY(10);
        
        Button button_tyhjenna = new Button();
        button_tyhjenna.setText("Tyhjenna");
        
        Button button_animaatio = new Button();
        button_animaatio.setText("Animaatio");
        
       Button button_viiva = new Button();
       button_viiva.setText("Viiva");
                        
        //väripaletin lisäys viivan varille, alussa musta oletuksena
        ColorPicker valitse_vari = new ColorPicker();        
        valitse_vari.setValue(Color.BLACK);
        
        //väripaletin lisäys taytto varille, alussa musta oletuksena
        ColorPicker tayton_vari = new ColorPicker();        
        tayton_vari.setValue(Color.BLACK);
        
        //kynän viivan vahvuus sliderilla
        Slider slider = new Slider();
        slider.setMin(1);
        slider.setMax(100);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        Label vahvuus = new Label();//label viivan vahvuudelle
        slider.valueProperty().addListener(e ->
        {
            double arvo = slider.getValue();
            String str = String.format("%.1f", arvo);
            vahvuus.setText(str);
            piirto.setLineWidth(arvo);
        });                
        
        //työkalupalkin kiinnitys
        GridPane kiinnitys = new GridPane();
        kiinnitys.addRow(0, button_kyna, button_viiva, button, button_ympyra, slider, vahvuus, label_viivan, valitse_vari, label_tayton, tayton_vari, button_animaatio, button_tyhjenna);
        kiinnitys.setHgap(10);
        kiinnitys.setAlignment(Pos.TOP_CENTER);
        kiinnitys.setPadding(new Insets(10, 0, 0, 0));
        
        //hiirellä piirtäminen on oletuksena
        scene.setOnMousePressed( ( MouseEvent event ) ->
        {                
            piirto.setStroke( valitse_vari.getValue() ) ;
            piirto.beginPath();
            piirto.moveTo(event.getX(), event.getY());
            piirto.stroke();
                  
        });

        scene.setOnMouseDragged( ( MouseEvent event ) ->
        {            
            piirto.lineTo(event.getX(), event.getY());
            piirto.stroke();
            
        });
                  
        scene.setOnMouseReleased( ( MouseEvent event ) ->
        {
                
        }) ;                   
        
        //hiirella piirtaminen napista
        button_kyna.setOnAction (e->
        { 
            scene.setOnMousePressed( ( MouseEvent event ) ->
            {     
                piirto.setStroke( valitse_vari.getValue() ) ;
                piirto.beginPath();
                piirto.moveTo(event.getX(), event.getY());
                piirto.stroke();    
                piirretaan = true;
            });

            scene.setOnMouseDragged( ( MouseEvent event ) ->
            {       
                    
                piirto.lineTo(event.getX(), event.getY());
                piirto.stroke();    
                    
            });
                  
            scene.setOnMouseReleased( ( MouseEvent event ) ->
            {
                
            }) ;  
        });
        
        //suorakaiteen piirtäminen napista
        button.setOnAction (e->
        {            
             scene.setOnMousePressed( ( MouseEvent event ) ->
            {
                if ( piirretaan == false )
                {
                    aloituspiste_x = event.getSceneX() ;
                    aloituspiste_y = event.getSceneY() ;

                    uusi_suorakaide = new Rectangle() ;
                    
                    uusi_suorakaide.setStroke( valitse_vari.getValue() ) ;
                    uusi_suorakaide.setFill( tayton_vari.getValue() ) ; 

                    group.getChildren().add( uusi_suorakaide ) ;

                    piirretaan = true ;
                }
            });

            scene.setOnMouseDragged( ( MouseEvent event ) ->
            {
                if ( piirretaan == true )
                {
                    double nykyinen_lopetuspiste_x = event.getSceneX() ;
                    double nykyinen_lopetuspiste_y = event.getSceneY() ;

                    suorakaiteen_ominaisuudet( aloituspiste_x,
                                                 aloituspiste_y,
                                                 nykyinen_lopetuspiste_x,
                                                 nykyinen_lopetuspiste_y,
                                                 uusi_suorakaide ) ;
                }
            });
            
      
            scene.setOnMouseReleased( ( MouseEvent event ) ->
            {
                if ( piirretaan == true )
                {            
                    uusi_suorakaide = null ;
                    piirretaan = false ;
                }
            }) ;
            
        });
        
        
        //ympyrän piirtäminen napista
        button_ympyra.setOnAction (e->
        {
                scene.setOnMousePressed( ( MouseEvent event ) ->
                    {
                        if ( piirretaan == false )
                        {
                            aloituspiste_x = event.getSceneX() ;
                            aloituspiste_y = event.getSceneY() ;

                            uusi_ympyra = new Circle() ;
                            
                            uusi_ympyra.setStroke( valitse_vari.getValue() ) ;
                            uusi_ympyra.setFill( tayton_vari.getValue() ) ;

                            group.getChildren().add( uusi_ympyra ) ;

                            piirretaan = true ;
                        }
                } ) ;

                scene.setOnMouseDragged( ( MouseEvent event ) ->
                {
                    if ( piirretaan == true )
                    {
                        double nykyinen_lopetuspiste_x = event.getSceneX() ;
                        double nykyinen_lopetuspiste_y = event.getSceneY() ;

                        ympyran_ominaisuudet( aloituspiste_x,
                                                     aloituspiste_y,
                                                     nykyinen_lopetuspiste_x,
                                                     nykyinen_lopetuspiste_y,
                                                     uusi_ympyra ) ;
                    }
                } ) ;

                scene.setOnMouseReleased( ( MouseEvent event ) ->
                {
                    if ( piirretaan == true )
                    {
                        uusi_ympyra = null ;
                        piirretaan = false ;
                    }
                } ) ;
        });
        
        //viivan piirtäminen napista
        button_viiva.setOnAction (e->
        {            
             scene.setOnMousePressed( ( MouseEvent event ) ->
            {
                if ( piirretaan == false )
                {
                    aloituspiste_x = event.getSceneX() ;
                    aloituspiste_y = event.getSceneY() ;

                    viiva = new Line() ;
                    
                    viiva.setStroke( valitse_vari.getValue() ) ;
                    viiva.setFill( tayton_vari.getValue() ) ; 

                    group.getChildren().add( viiva ) ;

                    piirretaan = true ;
                }
            });

            scene.setOnMouseDragged( ( MouseEvent event ) ->
            {
                if ( piirretaan == true )
                {
                    double nykyinen_lopetuspiste_x = event.getSceneX() ;
                    double nykyinen_lopetuspiste_y = event.getSceneY() ;

                    viivan_ominaisuudet( aloituspiste_x,
                                                 aloituspiste_y,
                                                 nykyinen_lopetuspiste_x,
                                                 nykyinen_lopetuspiste_y,
                                                 viiva) ;
                }
            });
            
      
            scene.setOnMouseReleased( ( MouseEvent event ) ->
            {
                if ( piirretaan == true )
                {            
                    viiva= null ;
                    piirretaan = false ;
                }
            }) ;
            
        });
        
        //tyhjenna nappi luo pelkän valkoisen taustan
         button_tyhjenna.setOnAction (e->
        {                  
            group.getChildren().clear();//tyhjentää kaiken, paitsi kynällä piirrettyä??!!     
            piirto.clearRect(0,0,1000,600);//tyhjentää kynällä piirretyt
            group.getChildren().addAll( canvas, kiinnitys );//lisää tyokalupalkin ja valkoisenpohjan
                        
        });
         
        // animaatio  
        button_animaatio.setOnAction (e->
        {            
            Bounds bounds = canvas.getBoundsInLocal();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), 
                                new KeyValue(aurinko.layoutXProperty(), bounds.getMaxX() -aurinko.getRadius())));
            
            //miten saa hain liikkumaan toiseen suuntaan eli ikkunan vasempaan laitaan??
            Timeline hai = new Timeline(new KeyFrame(Duration.seconds(10), 
                                new KeyValue(kolmio.layoutXProperty(), bounds.getMaxX())));
            timeline.setCycleCount(1);
            hai.setCycleCount(1);
            timeline.play();
            hai.play();
                       
        });                
            
        group.getChildren().addAll( taivas, aurinko, meri, hiekka, kolmio, canvas, kiinnitys);
        
        
        stage.setScene(scene);//scene stagelle
        stage.show();//stage nakyviin
        
      
}
   
                  
    
    public static void main(String[] args) 
    {
        launch(args);
    }
    
}
