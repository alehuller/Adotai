package br.com.alevh.sistema_adocao_pets;

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        super();
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
