package condicionescompetencias;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;
public class Hilo extends Thread{
    private JTextArea area;
    private RCompartido rc;
    
    private final static int inicio = 995;
    private final static int fin = 1000;
    
    private boolean dead=false;
    private boolean block=false;
    private int algoritmo;
    
    //Dikjstra
    private int id;
    private static final int Nthreads = 4;
    private static volatile boolean[] arrayB = new boolean[Nthreads];
    private static volatile boolean[] arrayC = new boolean[Nthreads];
    private static volatile int k = 0;
    
    //Mutex
    private Lock mutexApi;
    private Mutex mutex;
    
    //
    private boolean apaga = false;
    
    Hilo(JTextArea area,RCompartido rc){
        this.area = area;
        this.rc = rc;
        mutexApi = new ReentrantLock();
        mutex = new Mutex();
    }
    
    public void run(){
        switch(this.algoritmo){
            case 1:     //Condiciones competencias
                while(true){
                    try{
                        rc.setrc(this.getName());
                        area.append(rc.getrc()+": Eats\n");
                        sleep(1000);
                        if(isDead()){   
                            stop();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            
            
            case 2: //Interrupciones
                while(true){
                    try{
                        if(isReinicio()==false){
                        if(rc.estadoDeLasInterrupciones()){
                            rc.desactivarInterrupciones();
                            rc.setrc(this.getName());
                            area.append(rc.getrc()+": Eats\n");
                            if(isDead()){   
                                stop();
                            }   
                            rc.activarInterrupciones();                                           
                        }
                        else{
                            area.append("En espera...\n");
                        }
                        Thread.sleep((int) (inicio+Math.random()*fin));
                        }
                        }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        
            case 3: //Cerradura
                while(true){
                    try{
                        if(isReinicio()==false){
                        if(rc.statusCerradura()){
                            rc.cerrarCerradura();
                            rc.setrc(this.getName());
                            area.append(rc.getrc()+": Eats\n");
                            if(isDead()){   
                                stop();
                            }
                            rc.abrirCerradura();
                        }
                        else{
                            area.append("En espera...\n");
                        }
                        Thread.sleep((int) (inicio+Math.random()*fin));
                    }
                    }catch(Exception e)
                        {e.printStackTrace();
                    }
                 }
            case 4: //Dekker
                while (true){
                    try{
                        if(isReinicio()==false){
                        rc.setH(rc.getTurno(), true);
                        while(disponible() != -1){
                            rc.setH(rc.getTurno(), false);
                            Thread.sleep((int)(inicio + Math.random() * fin));
                        }
                        rc.setH(rc.getTurno(), true);
                        rc.setrc(this.getName());
                        area.append(rc.getrc() + "\n");
                        if(isDead()){
                            stop();
                        }
                        rc.setH(rc.getTurno(), false);  
                        if(rc.getTurno() == 3){
                            rc.setTurno(0);
                        }
                        else{
                            rc.setTurno(rc.getTurno()+1);
                        }
                        Thread.sleep((int)(inicio + Math.random() * fin));
                        }
                        }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }    
            case 5: //Dijkstra
                dijkstra:
                while (true) {
                    if(isReinicio()==false){
                    arrayB[id] = true;
                    arrayB = arrayB;
                    while (k != id) {
                        arrayC[id] = false;
                        arrayC = arrayC;
                        if (!arrayB[k]) {
                            k = id;
                        }
                    }
                    arrayC[id] = true;
                    arrayC = arrayC;
                    for (int i = 0; i < arrayC.length; i++) {
                        if (i != id && arrayC[i]) {
                            System.out.println("Ocupado");
                            continue dijkstra;
                        }
                    }
                    try{
                        rc.setrc(this.getName());
                        area.append(rc.getrc()+"\n");
                        if(isDead()){
                            stop();
                        }
                        Thread.sleep(1000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    arrayC[id] = false;
                    arrayC = arrayC;
                    arrayB[id] = false;
                    arrayB = arrayB;
                }
                }
            case 6: //Mutex APPI
                while(true){
                    try{
                        if(mutexApi.tryLock()){
                            mutexApi.lock();
                            rc.setrc(this.getName());
                            area.append(rc.getrc()+": Eats" + "\n");
                            if(isDead()){   
                                stop();
                            }   
                            mutexApi.unlock();
                        }      
                        else{
                            area.append("En espera...\n");
                        }
                        Thread.sleep((int) (inicio+Math.random()*fin));
                    }catch(Exception e){e.printStackTrace();}
                }  
            case 7: //Mutex tradicional
                while(true){
                    try{
                        if(mutex.trylock()){
                            mutex.lock();
                            rc.setrc(this.getName());
                            area.append(rc.getrc()+": Eats" + "\n");
                            if(isDead()){   
                                stop();
                            }   
                            mutex.unlock();
                        }
                        else{
                            area.append("En espera...");
                        }
                        Thread.sleep((int) (inicio+Math.random()*fin));
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
            default:
                while(true){
                    try{
                        rc.setrc(this.getName());
                        area.append(rc.getrc()+": Eats\n");
                        sleep(1000);
                        if(isDead()){   
                            stop();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
        }
    }
    public boolean isDead(){
        return dead;
    }
    public void setDead(boolean d){
        dead=d;
    }
    public boolean isBloquear(){
        return block;
    }
    public void setBlock(boolean b){
        block=b;
    }

    public int getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(int algoritmo) {
        this.algoritmo = algoritmo;
    }
    
    public int disponible(){
        boolean b[] = rc.getH();
        for(int i=0;i<b.length;i++){
            if(rc.getTurno() != i){
                if(b[i]){
                    return i; 
                }
            }
        }
        return -1;
    }
    
    public void setID(int i){
        id = i;
    }

    public boolean isReinicio() {
        return apaga;
    }

    public void setReinicio(boolean reinicio) {
        this.apaga = reinicio;
    }
}
