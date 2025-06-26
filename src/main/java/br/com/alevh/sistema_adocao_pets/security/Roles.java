package br.com.alevh.sistema_adocao_pets.security;

public enum Roles {
    ADMIN("admin"),
    USER("usuario"),
    ONG("ong");

    private String role;

    Roles(String role){
        this.role = role;
    }
    public String getRole(){
        return role;
    }
}
