package uqvirutal.edu.co.parcialdosp2.model;

record Resultado<T>(boolean exitoso, String mensaje, T valor) {

    public static <T> Resultado<T> exito(T valor) {
        return new Resultado<>(true, "", valor);
    }

    public static <T> Resultado<T> exito() {
        return new Resultado<>(true, "", null);
    }

    public static <T> Resultado<T> error(String mensaje) {
        return new Resultado<>(false, mensaje, null);
    }

    public boolean isExitoso() { return exitoso; }
    public String getMensaje() { return mensaje; }
    public T getValor() { return valor; }
}