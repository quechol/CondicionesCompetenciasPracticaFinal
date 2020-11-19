package condicionescompetencias;
public class Interrupcion {
    private boolean estado;
    
    public Interrupcion(){
        this.estado = true;  
    }
    
    public void desactivar(){
        this.estado = false;
    }
    
    public void activar(){
        this.estado = true;
    }
    public boolean estado(){
        return estado;
    }
}
