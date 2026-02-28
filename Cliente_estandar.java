public class Cliente_estandar extends Cliente{
    private String tipo;

    public Cliente_estandar(String nombre, String domicilio, String nif, String email, String tipo){
        super(nombre,domicilio,nif,email);
        this.tipo = "Estandar";
    }
    public String getTipo(){
        return tipo;
    }

    public String toString(){
    return super.toString() + "Tipo: " + tipo;
    }
}
