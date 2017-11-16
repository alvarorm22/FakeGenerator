/**
 * Created by alvaro on 12/07/17.
 */
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.Math;
import java.util.Queue;
import java.util.LinkedList;


public class Main {

    public static void main(String []args) throws Exception{

        System.out.print(args[0]);
        System.out.print(args[1]);

        int idMax=Integer.parseInt(args[1]);
        int idMin=Integer.parseInt(args[0]);

        Queue <sensor> controlMovimiento = new LinkedList<sensor>();
        Queue <sensor> controlHumo = new LinkedList<sensor>();
        Queue <sensor> controlTemperatura = new LinkedList<sensor>();

        //int N=100; int M=95; int n=99; int m=0;
        int k; int i;
        int tamañoSensores=idMax-idMin;
        sensor sensores[] = new sensor[tamañoSensores];

        for (i=idMin,k=0 ; i<idMax; i++,k++){
            sensores[k]=new sensor(i);
        }

        while(true){
            for (i=0; i<tamañoSensores; i++){
                sendPost(sensores[i], "notok");
                double aleatorio = Math.floor(Math.random()*101);
                System.out.println("Aleatorio : "+aleatorio);
                if (i%5000==0 && i>1) generarAlarma(aleatorio, idMax,idMin, sensores, controlMovimiento,controlHumo,controlTemperatura,i);
                if (i%12000==0 && i>1) limpiarAlarmas(controlHumo,controlMovimiento,controlTemperatura);
                controlAlarmasActivas(sensores[i], controlTemperatura,controlMovimiento,controlHumo);
            }
        }

    }

    public static void generarAlarma(double aleatorio, int idMax, int idMin, sensor [] sensores,
                              Queue<sensor>controlMovimiento, Queue<sensor>controlHumo,Queue<sensor>controlTemperatura, int random) throws Exception{
        //int sens = (int)Math.floor(Math.random()*(idMax-idMin+1)+idMin);
        int sens = (int)Math.floor(Math.random()*random);
        if (aleatorio > 90) {
            generarAlarmaMovimiento(sensores[sens], controlMovimiento);
            sendPost(sensores[sens], "notok");
            System.out.println("Alarma de Movimiento generada para el sensor con id : "+ sensores[sens].getIdSensor() );
        }
        else if (aleatorio >= 80 && aleatorio <= 90) {
            generarAlarmaHumo(sensores[sens], controlHumo);
            sendPost(sensores[sens], "notok");
            System.out.println("Alarma de Humo generada para el sensor con id : "+ sensores[sens].getIdSensor() );
        }
        else if (aleatorio >= 0 && aleatorio < 80){
            subirTemperatura(sensores[sens], controlTemperatura);
            System.out.println("Sube la temperatura para el sensor con id : "+ sensores[sens].getIdSensor() +
                    ". La temperatura actual es de "+ sensores[sens].getTemperatura() +
                    " || La temperatura máxima es : "+ sensores[sens].getTempMax());
            if (sensores[sens].getTemperatura() >= sensores[sens].getTempMax()){
                sendPost(sensores[sens], "notok");
                System.out.println("--> Se ha sobrepasado la temperatura máxima para el sensor");
            }

        }


    }

    public static void limpiarAlarmas(Queue<sensor>controlHumo, Queue<sensor>controlMovimiento,
                               Queue<sensor>controlTemperatura) throws Exception {
        sensor humo = controlHumo.poll();
        sensor mov = controlMovimiento.poll();
        sensor temperatura = controlTemperatura.poll();

        if (humo!=null){
            humo.setHumo(false);
            humo.resetCountHumo();
            System.out.println("Se recupera la alerta de humo para el sensor con id : " + humo.getIdSensor());
            sendPost(humo, "ok");
        }
        if (mov!=null){
            mov.setMovimiento(false);
            System.out.println("Se recupera la alerta de movimiento para el sensor con id : " + mov.getIdSensor());
            sendPost(mov, "ok");
        }
        if (temperatura != null){
            temperatura.setTemperatura(0);
            System.out.println("Se recupera la alerta de temperatura para el sensor con id : " + temperatura.getIdSensor());
            sendPost(temperatura, "ok");
        }

    }

    public static void sendPost(sensor s, String topic) throws Exception {

        String url = null;

        if (topic.equals("notok")){
            url = "http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/ENTRY/messages";
        }else if (topic.equals("ok")) url = "http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/OK/messages";


        //url = "http://TFM-982204392.us-west-2.elb.amazonaws.com/topics/demo/messages";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        //con.setRequestProperty("Content-Type", "text/plain");

        //String urlParameters = "{"+'"'+"id"+'"'+":"+'"'+s.getIdSensor()+'"'+","+'"'+"movimiento"+'"'+":"+'"'+s.getMovimiento()+'"'+","+'"'+"humo"+'"'+
               //":"+'"'+s.getHumo()+'"'+","+'"'+"temperatura"+'"'+":"+'"'+s.getTemperatura()+'"'+","+'"'+"tempMaxima"+'"'+":"+'"'+s.getTempMax()+'"'+"}";
        //String urlParameters = "{"+'"'+s.getIdSensor()+'"'+","+'"'+s.getMovimiento()+'"'+","+'"'+s.getHumo()+'"'+","+'"'+s.getTemperatura()+'"'+","+'"'+s.getTempMax()+'"'+"}";
        String urlParameters = s.getIdSensor()+","+s.getMovimiento()+","+s.getHumo()+","+s.getTemperatura()+","+s.getTempMax();

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

    }

    public static void generarAlarmaMovimiento(sensor s, Queue <sensor> controlMovimiento){
        s.setMovimiento(true);
        controlMovimiento.add(s);
    }
    public static void generarAlarmaHumo(sensor s,Queue <sensor> controlHumo){
        s.setHumo(true);
        s.addHumo();
        if(s.getCountHumo()>=3) {
            controlHumo.add(s);
            s.resetCountHumo();
        }
    }
    public static void subirTemperatura(sensor s, Queue <sensor> controlTemperatura) throws Exception {
        s.subirTemperatura();
        s.addTemperatura();
        if (s.getTemperatura() >= s.getTempMax()) {
            if (s.getCountTemperatura() >= 3) {
                controlTemperatura.add(s);
                s.resetCountTemperatura();
            }
        }
    }
        public static void controlAlarmasActivas(sensor s, Queue <sensor> controlTemperatura, Queue <sensor>
                                          controlMovimiento, Queue <sensor> controlHumo){
            if (s.getTemperatura()>=s.getTempMax()){
                s.addTemperatura();
                if (s.getCountTemperatura() >= 3) {
                    System.out.println("Alarma de temperatura generada x3 para el sensor con id : "+ s.getIdSensor() );
                    controlTemperatura.add(s);
                    s.resetCountTemperatura();
                }
            }

            if (s.getHumo()){
                s.addHumo();
                if(s.getCountHumo()>=3) {
                    System.out.println("Alarma de humo generada x3 para el sensor con id : "+ s.getIdSensor() );
                    controlHumo.add(s);
                    s.resetCountHumo();
                }
            }

    }






    public static void bajarTemperatura(sensor s){
        s.bajarTemperatura();
    }

}