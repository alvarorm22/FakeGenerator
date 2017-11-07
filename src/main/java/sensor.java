/**
 * Created by alvaro on 12/07/17.
 */
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;
import java.security.SecureRandom;

import static java.security.SecureRandom.*;

public class sensor {
    int idSensor;
    boolean movimiento;
    int temperatura;
    boolean humo;
    double tempMaxima;


    public sensor(int id) {
        idSensor=id;
        //if(id%2==0){
            movimiento=false;
        //}else movimiento = true;
        humo=false;
        temperatura=0;
        tempMaxima = (int) Math.floor(Math.random()*50) +1;


    }

    public void setIdSensor(int i){
        idSensor=i;
    }
    public int getIdSensor(){
        return idSensor;
    }
    public int getTemperatura(){
        return temperatura;
    }
    public void setTemperatura(int t) { temperatura = t; }
    public boolean getMovimiento(){
        return movimiento;
    }
    public void setMovimiento(boolean e){ movimiento=e; }
    public boolean getHumo(){
        return humo;
    }
    public void setHumo(boolean h){ humo = h; }
    public double getTempMax(){ return tempMaxima; }


    public void subirTemperatura(){
        temperatura+=5;
    }
    public void bajarTemperatura(){
        temperatura-=2;
    }


}

