package aplicacaofinanceira.deserializer;

import aplicacaofinanceira.model.Agencia;

public class ContaCorrenteDeserializer {
 
    private Long id;
    private int numero;
    private float saldo;
    private String dataDeAbertura;
    private float limite;
    private Agencia agencia;

    public ContaCorrenteDeserializer() {}

    public Long getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public float getSaldo() {
        return saldo;
    }

    public String getDataDeAbertura() {
        return dataDeAbertura;
    }

    public float getLimite() {
        return limite;
    }    

    public Agencia getAgencia() {
        return agencia;
    }    
}