/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uac.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author davidcerchiaro
 */
public class ArbolString {

    /*
    **Expreciones
    -infija: 2*3+9/3
    -prefija: + *23 /93
    -posfija: 23* 93/ +
    
    1.	Digitar una expresión infija
    2.	Convertir la infija a posfija o prefija. Sino es posible, 
    crear un árbol binario de expresiones manualmente (o por el teclado). Por ejemplo, de la expresión.
    3.	Visualizar la notación prefija (aplicando recorrido preorden).   //R-i-d (raíz, izquierda, derecha)
    4.	Visualizar la notación posfija (aplicando recorrido posorden).   //i-d-R  (izquierda, derecha, raíz)
    5.	Visualizar la notación infija
    6.	Evaluar la expresión: En este caso debe hacer un algoritmo o método que a 
    partir de la notación prefija o infija se evalúe la expresión. Es decir, calcule el valor total.
    
     */
    //** iniciamos 
    //convertimos expreciones infijas a posfijas!!
    private Stack<Character> signos = new Stack<Character>();
    private Arbin<String> raiz = null;
    ArrayList<String> mostrar = new ArrayList<>();

    public void reiniciarArbol() {
        this.raiz = null;
        this.mostrar = new ArrayList<>();
    }

    public void convertInfijaPrefija(String infija) {

        insertar(infija);
    }

    public void insertarArbolPrefija(String prefija) {
        char[] listaC = prefija.toCharArray();
        for (int i = 0; i < listaC.length; i++) {
            if (listaC[i] != ' ') {
                insertar(listaC[i] + "");
            }
        }
    }

    private String convertInfijaPosfija(String infija) {
        char[] caracteres = infija.toCharArray();
        String salida = "";
        for (int i = 0; i < caracteres.length; i++) {
            //code here
            char termino = caracteres[i];
            if (termino == '(') {
                this.signos.push(termino);
            } else if (termino == ')') {
                while (true) {
                    if (this.signos.isEmpty()) {
                        System.out.println("Error!");
                        break;
                    }
                    char temporal = this.signos.pop().charValue();
                    if (temporal == '(') {
                        break;
                    } else {
                        salida += " " + temporal;
                    }
                }
            } else if (Character.isDigit(termino)) { // Character.isDigit(termino:char||string); verifica que dicho elemento sea un numero
                salida += " " + termino;
                i++;
                busqueda:
                for (; i < caracteres.length; i++) {
                    if (Character.isDigit(caracteres[i])) {
                        salida += caracteres[i];
                    } else {
                        i--;
                        break busqueda;
                    }
                }
            } else if (termino == '*' || termino == '/' || termino == '+' || termino == '-' || termino == '^') {
                if (this.signos.empty()) {
                    this.signos.push(termino);
                } else {
                    while (true) {
                        if (Presedencia(termino)) {
                            this.signos.push(termino);
                            break;
                        } else {
                            salida += " " + this.signos.pop();
                        }
                    }
                }
            } else {
                System.out.println("Caracter " + termino + " no valido");
                break;
            }
            //System.out.println((i+1)+". "+salida);
        }
        if (!signos.empty()) {
            do {
                char temp = this.signos.pop().charValue();
                salida += " " + temp;
            } while (!signos.empty());

        }
        System.out.println(salida);

        return salida;
    }

    public void salidaPosfija(String infija){
        insertarArbolPosfija(convertInfijaPosfija(infija));
        postorden();
    }
    
    //** Calcular infijo
    public String calcularInfijo(String infijo){
    ;
        return evaluarInfijaPosfija(convertInfijaPosfija(infijo));
    }
    
