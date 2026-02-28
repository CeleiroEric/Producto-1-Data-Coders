public class Cliente_premium extends Cliente {
    private double cuota_anual;
    private double descuento;
    private String tipo;

    public Cliente_premium(String nombre, String domicilio, String nif, String email,double cuota_anual, double descuento ,String tipo) {
        super(nombre, domicilio, nif, email);
        this.cuota_anual = 30.0;
        this.descuento = 0.20;
        this.tipo = "Estandar";
    }

    public String getTipo(){
        return tipo;
    }

    public double getCuotaAnual(){
        return cuota_anual;
    }

    public void setCuotaAnual(double cuota_anual){
        this.cuota_anual=cuota_anual;
    }

    public double getDescuento(){
        return descuento;
    }

    public void setDescuento(double descuento){
        this.descuento=descuento;
    }

    //Cambiar al correcto nombre
    @Override
    public double getFactorEnvio(){
        return Producto.getPrecioEnvio * 0.80;
    }
    public String toString(){
        return super.toString() + "Tipo: " + tipo + "/ Cuota anual: " + cuota_anual
                + "/ Descuento: " + descuento;
    }
    }