package condicionescompetencias;
public class Cerradura {
    private boolean cerradura;
    
    public Cerradura(){
        this.cerradura = true;
    }
    
    public void cerrar (){
        this.cerradura = false;
    }
    
    public void abrir (){
        this.cerradura = true;
    }
    
    public boolean estado(){
        return cerradura;
    }

}

