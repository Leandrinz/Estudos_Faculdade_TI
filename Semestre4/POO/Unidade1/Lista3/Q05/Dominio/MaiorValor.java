package Semestre4.POO.Unidade1.Lista3.Q05.Dominio;

public class MaiorValor {
    public static int maior(int a, int b){
        if (a > b){
            return a;
        }
        else{
            return b;
        }
    }

    public static double maior(double a, double b){
        if (a > b){
            return a;
        }
        else{
            return b;
        }
    }

    public static int maior(int a, int b, int c){
        if (a > b && a > c){
            return a;
        }
        else if (b > a && b > c){
            return b;
        }
        else{
            return c;
        }
    }

    public static double maior(double a, double b, double c){
        if (a > b && a > c){
            return a;
        }
        else if (b > a && b > c){
            return b;
        }
        else{
            return c;
        }
    }

    public static int maior(int a, int b, int c, int d){
        if (a > b && a > c && a > d){
            return a;
        }
        else if (b > a && b > c && b > d){
            return b;
        }
        else if (c > a && c > b && c > d){
            return c;
        }
        else{
            return d;
        }
    }

    public static double maior(double a, double b, double c, double d){
        if (a > b && a > c && a > d){
            return a;
        }
        else if (b > a && b > c && b > d){
            return b;
        }
        else if (c > a && c > b && c > d){
            return c;
        }
        else{
            return d;
        }
    }

    public static int maior(int a, int b, int c, int d, int e){
        if (a > b && a > b && a > c && a > d && a > e){
            return a;
        }
        else if (b > a && b > c && b > d && b > e){
            return b;
        }
        else if (c > a && c > b && c > d && c > e){
            return c;
        }
        else if (d > a && d > b && d > c && d > e){
            return d;
        }
        else {
            return e;
        }
    }

    public static double maior(double a, double b, double c, double d, double e){
        if (a > b && a > b && a > c && a > d && a > e){
            return a;
        }
        else if (b > a && b > c && b > d && b > e){
            return b;
        }
        else if (c > a && c > b && c > d && c > e){
            return c;
        }
        else if (d > a && d > b && d > c && d > e){
            return d;
        }
        else {
            return e;
        }
    }
}