    private String evaluarInfijaPosfija(String infija) {
        String[] post = infija.split(" ");
        //Declaración de las pilas
        Stack< String> E = new Stack< String>(); //Pila entrada
        Stack< String> P = new Stack< String>(); //Pila de operandos

        //Añadir post (array) a la Pila de entrada (E)
        for (int i = post.length - 1; i >= 0; i--) {
            E.push(post[i]);
        }

        //Algoritmo de Evaluación Postfija
        String operadores = "+-*/%";
        while (!E.isEmpty()) {
            if (operadores.contains("" + E.peek())) {
                P.push(evaluar(E.pop(), P.pop(), P.pop()) + "");
            } else {
                P.push(E.pop());
            }
        }

        //Mostrar resultados:
        System.out.println("Expresion: " + infija);
        //System.out.println("Resultado: " + P.peek());
        return P.peek();
    }

    private int evaluar(String op, String n2, String n1) {
        int num1 = Integer.parseInt(n1);
        int num2 = Integer.parseInt(n2);
        if (op.equals("+")) {
            return (num1 + num2);
        }
        if (op.equals("-")) {
            return (num1 - num2);
        }
        if (op.equals("*")) {
            return (num1 * num2);
        }
        if (op.equals("/")) {
            return (num1 / num2);
        }
        if (op.equals("%")) {
            return (num1 % num2);
        }
        return 0;
    }

    private boolean Presedencia(char termino) {

        if (this.signos.empty()) {
            return true;
        }
        if (termino == signos.peek().charValue()) {
            return false;
        }
        if (termino == '^') {
            return true;
        }
        if ((termino == '*' && signos.peek().charValue() == '/') || (termino == '/' && signos.peek().charValue() == '*')) {
            return false;
        }
        if ((termino == '+' && signos.peek().charValue() == '-') || (termino == '-' && signos.peek().charValue() == '+')) {
            return false;
        } else if (termino == '-' || termino == '+') {
            char temp = signos.peek().charValue();
            if (temp == '*' || temp == '/') {
                return false;
            }
        }
        return true;
    }

    private void insertarArbolPosfija(String posfila) {
        char[] listaC = posfila.toCharArray();
        for (int i = listaC.length - 1; i > 0; i--) {
            if (listaC[i] != ' ') {
                insertar(listaC[i] + "");
            }
        }
    }

    public void insertarArbol(String posfila) {
        char[] listaC = posfila.toCharArray();
        for (int i = listaC.length - 1; i > 0; i--) {
            //System.out.print(listaC[i]);
            if (listaC[i] != ' ') {
                insertar(listaC[i] + "");
            }
        }
    }

    private void insertar(String elemt) {
        if (this.raiz == null) {
            this.raiz = new ArbolEnl<>(elemt);
        } else {
            insertar(this.raiz, elemt);
        }
    }

    private void insertar(Arbin<String> raiz, String elemt) {
        if (elemt == "+" || elemt == "-" || elemt == "*" || elemt == "/") {
            if (raiz.izq() == null) {
                raiz.enlIzq(new ArbolEnl<>(elemt));
            } else {
                insertar(raiz.izq(), elemt);
            }
        } else {
            if (raiz.der() == null) {
                raiz.enlDer(new ArbolEnl<>(elemt));
            } else {
                insertar(raiz.der(), elemt);
            }
        }
    }

    //RECORRIENDO EL ARBOL
    private void preorden() {
        if (this.raiz != null) {
            //System.out.println(" " + this.raiz.obtener()); //R
            mostrar.add(this.raiz.obtener());
            preorden(this.raiz.izq()); //i
            preorden(this.raiz.der()); //d
        }
    }

    private void preorden(Arbin<String> r) {
        if (r != null) {
            ///System.out.println(" " + r.obtener()); //R
            mostrar.add(r.obtener());
            preorden(r.izq()); //i
            preorden(r.der()); //d
        }
    }

    //I_D-R
    private void postorden() {
        if (this.raiz != null) {
            postorden(this.raiz.izq()); //I
            postorden(this.raiz.der()); //D
            //System.out.println(" " + this.raiz.obtener());//R
            mostrar.add(this.raiz.obtener());

        }
    }

    private void postorden(Arbin<String> r) {
        if (r != null) {
            postorden(r.izq()); //I
            postorden(r.der()); //D
            //System.out.println(" " + r.obtener());//R
            mostrar.add(r.obtener());

        }
    }

}
