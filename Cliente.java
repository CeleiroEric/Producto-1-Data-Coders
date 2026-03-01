public abstract class Cliente {
    private String nombre;
    private String domicilio;
    private String nif;
    private String email;

    public Cliente(String nombre, String domicilio, String nif, String email) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obligatorio");
        }

        this.nombre = nombre.trim();
        this.domicilio = (domicilio == null) ? "" : domicilio.trim();
        this.nif = (nif == null) ? "" : nif.trim();
        this.email = email.trim();
    }
    public String getNombre(){
        return nombre;
    }
    public String getDomicilio(){
        return domicilio;
    }
    public String getNif(){
        return nif;
    }
    public String getEmail(){
        return email;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setDomicilio(String domicilio){
        this.domicilio = domicilio;
    }
    public void setNif(String nif){
        this.nif = nif;
    }
    public void setEmail(String email){
        this.email = email;
    }

    //Actualizar al correcto nombre
    public double getFactorEnvio(Articulo articulo){

        return articulo.getGastosEnvio() * 1;
    }
    public String toString(){
        return "Cliente{ Nombre: "+ nombre + "/ Domicilio: " + domicilio +
                "/ NIF: " + nif + "/ Email: " + email + "}";
    }
}
